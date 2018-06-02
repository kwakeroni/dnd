package be.kwakeroni.dnd.ui.swing.console;

import be.kwakeroni.dnd.type.description.DecoratedDescription;
import be.kwakeroni.dnd.ui.base.fight.FightLogger;
import be.kwakeroni.dnd.model.target.Hittable;
import be.kwakeroni.dnd.model.fight.Fight;
import be.kwakeroni.dnd.engine.api.FightController;
import be.kwakeroni.dnd.model.fight.Participant;
import be.kwakeroni.dnd.type.value.Score;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class ConsoleFightLogger implements FightLogger {


    public static final ConsoleFightLogger INSTANCE = new ConsoleFightLogger();

    private ConsoleFightLogger() {

    }

    public void dump(String header, Stream<? extends Participant> participants) {

        System.out.println();
        System.out.println(header);

        PARTICIPANT_INFO.format(participants)
                .forEach(System.out::println);
    }
//
//    public void dump(be.kwakeroni.dnd.model.actor.Action<?> action) {
//        if (action instanceof HitAction) {
//            HitAction hit = (HitAction) action;
//            System.out.println("-- " + hit.getActor().getName() + " hits " + hit.getTarget().getName());
//        }
//
//    }

    public void dump(String header, FightController fight) {
        dump(header, fight.getState());
    }

    public void dump(String header, Fight fight) {
        String turn = "Turn " + fight.getCurrentTurnNumber();
        TableFormat<Participant> format = TableFormat.<Participant>with()
                .column(turn, p -> p.asActor().map(actor -> fight.getCurrentActor().filter(current -> current == actor).map(to("*")).orElse("")).orElse("-")

                )
                //.column(turn  , p -> (p.isActor()) ? (fight.getCurrentActor().map(current -> (Boolean) (current == p)).orElse(Boolean.FALSE) ? "*" : "") : "-")
                .column("Name", p -> p.getName())
                .column("HP", p -> p.asTarget().map(h -> h.getHP()).map(Object::toString).orElse("-"));

        if (header != null) System.out.println(header);
        System.out.println();

        format.format(Stream.<Participant>concat(fight.getActors(), fight.getTargets().filter(p -> !p.isActor())))
                .forEach(System.out::println);

        System.out.println();
    }

    private static TableFormat<Participant> PARTICIPANT_INFO =
            TableFormat.<Participant>with()
                    .column("Name", p -> p.getName())
                    .column("INI", p -> p.getInitiative().map(Object::toString).orElse(""))
                    .column("INI*", p -> p.asActor().map(a -> a.getInitiativeModifier()).map(Object::toString).orElse(""));

    public DecoratedDescription.Builder log() {
        return DecoratedDescription.builder()
                .intercept(be.kwakeroni.dnd.model.actor.Action.class)
                .thenPrefixWith("-- ")
                .intercept(Participant.class)
                .thenWrapBetween(blue(), black())
                .intercept(Score.class)
                .thenWrapBetween(red(), black())
                ;
    }

    private static Hittable target(FightController fight) {
        return fight.getState().getTargets()
                .filter(isSameAs(fight.getState().getCurrentActor()).negate())
                .findAny()
                .get();
    }


    private static <T, U> Function<T, U> to(U constant) {
        return (T t) -> constant;
    }

    private static <T> Predicate<? super T> isSameAs(Optional<? extends T> optional) {
        return (T t) -> optional.isPresent() && t == optional.get();
    }

    private static <T> Predicate<Optional<? super T>> hasSameAs(Optional<? extends T> optional) {
        return (Optional<? super T> opt) -> opt.isPresent() && optional.isPresent() && opt.get() == optional.get();
    }

    private static final char ANSI_ESC = 27;

    private static final String ansi(String control) {
        return ANSI_ESC + "[" + control;
    }

    private static final String black() {
        return ansi("30m");
    }

    private static final String red() {
        return ansi("31m");
    }

    private static final String blue() {
        return ansi("34m");
    }

    private static final String grey() {
        return ansi("37m");
    }

    private static final String grey(String str) {
        return grey() + str + black();
    }

}
