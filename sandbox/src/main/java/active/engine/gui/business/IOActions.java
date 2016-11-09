package active.engine.gui.business;

import active.engine.command.CommandHandler;
import active.io.xml.XMLInput;
import active.io.xml.XMLOutput;
import active.model.creature.Party;
import active.model.fight.Fight;
import active.model.fight.FightController;
import active.model.fight.command.AddParty;

import java.io.File;
import java.util.Collection;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface IOActions {

    public static void importParties(File file, CommandHandler handler){
        try {
            Collection<Party> parties = XMLInput.readParties(file);
            for (Party party : parties){
                handler.execute(new AddParty(party));
            }
        } catch (Exception exc){
            exc.printStackTrace();
        }
    }

    public static void exportFight(File file, Fight fight){
        try {
            XMLOutput.writeToFile(fight, file);
        } catch (Exception exc){
            exc.printStackTrace();
        }
    }

}
