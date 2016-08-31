package words;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class Match {

    private static String vzs = "(op|onder|in|om|boven|beneden|voor|na|naa)";
    private static String car = "(\\w|en|is|min|plus|maal)";
    private static String kl = "(\\w|en|is|min|plus|maal|do|re|mi|fa|sol|la|si|aa|bb|be|bee|ce|cee|dd|de|dee|ff|ef|gg|ge|gee|hh|ha|haa|ie|jj|je|jee|kk|ka|kaa|ll|el|mm|em|nn|en|oo|pp|pe|pee|qu|quu|ku|kuu|rr|er|ss|es|tt|te|tee|uu|vv|ve|vee|ww|we|wee|xx|ix|zz|zet)";
    private static String ls = car + "{1,2}";
//    private static String word = car + "("+car+"?"+vzs+car +")+" + car + "?";
    private static String word = kl + "(" + vzs + kl + ")+";
    private static Pattern pattern = Pattern.compile(word);

    public static void main(String[] args)throws Exception{
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Match.class.getResourceAsStream("/dutch")))){
            reader.lines()
                    .map(String::toLowerCase)
//                    .filter(s -> ! s.startsWith("onder"))
//                    .filter(s -> ! s.startsWith("in"))
//                    .filter(s -> ! s.endsWith("onder"))
//                    .filter(s -> ! s.endsWith("in"))
//                    .filter(s -> s.contains("onder"))
//                    .filter(s -> s.contains("in"))
                    .filter(s -> s.length() >= 6)
                    .filter(s -> pattern.matcher(s).matches())
                    .forEach(System.out::println);
        }

        // onder ne m in g

        // onder hand-el in g

        // w onder k in d

        // voor b-in-d p+=

        // t in w in n in g

        // so -na- te

        // qu in oa
        // on om at op ee

        // lo onder v in g

        // k op l op r

        // c om b in e

        // p op er in ge naa r
    }

}
