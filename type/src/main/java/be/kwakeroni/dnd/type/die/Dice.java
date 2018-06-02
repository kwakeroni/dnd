package be.kwakeroni.dnd.type.die;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public class Dice {

    private Dice(){

    }

    public static final D20 D20 = be.kwakeroni.dnd.type.die.D20.INSTANCE;
    public static Die D4 = new SingleDie(4);
    public static Die D6 = new SingleDie(6);
    public static Die D8 = new SingleDie(8);

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

    public static Die fromString(String die){
        String[] dice = die.split("\\+");
        if (dice.length == 1){
            return singleFromString(dice[0]);
        } else {
            return combinedFromStrings(dice);
        }
    }

    private static Pattern SINGLE_DIE_PATTERN = Pattern.compile("(\\d*)D(\\d+)");

    private static Die singleFromString(String die){
        Matcher matcher = SINGLE_DIE_PATTERN.matcher(die);
        if (matcher.matches()){
            Die singleDie = new SingleDie(Integer.parseInt(matcher.group(2)));
            if (matcher.group(1).isEmpty()){
                return singleDie;
            } else {
                return Dice.of(Integer.parseInt(matcher.group(1)), singleDie);
            }
        } else {
            throw new IllegalArgumentException("Could not parse as die: " + die);
        }
    }

    private static Die combinedFromStrings(String... dice){
        return combine(Arrays.stream(dice).map(Dice::singleFromString).toArray(Die[]::new));
    }
}
