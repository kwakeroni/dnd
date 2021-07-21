package be.kwakeroni.dnd.rule.v35.ref;

public @interface PlayersHandbook {
    String chapter();
    String page();
    String table() default "";
    String title();
}
