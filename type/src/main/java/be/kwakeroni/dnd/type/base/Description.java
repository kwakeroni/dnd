package be.kwakeroni.dnd.type.base;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Description {

    public Description append(Describable describable);

    public Description append(String string);


}
