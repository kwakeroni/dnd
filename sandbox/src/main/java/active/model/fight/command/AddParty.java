package active.model.fight.command;

import active.engine.command.Command;
import active.engine.command.CommandContext;
import active.model.creature.Party;
import active.model.fight.FightController;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class AddParty implements Command {

    private final Party party;

    public AddParty(Party party) {
        this.party = party;
    }

    @Override
    public void execute(CommandContext context) {
        context.getContext(FightController.class).addParty(party);
    }
}
