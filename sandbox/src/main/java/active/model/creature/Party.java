package active.model.creature;

import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Party {
    
    public Stream<Creature> members();
    
}
