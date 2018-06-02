package be.kwakeroni.dnd.model.creature.stats;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Statistic<S> {

    S fromString(String value);

}
