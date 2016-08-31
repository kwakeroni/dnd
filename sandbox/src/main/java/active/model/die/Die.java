package active.model.die;

/**
 * @author Maarten Van Puymbroeck
 */
public abstract class Die {

    Die(){

    }

    abstract int doRoll();

    public abstract int getMaxRoll();

    public boolean isValid(int roll){
        return roll > 0 && roll <= getMaxRoll();
    }

}
