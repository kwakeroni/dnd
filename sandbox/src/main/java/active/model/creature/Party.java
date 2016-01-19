package active.model.creature;

import java.util.stream.Stream;

import active.model.cat.Named;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Party extends Named {
    
    public Stream<Creature> members();
    
}
