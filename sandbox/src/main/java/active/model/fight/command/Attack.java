package active.model.fight.command;

import active.engine.command.Command;
import active.engine.command.CommandContext;
import active.model.cat.Hittable;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class Attack implements Command {

    Hittable target;

    public Attack(Hittable target) {
        this.target = target;
    }

    @Override
    public void execute(CommandContext context) {
        System.out.println("Attack " + target.getName());
    }
}
