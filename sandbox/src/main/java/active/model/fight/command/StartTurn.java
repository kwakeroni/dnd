package active.model.fight.command;

import active.engine.command.Command;
import active.engine.command.CommandContext;
import active.model.fight.FightController;

public class StartTurn implements Command {

    @Override
    public void execute(CommandContext context) {
        
        context.getContext(FightController.class).startTurn();

    }

}