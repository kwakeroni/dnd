package active.engine.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public class TableFormat<T> {

    private List<Column<T>> columns;


    private TableFormat(){
        this.columns = new ArrayList<>();
    }

    public static <T> TableFormat<T> with(){
        return new TableFormat<T>();
    }

    public TableFormat<T> column(String title, Function<? super T, ?> value){
        this.columns.add(new Column<>(this.columns.size(), title, value));
        return this;
    }

    public Stream<String> format(Stream<T> coll){
        Pair<Pattern, List<String[]>> data = toFormattableData(coll);

        return Stream.concat(
            Stream.of(data.getA().format(this.columns.stream().map(c -> c.title).toArray(s -> new String[s])),
                      data.getA().getSeparatorLine()),
            data.getB().stream().map(fields -> data.getA().format(fields))
        );
    }

    private Pair<Pattern, List<String[]>> toFormattableData(Stream<T> stream) {
        @SuppressWarnings("unchecked")
        Column<T>[] cols = columns.toArray(new Column[columns.size()]);
        int[] length = columns.stream().mapToInt(c -> c.title.length()).toArray();

        List<String[]> strings = stream
                             .map(t -> IntStream.range(0, cols.length)
                                     .mapToObj(index -> {
                                         // Side effect!
                                         String value = String.valueOf(cols[index].value.apply(t));
                                         if (length[index] < value.length()) {
                                             length[index] = value.length();
                                         }
                                         return value;
                                     }).toArray(s -> new String[s])
                             ).collect(Collectors.toList());

        return new Pair(new Pattern(length), strings);
    }




    private static final class Column<T> {
        final int index;
        final String title;
        final Function<? super T, ?> value;

        public Column(int index, String title, Function<? super T, ?> value) {
            this.index = index;
            this.title = title;
            this.value = value;
        }
    }

    private static final class Pattern {
        final int[] lengths;

        public Pattern(int[] lengths) {
            this.lengths = lengths;
        }

        private String getPattern(){
            return Arrays.stream(lengths)
                         .mapToObj(String::valueOf)
                         .collect(Collectors.joining("s | %", "| %", "s |"));
        }

        private String getSeparatorLine(){
            return Arrays.stream(lengths)
                         .mapToObj(n -> nCopies(n, '-'))
                         .collect(Collectors.joining("-|-", "|-", "-|"));
        }

        private String format(String... args){
            return String.format(getPattern(), args);
        }

        private static String nCopies(int n, char c){
            char[] chars = new char[n];
            Arrays.fill(chars, c);
            return new String(chars);
        }
    }


}
