package active.engine.internal.action.type;

import active.model.action.ActionCategory;
import active.model.action.ActionType;

import java.util.*;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class ActionTypeSupport implements ActionType {

    private final String name;
    private final Set<ActionCategory> categories;

    private ActionTypeSupport(String name, Set<? extends ActionCategory> categories, boolean isPrivate){
        this.name = name;
        this.categories = Collections.unmodifiableSet(categories);
    }

    public <E extends Enum<E> & ActionCategory> ActionTypeSupport(String name, E firstCategory, E... rest) {
        this(name, EnumSet.of(firstCategory, rest), true);
    }

    public <E extends Enum<E> & ActionCategory> ActionTypeSupport(String name, EnumSet<E> categories) {
        this(name, EnumSet.copyOf(categories), true);
    }

    public ActionTypeSupport(String name, ActionCategory categories) {
        this(name, Arrays.asList(categories));
    }

    public ActionTypeSupport(String name, Collection<ActionCategory> categories) {
        this(name, new HashSet<>(categories), true);
    }

    @Override
    public Set<ActionCategory> getCategories() {
        return this.categories;
    }

    @Override
    public String getName() {
        return this.name;
    }

}
