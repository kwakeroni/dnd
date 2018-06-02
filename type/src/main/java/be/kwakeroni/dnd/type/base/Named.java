package be.kwakeroni.dnd.type.base;

public interface Named extends Describable {

    public default String getName(){
        return this.toString();
    }

    public default void describe(Description description){
        description.append(getName());
    }
    
}
