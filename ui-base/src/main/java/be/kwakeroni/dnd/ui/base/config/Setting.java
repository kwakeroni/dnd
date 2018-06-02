package be.kwakeroni.dnd.ui.base.config;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface Setting<T> {

    String getKey();

    T fromString(String string);

    String toString(T value);

    default T fromNullableString(String string){
        return (string==null)? fromNull() : fromString(string);
    }

    default T fromNull(){
        return null;
    }

}
