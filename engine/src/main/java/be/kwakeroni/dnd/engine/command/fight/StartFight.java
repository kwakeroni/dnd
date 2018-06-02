package be.kwakeroni.dnd.engine.command.fight;

import be.kwakeroni.dnd.engine.api.BattleField;
import be.kwakeroni.dnd.engine.api.FightController;
import be.kwakeroni.dnd.engine.api.command.Command;
import be.kwakeroni.dnd.engine.api.command.CommandContext;

public class StartFight implements Command.WithResult<FightController> {

    public StartFight() {
    }

    @Override
    public FightController getResult(CommandContext context) {
        BattleField battleField = context.getContext(BattleField.class);
        return battleField.startFight();
    }
}
