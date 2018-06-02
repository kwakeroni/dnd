package be.kwakeroni.dnd.model.creature;

import java.util.stream.Stream;

import be.kwakeroni.dnd.type.base.Named;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Party extends Named {
    
    public Stream<Creature> members();

    
}
