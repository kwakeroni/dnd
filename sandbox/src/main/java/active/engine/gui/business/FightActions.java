package active.engine.gui.business;

import active.engine.command.Command;
import active.model.cat.Actor;
import active.model.fight.FightController;
import active.model.fight.command.*;
import active.model.fight.event.FightData;

import java.io.File;
import java.util.function.Predicate;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface FightActions<ActionGUI> {

    default void initBehaviour(FightData fight){
        addAction(fight, "Start Turn", 'S', FightActions::noTurnActive, new StartTurn());
        addAction(fight, "Next Turn", 'N', FightActions::isTurnActive, new NextTurn());
        addAction(fight, "End Turn", null, FightActions::isTurnActive, new EndTurn());
        addAction(fight, "Attack", 'A', FightActions::isTurnActive,
                context -> {
                    FightController f = context.getContext(FightController.class);
                    Actor actor = f.getState().getCurrentActor().get();
                    SelectParticipant.hittable(target -> new Attack(actor, target)).execute(context);
                });
    }

    default void addAction(FightData fight, String name, Character mnemonic, Predicate<FightData> enabled, Command command){
        ActionGUI actionGUI = addActionGUI(name, mnemonic, command);
        fight.turn().onChanged(() -> setEnabled(actionGUI, enabled.test(fight)));
        setEnabled(actionGUI, enabled.test(fight));
    }

    ActionGUI addActionGUI(String name, Character mnemonic, Command command);
    void setEnabled(ActionGUI gui, boolean enabled);

    static boolean  isTurnActive(FightData f){
        return f.turn().get().isPresent();
    }
    static boolean noTurnActive(FightData f){
        return ! isTurnActive(f);
    }


}
