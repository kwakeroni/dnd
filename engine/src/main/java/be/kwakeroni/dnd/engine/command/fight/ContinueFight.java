package be.kwakeroni.dnd.engine.command.fight;

import be.kwakeroni.dnd.engine.api.BattleField;
import be.kwakeroni.dnd.engine.api.FightController;
import be.kwakeroni.dnd.engine.api.command.Command;
import be.kwakeroni.dnd.engine.api.command.CommandContext;
import be.kwakeroni.dnd.model.fight.Fight;

public class ContinueFight implements Command.WithResult<FightController> {

    private final Fight fight;

    public ContinueFight(Fight fight) {
        this.fight = fight;
    }

    @Override
    public FightController getResult(CommandContext context) {
        BattleField battleField = context.getContext(BattleField.class);
        return battleField.continueFight(this.fight);
    }
}
