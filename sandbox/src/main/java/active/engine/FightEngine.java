package active.engine;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.*;

import active.engine.command.CommandHandler;
import active.engine.command.CommandHandlerSupport;
import active.engine.event.EventBrokerSupport;
import active.engine.gui.swing.EngineWindow;
import active.engine.internal.action.HitAction;
import active.engine.internal.cat.DecoratedDescription;
import active.engine.internal.creature.DefaultCreature;
import active.engine.internal.creature.DefaultParty;
import active.engine.internal.fight.BattleField;
import active.engine.internal.fight.DefaultParticipant;
import active.engine.util.TableFormat;
import active.io.xml.XMLInput;
import active.io.xml.XMLOutput;
import active.model.action.Action;
import active.model.cat.Hittable;
import active.model.creature.Party;
import active.model.die.Roll;
import active.model.fight.Fight;
import active.model.fight.FightController;
import active.model.fight.Participant;
import active.model.value.Modifier;
import active.model.value.Score;
/**
 * @author Maarten Van Puymbroeck
 */
public class FightEngine {

    public static void main(String[] args)  throws Exception{

//        Collection<Party> parties = XMLInput.readParties("C:\\Projects\\workspace-java8\\dnd\\sandbox\\src\\test\\resources\\simple.xml");
//        System.out.println(parties);
//
//        System.exit(0);

        System.setProperty("sun.java2d.d3d", "false");
        
        EventBrokerSupport<?> broker = EventBrokerSupport.newInstance();

        BattleField battleField = setup(broker);



        CommandHandlerSupport handler = new CommandHandlerSupport();



        EngineWindow ui = new EngineWindow(battleField, handler, broker);
        ui.show();

//        Participant p = DefaultParticipant.ofCreature(new DefaultCreature("Billy", Score.of(25), Score.of(27), Modifier.of(4)));
//        p.setInitiative(Roll.D20());
//        fight.addParticipant(p);



//        File targetDir = new File(FightEngine.class.getResource("/classdir").toURI()).getAbsoluteFile().getParentFile().getParentFile();
//System.out.println("Outputting to " +  new File(targetDir, "myparty.xml").getAbsolutePath());
//        XMLOutput.writeToFile(getMyParty(), new File(targetDir, "myparty.xml").getAbsolutePath());
//        XMLOutput.writeToFile(getOtherParty(), new File(targetDir, "otherparty.xml").getAbsolutePath());

    }
    
    private static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static BattleField setup(EventBrokerSupport<?> broker){
        BattleField setup = new BattleField(broker);

//        setup.add(getMyParty());
//        setup.add(getOtherParty());

        setup.participants()
             .filter(Participant::isActor)
             .forEach(p -> p.setInitiative(Roll.D20()));

        return setup;
    }
    
    private static Party getMyParty(){

//        try {
//            return XMLInput.readParties("C:\\Projects\\workspace-java8\\dnd\\myparty.xml").iterator().next();
//        } catch (IOException e) {
//            throw new UncheckedIOException(e);
//        }

        DefaultParty party = new DefaultParty("ours");
        party.add(new DefaultCreature("Fred", Score.of(18), Score.of(21), Modifier.of(3)));
        party.add(new DefaultCreature("George", Score.of(18), Score.of(21), Modifier.of(3)));
        return party;
    }
    
//    private static Party getOtherParty(){
//        DefaultParty party = new DefaultParty("the others");
//        party.add(new DefaultCreature("Lucius", Score.of(22), Score.of(19), Modifier.of(2)));
//        party.add(new DefaultCreature("Astoria", Score.of(16), Score.of(25), Modifier.of(4)));
//        return party;
//    }
//


}
