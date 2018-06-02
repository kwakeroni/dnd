package be.kwakeroni.dnd.engine;

import be.kwakeroni.dnd.engine.api.command.CommandHandler;
import be.kwakeroni.dnd.engine.api.command.fight.FightCommandFactory;
import be.kwakeroni.dnd.engine.command.CommandHandlerSupport;
import be.kwakeroni.dnd.engine.command.DefaultFightCommandFactory;
import be.kwakeroni.dnd.engine.event.EventBrokerSupport;
import be.kwakeroni.dnd.engine.model.DefaultCreature;
import be.kwakeroni.dnd.engine.model.DefaultParty;
import be.kwakeroni.dnd.engine.model.fight.BattleField;
import be.kwakeroni.dnd.model.creature.Party;
import be.kwakeroni.dnd.model.fight.Participant;
import be.kwakeroni.dnd.type.die.Roll;
import be.kwakeroni.dnd.type.value.Modifier;
import be.kwakeroni.dnd.type.value.Score;
import be.kwakeroni.dnd.ui.swing.EngineWindow;

/**
 * @author Maarten Van Puymbroeck
 */
public class FightEngine {

    public static void main(String[] args) throws Exception {
//        Collection<Party> parties = XMLInput.readParties("C:\\Projects\\workspace-java8\\dnd\\sandbox\\src\\test\\resources\\simple.xml");
//        System.out.println(parties);
//
//        System.exit(0);

        System.setProperty("sun.java2d.d3d", "false");
        System.setProperty("java.awt.Window.locationByPlatform", "true");

        EventBrokerSupport<?> broker = EventBrokerSupport.newInstance();

        BattleField battleField = setup(broker);


        CommandHandler handler = CommandHandlerSupport.root();
        handler.registerContext(be.kwakeroni.dnd.engine.api.BattleField.class, battleField);


        FightCommandFactory fightCommand = new DefaultFightCommandFactory();

        EngineWindow ui = new EngineWindow(handler, broker, fightCommand);
        ui.show();

//        Participant p = DefaultParticipant.ofCreature(new DefaultCreature("Billy", Score.of(25), Score.of(27), Modifier.of(4)));
//        p.setInitiative(Roll.D20());
//        fight.addParticipant(p);


//        File targetDir = new File(FightEngine.class.getResource("/classdir").toURI()).getAbsoluteFile().getParentFile().getParentFile();
//System.out.println("Outputting to " +  new File(targetDir, "myparty.xml").getAbsolutePath());
//        XMLOutput.writeToFile(getMyParty(), new File(targetDir, "myparty.xml").getAbsolutePath());
//        XMLOutput.writeToFile(getOtherParty(), new File(targetDir, "otherparty.xml").getAbsolutePath());

    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static BattleField setup(EventBrokerSupport<?> broker) {
        BattleField setup = new BattleField(broker);

//        setup.add(getMyParty());
//        setup.add(getOtherParty());

        setup.participants()
                .filter(Participant::isActor)
                .forEach(p -> p.setInitiative(Roll.D20()));

        return setup;
    }

    private static Party getMyParty() {

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
