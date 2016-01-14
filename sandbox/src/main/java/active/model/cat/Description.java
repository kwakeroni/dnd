package active.model.cat;

import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Description {

    public Description append(Describable describable);

    public Description append(String string);


}
