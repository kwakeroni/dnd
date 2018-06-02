package be.kwakeroni.dnd.ui.base.action;

import be.kwakeroni.dnd.engine.api.FightController;
import be.kwakeroni.dnd.engine.api.command.Command;
import be.kwakeroni.dnd.engine.api.command.fight.FightCommandFactory;
import be.kwakeroni.dnd.model.actor.Actor;
import be.kwakeroni.dnd.model.fight.Participant;
import be.kwakeroni.dnd.model.fight.event.FightData;
import be.kwakeroni.dnd.ui.base.command.UICommandFactory;

import java.util.function.Predicate;

import static be.kwakeroni.dnd.engine.api.command.Command.WithResult.get;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface FightActions<ActionGUI> {

    default void initBehaviour(FightData fight) {
        addAction(fight, "Start Turn", 'S', test(FightActions::noTurnActive).and(FightActions::hasActors), getFightCommandFactory().startTurn());
        addAction(fight, "Next Turn", 'N', test(FightActions::isTurnActive).and(FightActions::hasActors), getFightCommandFactory().nextTurn());
        addAction(fight, "End Turn", null, FightActions::isTurnActive, getFightCommandFactory().endTurn());
        addAction(fight, "Attack", 'A', test(FightActions::isTurnActive).and(FightActions::hasActors), getUICommandFactory().attack());
    }

    default void addAction(FightData fight, String name, Character mnemonic, Predicate<FightData> enabled, Command command) {
        ActionGUI actionGUI = addActionGUI(name, mnemonic, command);
        fight.participants().onChanged(() -> setEnabled(actionGUI, enabled.test(fight)));
        fight.turn().onChanged(() -> setEnabled(actionGUI, enabled.test(fight)));
        setEnabled(actionGUI, enabled.test(fight));
    }

    FightCommandFactory getFightCommandFactory();

    UICommandFactory<?, ?> getUICommandFactory();

    ActionGUI addActionGUI(String name, Character mnemonic, Command command);

    void setEnabled(ActionGUI gui, boolean enabled);

    private static boolean hasActors(FightData f) {
        return f.participants().get().anyMatch(Participant::isActor);
    }

    private static boolean isTurnActive(FightData f) {
        return f.turn().get().isPresent();
    }

    private static boolean noTurnActive(FightData f) {
        return !isTurnActive(f);
    }

    private static Predicate<FightData> test(Predicate<FightData> predicate) {
        return predicate;
    }

}
