package active.engine;

import active.engine.internal.fight.DefaultParticipant;
import active.engine.internal.fight.FightSetup;
import active.engine.util.TableFormat;
import active.model.creature.Creature;
import active.model.fight.Fight;
import active.model.fight.FightController;
import active.model.fight.Participant;
import active.model.die.Roll;
import active.model.fight.Round;
import active.model.fight.Turn;

import java.util.stream.Stream;

import static active.model.die.Dice.*;
/**
 * @author Maarten Van Puymbroeck
 */
public class FightEngine {

    public static void main(String[] args){

        FightSetup setup = new FightSetup();

        Participant p1 = DefaultParticipant.ofCharacter("Fred", new Creature(){});
        Participant p2 = DefaultParticipant.ofCharacter("George", new Creature(){});

        setup.add(p1);
        setup.add(p2);


        setup.participants()
             .filter(Participant::isActor)
             .forEach(p -> p.setInitiative(Roll.D20()));

        FightController fight = setup.start();

        Round round;
        Turn turn;

        dump("Actors", fight.getState().getActors());
        dump("Targets", fight.getState().getTargets());

        dump("Start", fight);

        for (int i=0; i<4; i++){
        fight.nextTurn();
        dump(null, fight);
        }


    }

    private static void dump(String header, Stream<Participant> participants){

        System.out.println();
        System.out.println(header);

        PARTICIPANT_INFO.format(participants)
                        .forEach(System.out::println);
    }

    private static void dump(String header, FightController fight){
        dump(header, fight.getState());
    }
    private static void dump(String header, Fight fight){
        String turn = "Turn " + fight.getCurrentTurnNumber();
        TableFormat<Participant> format =  TableFormat.<Participant> with()
            .column(turn  , p -> (p.isActor()) ? (fight.getCurrentActor().map(current -> (Boolean) (current == p)).orElse(Boolean.FALSE) ? "*" : "") : "-")
            .column("Name", p -> p.getName())
            ;

        if (header != null) System.out.println(header);
        System.out.println();

        format.format( Stream.concat(fight.getActors(), fight.getTargets().filter(p -> ! p.isActor())) )
              .forEach(System.out::println);
    }

    private static TableFormat<Participant> PARTICIPANT_INFO =
            TableFormat.<Participant> with()
                .column("Name", p -> p.getName())
                .column("INI", p -> p.getInitiative().map(Object::toString).orElse(""))
                .column("INI*", p -> p.asActor().getInitiativeModifier());

}
