package active.engine;

import active.engine.internal.action.DefaultHit;
import active.engine.internal.creature.DefaultCreature;
import active.engine.internal.fight.DefaultParticipant;
import active.engine.internal.fight.FightSetup;
import active.engine.util.TableFormat;
import active.model.cat.Actor;
import active.model.cat.Hittable;
import active.model.creature.Creature;
import active.model.fight.Fight;
import active.model.fight.FightController;
import active.model.fight.IsActor;
import active.model.fight.IsTarget;
import active.model.fight.Participant;
import active.model.die.D20;
import active.model.die.Roll;
import active.model.fight.Round;
import active.model.fight.Turn;
import active.model.value.Modifier;
import active.model.value.Score;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static active.model.die.Dice.*;
/**
 * @author Maarten Van Puymbroeck
 */
public class FightEngine {
    public static <TP extends Participant & IsTarget> Stream<TP> getTargets(){
        return null;
    }
    public static void main(String[] args){
        FightSetup setup = new FightSetup();

        Participant p1 = DefaultParticipant.ofCharacter(new DefaultCreature("Fred", Score.of(18), Modifier.of(3)));
        Participant p2 = DefaultParticipant.ofCharacter(new DefaultCreature("George", Score.of(18), Modifier.of(3)));

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
            target(fight).hit(DefaultHit.of(5));
            System.out.println("-- " + fight.getState().getCurrentActor().map(Participant::getName).get() + " hits " + target(fight).getName());
            dump(null, fight);
        }


    }
    
    private static Hittable target(FightController fight){
        return fight.getState().getTargets()
                    .filter(isSameAs(fight.getState().getCurrentActor()).negate())
                    .findAny()
                    .flatMap(Participant::asTarget)
                    .get();
    }

    private static void dump(String header, Stream<? extends Participant> participants){

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
                .column(turn, p->  p.asActor().map(actor -> fight.getCurrentActor().flatMap(Participant::asActor).filter(current -> current == actor).map(to("*")).orElse("")).orElse("-")
                    
                )
            //.column(turn  , p -> (p.isActor()) ? (fight.getCurrentActor().map(current -> (Boolean) (current == p)).orElse(Boolean.FALSE) ? "*" : "") : "-")
            .column("Name", p -> p.getName())
            .column("HP", p -> p.asTarget().map(Hittable::getHP).map(Object::toString).orElse("-"))
            ;

        if (header != null) System.out.println(header);
        System.out.println();

        format.format( Stream.<Participant> concat(fight.getActors(), fight.getTargets().filter(p -> ! p.isActor())) )
              .forEach(System.out::println);
    }

    private static TableFormat<Participant> PARTICIPANT_INFO =
            TableFormat.<Participant> with()
                .column("Name", p -> p.getName())
                .column("INI", p -> p.getInitiative().map(Object::toString).orElse(""))
                .column("INI*", p -> p.asActor().map(Actor::getInitiativeModifier).map(Object::toString).orElse(""));

    private static <T, U> Function<T, U> to(U constant){
        return (T t) -> constant;
    }
    
    private static <T> Predicate<? super T> isSameAs(Optional<? extends T> optional){
        return (T t) -> optional.isPresent() && t == optional.get();
    }
    
    private static <T> Predicate<Optional<? super T>> hasSameAs(Optional<? extends T> optional){
        return (Optional<? super T> opt) -> opt.isPresent() && optional.isPresent() && opt.get() == optional.get(); 
    }
}
