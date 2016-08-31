package active.model.fight.command;

import active.engine.command.Command;
import active.engine.command.CommandContext;
import active.engine.gui.InteractionHandler;
import active.engine.internal.action.HitAction;
import active.model.cat.Actor;
import active.model.cat.Hittable;
import active.model.effect.Damage;
import active.model.fight.FightController;
import active.model.value.Score;

import java.util.ArrayList;
import java.util.List;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class Attack implements Command {
    private final Actor actor;
    private final Hittable target;


    public Attack(Actor actor, Hittable target) {
        this.actor = actor;
        this.target = target;
    }

    @Override
    public void execute(CommandContext context) {
        FightController fight = context.getContext(FightController.class);
        InteractionHandler interaction = context.getContext(InteractionHandler.class);
//        fight.execute(new HitAction(actor, target));
        interaction.requestAttackData(new AttackData(fight, actor, target));
    }

    public static final class AttackData {
        private final Actor actor;
        private final Hittable target;
        private final FightController fight;
        private final List<HitAction> hits = new ArrayList<>(3);

        private AttackData(FightController fight, Actor actor, Hittable target) {
            this.fight = fight;
            this.actor = actor;
            this.target = target;
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
