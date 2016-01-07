package active.engine;

import active.engine.internal.fight.DefaultParticipant;
import active.engine.internal.fight.FightSetup;
import active.engine.util.TableFormat;
import active.model.creature.Creature;
import active.model.fight.Fight;
import active.model.fight.Participant;
import active.model.die.Roll;

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

        Fight fight = setup.create();



        dump("Actors", fight.getActors());
        dump("Targets", fight.getTargets());


    }

    private static void dump(String header, Stream<Participant> participants){

        System.out.println();
        System.out.println(header);

        TableFormat<Participant> format =
            TableFormat.<Participant> withColumn("Name", p -> p.getName())
                       .column("INI", p -> p.getInitiative().map(Object::toString).orElse(""))
                       .column("INI*", p -> p.asActor().getInitiativeModifier());

        format.format(participants)
              .forEach(System.out::println);
    }



}
