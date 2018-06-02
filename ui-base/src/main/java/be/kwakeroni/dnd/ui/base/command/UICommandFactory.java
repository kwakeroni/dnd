package be.kwakeroni.dnd.ui.base.command;

import be.kwakeroni.dnd.engine.api.FightController;
import be.kwakeroni.dnd.engine.api.InteractionHandler;
import be.kwakeroni.dnd.engine.api.command.Command;
import be.kwakeroni.dnd.engine.api.command.CommandContext;
import be.kwakeroni.dnd.engine.api.command.fight.FightCommandFactory;
import be.kwakeroni.dnd.engine.api.command.io.IOCommandFactory;
import be.kwakeroni.dnd.model.fight.Participant;
import be.kwakeroni.dnd.model.target.Hittable;
import be.kwakeroni.dnd.type.collection.Box;
import be.kwakeroni.dnd.ui.base.fight.FightLogger;
import be.kwakeroni.dnd.ui.base.GUIController;
import be.kwakeroni.dnd.ui.base.PluggableContent;
import be.kwakeroni.dnd.ui.base.fight.PluggableFightUI;
import be.kwakeroni.dnd.util.function.Pair;

import java.io.File;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static be.kwakeroni.dnd.engine.api.command.Command.WithFollowUp.followUpWith;
import static be.kwakeroni.dnd.engine.api.command.Command.WithResult.*;
import static be.kwakeroni.dnd.engine.api.command.Command.execute;

public interface UICommandFactory<WindowType, ContentType> {

    public interface CurrentFightFile {
        File getFile();
    }

    FightCommandFactory getFightCommandFactory();

    IOCommandFactory getIOCommandFactory();

    public IOCommandFactory.FileSelection getImportPartyDescriptor();

    public IOCommandFactory.FileSelection getExportFightDescriptor();

    public IOCommandFactory.FileSelection getImportFightDescriptor();

    public PluggableFightUI<? super WindowType, ContentType> getFightUI(FightController fightController);

    public default Command hideFight() {
        return context -> {
            getGUIController(context).clearContent(null);
        };
    }

    public default Command attack() {
        return this.selectAndAttack();
    }

    private <PH extends Hittable & Participant> Command selectAndAttack() {
        FightCommandFactory fightCommand = getFightCommandFactory();
        return this.<PH> selectTarget()
                .andThen(execute(this::attackTarget));
    }

    private <PH extends Hittable & Participant> Command attackTarget(PH target) {
        FightCommandFactory fightCommand = getFightCommandFactory();
        return fightCommand.initiateAttack(target)
                .andThen(followUpWith(this::requestAttackData))
                .andThen(InteractionHandler.InteractiveAttackData::execute);

    }

    public default <T extends Participant> Command.WithFollowUp<T> selectParticipant(Class<T> type) {
        return (context, followUpAction) ->
            context.getContext(InteractionHandler.class)
                    .withParticipant( participant -> followUpAction.accept( type.cast(participant) ));
    }

    public default <PH extends Hittable & Participant> Command.WithFollowUp<PH> selectTarget() {
        return selectParticipant(generic(Hittable.class));
    }

    public default Command.WithFollowUp<InteractionHandler.InteractiveAttackData> requestAttackData(InteractionHandler.InteractiveAttackData initialData) {
        return (context, followUpAction) ->
                    context.getContext(InteractionHandler.class)
                            .withAttackData(initialData, followUpAction);
    }

    public default Command importPartiesToFight() {
        FightCommandFactory fightCommand = getFightCommandFactory();
        IOCommandFactory ioCommand = getIOCommandFactory();

        return ioCommand.selectFile(getImportPartyDescriptor())
                .andIfPresent(get(ioCommand::importParties))
                .andIfPresent(fightCommand::addParties);
    }

    public default Command exportFight() {
        IOCommandFactory ioCommand = getIOCommandFactory();

        return exportFightWith( fileSelection ->
                getCurrentFightFile()
                    .or(() -> ioCommand.selectFile(fileSelection)));
    }

    public default Command exportFightAs() {
        return exportFightWith(getIOCommandFactory()::selectFile);
    }

    private Command exportFightWith(Function<IOCommandFactory.FileSelection, WithOptionalResult<File>> selection) {
        FightCommandFactory fightCommand = getFightCommandFactory();
        IOCommandFactory ioCommand = getIOCommandFactory();

        Box<File> exportedFile = new Box<>();

        return selection.apply(getExportFightDescriptor())
                .andIfPresent(map(exportedFile::capture))
                .andIfPresent(pairWith(fightCommand::getState))
                .andIfPresent(map(Pair::invert))
                .andIfPresent(execute(ioCommand::exportFight))
                .andThen(registerContext(CurrentFightFile.class, () -> exportedFile::get))
                ;
    }

    private Command.WithOptionalResult<File> getCurrentFightFile() {
        return context -> context.getOptional(CurrentFightFile.class).map(CurrentFightFile::getFile);
    }

    public default Command importFight() {
        IOCommandFactory ioCommand = getIOCommandFactory();
        FightCommandFactory fightCommand = getFightCommandFactory();

        Box<File> importedFile = new Box<>();

        return ioCommand.selectFile(getImportFightDescriptor())
                .andIfPresent(map(importedFile::capture))
                .andIfPresent(get(ioCommand::importFight))
                .andIfPresent(get(fightCommand::continueFight))
                .andIfPresent(also(this::showFight))
                ._andIfPresent(also(registerContext(CurrentFightFile.class, () -> importedFile::get)))
                ;
    }

    public default Command startFight() {
        FightCommandFactory fightCommand = getFightCommandFactory();
        return fightCommand.startFight()
                .andThen(execute(this::showFight));
    }

    public default Command endFight() {
        FightCommandFactory fightCommand = getFightCommandFactory();

        return hideFight()
                .andThen(fightCommand::endFight);
    }

    public default Command showFight(FightController controller) {
        return context -> {
            GUIController<WindowType, ContentType> guiController = getGUIController(context);
            PluggableFightUI<? super WindowType, ContentType> ui = getFightUI(controller);
            guiController.setContent(ui);
            guiController.registerContext(FightController.class, ui.getFightController());
            guiController.registerContext(FightLogger.class, ui.getFightLogger());
            guiController.registerContext(InteractionHandler.class, ui.getInteractionHandler());
        };
    }

    private <T> Function<T, Command> registerContext(Class<T> $class){
        return value -> context -> getGUIController(context).registerContext($class, value);
    }

    private <T, S> Function<T, Command> registerContext(Class<S> $class, Function<T, S> valueFunction){
        return value -> context -> getGUIController(context).registerContext($class, valueFunction.apply(value));
    }

    private <T> Supplier<Command> registerContext(Class<T> $class, Supplier<T> value){
        return () -> context -> getGUIController(context).registerContext($class, value.get());
    }

    private GUIController<WindowType, ContentType> getGUIController(CommandContext context){
        return context.getContext(generic(GUIController.class));
    }

    private static <X> Class<X> generic(Class<? super X> $class){
        return (Class<X>) (Class<?>) $class;
    }

}
