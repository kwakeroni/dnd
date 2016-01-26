package active.engine;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import active.engine.gui.swing.EngineWindow;
import active.engine.internal.action.HitAction;
import active.engine.internal.cat.DecoratedDescription;
import active.engine.internal.creature.DefaultCreature;
import active.engine.internal.creature.DefaultParty;
import active.engine.internal.fight.BattleField;
import active.engine.internal.fight.DefaultParticipant;
import active.engine.util.TableFormat;
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

        System.setProperty("sun.java2d.d3d", "false");
        
        DecoratedDescription.Builder log = log();


        BattleField battleField = setup();


        FightController fight = battleField.startFight();

        new EngineWindow(fight.getData()).show();


        Participant p = DefaultParticipant.ofCreature(new DefaultCreature("Billy", Score.of(25), Modifier.of(4)));
        p.setInitiative(Roll.D20());
        fight.addParticipant(p);


        fight.on().turnEnded()

                  .peek(turn -> dump(null, turn.getFight()))
                  //.peek(turn -> JOptionPane.showConfirmDialog(null, "Wait"))
                  .peek(t -> sleep(2000))
                  ;

        fight.on().action()
                  .forEach(action -> {
                      System.out.println(log.newDescription().append(action.getAction()).toString());
                  });


        dump("Actors", fight.getState().getActors());
        dump("Targets", fight.getState().getTargets());
        dump("Start", fight);

        for (int i=0; i<4; i++){
            fight.nextTurn();

            Action<? super Fight> action = new HitAction(fight.getState().getCurrentActor().get(), target(fight));

            fight.execute(action);
        }

        fight.endTurn();
        fight.endRound();

//        XMLOutput.writeToFile(getMyParty(), "./target/myparty.xml");
        
    }
    
    private static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static BattleField setup(){
        BattleField setup = new BattleField();

        setup.add(getMyParty());
        setup.add(getOtherParty());

        setup.participants()
             .filter(Participant::isActor)
             .forEach(p -> p.setInitiative(Roll.D20()));

        return setup;
    }
    
    private static Party getMyParty(){
        DefaultParty party = new DefaultParty("ours");
        party.add(new DefaultCreature("Fred", Score.of(18), Modifier.of(3)));
        party.add(new DefaultCreature("George", Score.of(18), Modifier.of(3)));
        return party;
    }
    
    private static Party getOtherParty(){
        DefaultParty party = new DefaultParty("the others");
        party.add(new DefaultCreature("Lucius", Score.of(22), Modifier.of(2)));
        party.add(new DefaultCreature("Bellatrix", Score.of(16), Modifier.of(4)));
        return party;
    }
    

    private static DecoratedDescription.Builder log(){
        return DecoratedDescription.builder()
                .intercept(Action.class)
                    .thenPrefixWith("-- ")
                .intercept(Participant.class)
                    .thenWrapBetween(blue(), black())
                .intercept(Score.class)
                    .thenWrapBetween(red(), black())
                    ;
    }
    
    private static Hittable target(FightController fight){
        return fight.getState().getTargets()
                    .filter(isSameAs(fight.getState().getCurrentActor()).negate())
                    .findAny()
                    .get();
    }



    private static void dump(String header, Stream<? extends Participant> participants){

        System.out.println();
        System.out.println(header);

        PARTICIPANT_INFO.format(participants)
                        .forEach(System.out::println);
    }

    private static void dump(Action<?> action){
        if (action instanceof HitAction){
            HitAction hit = (HitAction) action;
            System.out.println("-- " + hit.getActor().getName() + " hits " + hit.getTarget().getName());
        }

    }

    private static void dump(String header, FightController fight){
        dump(header, fight.getState());
    }
    private static void dump(String header, Fight fight){
        String turn = "Turn " + fight.getCurrentTurnNumber();
        TableFormat<Participant> format =  TableFormat.<Participant> with()
                .column(turn, p->  p.asActor().map(actor -> fight.getCurrentActor().filter(current -> current == actor).map(to("*")).orElse("")).orElse("-")
                    
                )
            //.column(turn  , p -> (p.isActor()) ? (fight.getCurrentActor().map(current -> (Boolean) (current == p)).orElse(Boolean.FALSE) ? "*" : "") : "-")
            .column("Name", p -> p.getName())
            .column("HP", p -> p.asTarget().map(h -> h.getHP()).map(Object::toString).orElse("-"))
            ;

        if (header != null) System.out.println(header);
        System.out.println();

        format.format( Stream.<Participant> concat(fight.getActors(), fight.getTargets().filter(p -> ! p.isActor())) )
              .forEach(System.out::println);

        System.out.println();
    }

    private static TableFormat<Participant> PARTICIPANT_INFO =
            TableFormat.<Participant> with()
                .column("Name", p -> p.getName())
                .column("INI", p -> p.getInitiative().map(Object::toString).orElse(""))
                .column("INI*", p -> p.asActor().map(a -> a.getInitiativeModifier()).map(Object::toString).orElse(""));

    private static <T, U> Function<T, U> to(U constant){
        return (T t) -> constant;
    }
    
    private static <T> Predicate<? super T> isSameAs(Optional<? extends T> optional){
        return (T t) -> optional.isPresent() && t == optional.get();
    }
    
    private static <T> Predicate<Optional<? super T>> hasSameAs(Optional<? extends T> optional){
        return (Optional<? super T> opt) -> opt.isPresent() && optional.isPresent() && opt.get() == optional.get(); 
    }

        private static final char ANSI_ESC = 27;
    private static final String ansi(String control){
        return ANSI_ESC + "[" + control;
    }
    private static final String black(){
        return ansi("30m");
    }
    private static final String red(){
        return ansi("31m");
    }

    private static final String blue(){
        return ansi("34m");
    }

    private static final String grey(){
        return ansi("37m");
    }
    private static final String grey(String str){
        return grey() + str + black();
    }
}
