package be.kwakeroni.dnd.model.creature.stats;

import be.kwakeroni.dnd.type.value.Score;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public enum Prop implements Statistic<Score> {
    AC
    ;



    static {
        initialized();
    }

    static Class<Prop> initialized(){
        Statistics.addAll(Prop.class);
        return Prop.class;
    }

    @Override
    public Score fromString(String value) {
        return Score.fromString(value);
    }
}
