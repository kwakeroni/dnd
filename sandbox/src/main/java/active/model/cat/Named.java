package active.model.cat;

public interface Named {

    public default String getName(){
        return this.toString();
    }
    
}
