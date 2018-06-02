package be.kwakeroni.dnd.engine.command;

import be.kwakeroni.dnd.engine.command.fight.AddParty;
import be.kwakeroni.dnd.engine.command.fight.Attack;
import be.kwakeroni.dnd.engine.command.fight.ContinueFight;
import be.kwakeroni.dnd.engine.command.fight.EndFight;
import be.kwakeroni.dnd.engine.command.fight.EndTurn;
import be.kwakeroni.dnd.engine.command.fight.NextTurn;
import be.kwakeroni.dnd.engine.command.fight.StartFight;
import be.kwakeroni.dnd.engine.command.fight.StartTurn;
import be.kwakeroni.dnd.engine.api.FightController;
import be.kwakeroni.dnd.engine.api.InteractionHandler;
import be.kwakeroni.dnd.engine.api.command.Command;
import be.kwakeroni.dnd.engine.api.command.fight.FightCommandFactory;
import be.kwakeroni.dnd.model.actor.Actor;
import be.kwakeroni.dnd.model.creature.Party;
import be.kwakeroni.dnd.model.fight.Fight;
import be.kwakeroni.dnd.model.target.Hittable;

public class DefaultFightCommandFactory implements FightCommandFactory {

    @Override
    public Command.WithResult<FightController> startFight() {
        return new StartFight();
    }

    @Override
    public Command.WithResult<FightController> continueFight(Fight fight) {
        return new ContinueFight(fight);
    }

    @Override
    public Command endFight() {
        return new EndFight();
    }

    @Override
    public Command addParty(Party party) {
        return new AddParty(party);
    }

    @Override
    public Command startTurn() {
        return new StartTurn();
    }

    @Override
    public Command nextTurn() {
        return new NextTurn();
    }

    @Override
    public Command endTurn() {
        return new EndTurn();
    }

    @Override
    public Command.WithResult<InteractionHandler.InteractiveAttackData> initiateAttack(Hittable target) {
        return new Attack(target);
    }

    @Override
    public Command.WithResult<Fight> getState() {
        return context -> context.getContext(FightController.class).getState();
    }

}
