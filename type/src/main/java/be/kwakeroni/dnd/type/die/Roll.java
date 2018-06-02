package be.kwakeroni.dnd.type.die;

import be.kwakeroni.dnd.type.value.Modifier;
import be.kwakeroni.dnd.type.value.Score;

import static be.kwakeroni.dnd.type.die.Dice.*;

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

    public static <D extends Die> Roll<D> of(D dice, int result){
        if (! dice.isValid(result)){
            throw new IllegalArgumentException(result + " is not a valid roll for " + dice);
        }
        return new Roll<>(result);
    }

    Roll(int result){
        this.result = result;
    }

    public Score modify(Modifier modifier){
        return Score.of(this.result + modifier.toInt());
    }

    public Score toScore() { return Score.of(this.result); }

     private static final void main(){

         Roll<D20> roll1 = Roll.of(D20);
         Roll<D20> roll1b = Roll.D20();
         Roll<_X<D20>> roll2 = Roll.of(Dice.of(2, D20));
         Roll<_2<D20>> roll3 = Roll.of(_2(D20));

     }

    public int asInt(){
        return this.result;
    }
}
