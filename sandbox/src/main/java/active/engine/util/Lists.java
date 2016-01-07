package active.engine.util;

import java.util.List;
import java.util.Optional;

/**
 * @author Maarten Van Puymbroeck
 */
public class Lists {

    private Lists(){

    }

    public static Optional<Integer> indexOf(List<?> list, Object object){
        int index = list.indexOf(object);
        return (index < 0)? Optional.empty() : Optional.of(index);
    }

}
