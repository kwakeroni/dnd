package be.kwakeroni.dnd.type.die;

import java.util.Random;

/**
 * @author Maarten Van Puymbroeck
 */
public /* value */ class SingleDie extends Die {

    private final int sides;
    private final Random random;

    SingleDie(int sides){
        this.sides = sides;
        this.random = new Random();
    }

    @Override
    int doRoll() {
        return random.nextInt(sides) + 1;
    }

    @Override
    public int getMaxRoll() {
        return sides;
    }

    @Override
    public String toString() {
        return "D" + sides;
    }

}
