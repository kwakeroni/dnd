package active.model.die;

import active.model.value.Modifier;
import active.model.value.Score;

import static active.model.die.Dice.*;

/**
 * @author Maarten Van Puymbroeck
 */
public final /* value */ class Roll<D extends Die> {

    private final int result;


    public static Roll<D20> D20(){
        return of(D20);
    }

    public static <D extends Die> Roll<D> of(D dice){
        return new Roll<>(dice.doRoll());
    }

    Roll(int result){
        this.result = result;
    }

    public Score modify(Modifier modifier){
        return Score.of(this.result + modifier.toInt());
    }

     private static final void main(){

         Roll<D20> roll1 = Roll.of(D20);
         Roll<D20> roll1b = Roll.D20();
         Roll<_X<D20>> roll2 = Roll.of(Dice.of(2, D20));
         Roll<_2<D20>> roll3 = Roll.of(_2(D20));

     }

}
