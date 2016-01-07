package active.model.die;

/**
 * @author Maarten Van Puymbroeck
 */
public /* value */ class D20 extends SingleDie {

    static D20 INSTANCE = new D20();

    private D20(){
        super(20);
    }

}
