package be.kwakeroni.dnd.engine.command.fight;

import be.kwakeroni.dnd.engine.api.command.Command;
import be.kwakeroni.dnd.engine.api.command.CommandContext;
import be.kwakeroni.dnd.engine.api.InteractionHandler;
import be.kwakeroni.dnd.engine.action.HitAction;
import be.kwakeroni.dnd.model.actor.Actor;
import be.kwakeroni.dnd.model.target.Hittable;
import be.kwakeroni.dnd.model.effect.Damage;
import be.kwakeroni.dnd.engine.api.FightController;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class Attack implements Command.WithResult<InteractionHandler.InteractiveAttackData> {
    private final Function<FightController, Actor> actorSelector;
    private final Hittable target;


    public Attack(Hittable target) {
        this.target = target;
        this.actorSelector = fight -> fight.getState().getCurrentActor().get();
    }

    public Attack(Actor actor, Hittable target) {
        this.actorSelector = fight -> actor;
        this.target = target;
    }

    @Override
    public InteractionHandler.InteractiveAttackData getResult(CommandContext context) {
        FightController fight = context.getContext(FightController.class);
        return new AttackData(fight);
    }

    public final class AttackData implements InteractionHandler.InteractiveAttackData {
        private final List<HitAction> hits = new ArrayList<>(3);

        private final Actor actor;
        private final FightController fight;

        private AttackData(FightController fight) {
            this.fight = fight;
            this.actor = actorSelector.apply(fight);
        }

        public Actor getActor() {
            return actor;
        }

        public Hittable getTarget() {
            return target;
        }

        public void addHit(Hittable target, Damage dmg) {
            this.hits.add(new HitAction(actor, target, dmg));
        }

        public void execute() {
            this.hits.forEach(fight::execute);
        }
    }
}
