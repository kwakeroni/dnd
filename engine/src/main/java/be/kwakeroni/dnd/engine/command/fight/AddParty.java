package be.kwakeroni.dnd.engine.command.fight;

import be.kwakeroni.dnd.engine.api.command.Command;
import be.kwakeroni.dnd.engine.api.command.CommandContext;
import be.kwakeroni.dnd.model.creature.Party;
import be.kwakeroni.dnd.engine.api.FightController;

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
