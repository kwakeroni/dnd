package be.kwakeroni.dnd.model.creature.stats;

import be.kwakeroni.dnd.type.value.Modifier;

/**
 * @author Maarten Van Puymbroeck
 */
public enum Mod implements Statistic<Modifier> {

    INIT;



    static {
        initialized();
    }

    static Class<Mod> initialized(){
        Statistics.addAll(Mod.class);
        return Mod.class;
    }


    @Override
    public Modifier baseline() {
        return Modifier.ZERO;
    }

    @Override
    public Modifier fromString(String value) {
        return Modifier.fromString(value);
    }
}
