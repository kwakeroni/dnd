package active.engine.internal.cat;

import active.model.cat.Describable;
import active.model.cat.Description;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public class DefaultDescription implements Description {

    List<String> parts = new ArrayList<>();

    @Override
    public Description append(Describable describable) {
        describable.describe(this);
        return this;
    }

    @Override
    public Description append(final String string) {
        parts.add(string);
        return this;
    }

    public Stream<String> stream() {
        return this.parts.stream();
    }

    public String toString(){
        return stream().collect(Collectors.joining());
    }
}
