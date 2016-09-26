package active.model.action;

import active.model.cat.Named;

import java.util.Set;
import java.util.function.Predicate;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface ActionType extends Named {

    public ActionCategory getCategory();


    public static Predicate<ActionType> hasCategory(ActionCategory category){
        return type -> category.equals(type.getCategory());
    }
}
