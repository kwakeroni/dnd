package active.model.die;

/**
 * @author Maarten Van Puymbroeck
 */
public class _2<D extends Die> extends _X<D> {

    private _2(D d) {
        super(2, d);
    }

    static <D extends Die> _2<D> instance(D d){
        return new _2<D>(d);
    }

}
