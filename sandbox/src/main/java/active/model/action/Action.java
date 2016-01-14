package active.model.action;

import active.model.cat.Describable;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Action<Context> extends Describable {

    void execute(Context context);

}
