package active.engine.gui.business;

import active.engine.command.Command;
import active.model.cat.Actor;
import active.model.fight.FightController;
import active.model.fight.Participant;
import active.model.fight.command.Attack;
import active.model.fight.command.EndTurn;
import active.model.fight.command.NextTurn;
import active.model.fight.command.SelectParticipant;
import active.model.fight.command.StartTurn;
import active.model.fight.event.FightData;

import java.util.function.Predicate;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface FightActions<ActionGUI> {

    default void initBehaviour(FightData fight) {
        addAction(fight, "Start Turn", 'S', test(FightActions::noTurnActive).and(FightActions::hasActors), new StartTurn());
        addAction(fight, "Next Turn", 'N', test(FightActions::isTurnActive).and(FightActions::hasActors), new NextTurn());
        addAction(fight, "End Turn", null, FightActions::isTurnActive, new EndTurn());
        addAction(fight, "Attack", 'A', test(FightActions::isTurnActive).and(FightActions::hasActors),
                context -> {
                    FightController f = context.getContext(FightController.class);
                    Actor actor = f.getState().getCurrentActor().get();
                    SelectParticipant.hittable(target -> new Attack(actor, target)).execute(context);
                });
    }

    default void addAction(FightData fight, String name, Character mnemonic, Predicate<FightData> enabled, Command command) {
        ActionGUI actionGUI = addActionGUI(name, mnemonic, command);
        fight.participants().onChanged(() -> setEnabled(actionGUI, enabled.test(fight)));
        fight.turn().onChanged(() -> setEnabled(actionGUI, enabled.test(fight)));
        setEnabled(actionGUI, enabled.test(fight));
    }

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
