package active.model.creature.stats;

import active.model.value.Modifier;

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
    public Modifier fromString(String value) {
        return Modifier.fromString(value);
    }
}
