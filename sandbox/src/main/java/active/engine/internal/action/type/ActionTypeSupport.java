package active.engine.internal.action.type;

import active.model.action.ActionCategory;
import active.model.action.ActionType;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class ActionTypeSupport implements ActionType {

    private String name;
    private ActionCategory category;

    protected ActionTypeSupport(ActionCategory category) {
        this.category = category;
    }

    public ActionTypeSupport(String name, ActionCategory category) {
        this.name = name;
        this.category = category;
    }

    @Override
    public ActionCategory getCategory() {
        return this.category;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String toString(){
        return getCategory().name().toLowerCase() + "["+getName()+"]";
    }

}
