package be.kwakeroni.dnd.engine.api.command.fight;

import be.kwakeroni.dnd.engine.api.FightController;
import be.kwakeroni.dnd.engine.api.InteractionHandler;
import be.kwakeroni.dnd.engine.api.command.Command;
import be.kwakeroni.dnd.engine.api.BattleField;
import be.kwakeroni.dnd.model.actor.Actor;
import be.kwakeroni.dnd.model.creature.Party;
import be.kwakeroni.dnd.model.fight.Fight;
import be.kwakeroni.dnd.model.target.Hittable;

import java.util.Collection;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public interface FightCommandFactory {

    public Command.WithResult<FightController> startFight();

    public Command.WithResult<FightController> continueFight(Fight fight);

    public Command endFight();

    public default Command addParties(Collection<Party> parties) {
        return parties.stream()
                .map(this::addParty)
                .collect(collectingAndThen(toList(), Command::combine));
    }

    public Command addParty(Party party);

    public Command startTurn();

    public Command nextTurn();

    public Command endTurn();

    public Command.WithResult<InteractionHandler.InteractiveAttackData> initiateAttack(Hittable target);

    public Command.WithResult<Fight> getState();
}
