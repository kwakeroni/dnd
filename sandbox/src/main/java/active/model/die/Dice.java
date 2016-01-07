package active.model.die;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public class Dice {

    private Dice(){

    }

    public static D20 D20 = active.model.die.D20.INSTANCE;

    public static <D extends Die> _2<D> _2(D die){
        return _2.instance(die);
    }

    public static <D extends Die> _X<D> of(int nb, D die){
        switch (nb){
            case 2: return _2.instance(die);
            default: return new _X<D>(nb, die);
        }
    }

    public static CombinedDie combine(Die... dice){
        return new CombinedDie(){
            @Override
            Stream<Die> dice() {
                return Arrays.stream(dice);
            }
        };
    }

}
