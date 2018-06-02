package be.kwakeroni.dnd.model.actor;

import be.kwakeroni.dnd.type.base.Describable;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Action<Context> extends Describable {

    public void execute(Context context);

}
