package be.kwakeroni.dnd.engine.command.fight;

import be.kwakeroni.dnd.engine.api.command.Command;
import be.kwakeroni.dnd.engine.api.command.CommandContext;
import be.kwakeroni.dnd.engine.api.FightController;

public class StartTurn implements Command {

    @Override
    public void execute(CommandContext context) {
        context.getContext(FightController.class).startTurn();
    }

}
