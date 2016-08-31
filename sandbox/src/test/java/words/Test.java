package words;

import java.io.*;
import java.nio.CharBuffer;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class Test {

    private static final Set<String> REMOVE = new HashSet<>(Arrays.asList(
            "ank", "chen", "chan", "enk", "heck", "nen", "stob", "biko", "ret", "boucl", "nrc", "han",
            "obia", "ate", "chant", "caubo", "tea", "ane", "the", "kuo", "balci", "can", "baco", "enna",
            "cranen", "sukel", "aceh", "goj", "voj", "vei", "fok", "deuce", "timan", "bbc", "font", "rcan",
            "reen", "acre", "crank", "arcen", "krach", "hacer", "eke", "eek", "cda", "cbb", "kca", "then", "nonet",
            "tno", "teo", "eken", "hoet", "kno", "eno", "diere", "cok", "hock", "chocke", "echon", "kean",
            "haeck", "keen", "eken", "coert", "corte", "coret", "fhn", "cor", "taeke", "ocr", "tan", "recta", "coen",
            "ron", "tno", "anco", "kane", "kate", "kee", "tarek", "corn", "tra", "acton", "eker", "toke",
            "cuijk", "eve", "eelco", "jeu", "evi", "kanne", "elco", "che", "evie", "veie", "jelke", "jekel",
            "clio", "cleo", "clo", "loc", "oele", "ceel", "iov", "onne", "cohen", "cont", "veli", "levi",
            "joke", "jouke", "conen", "lic", "ceel", "tonk", "notk", "franc", "franco", "cote", "eijck", "lou",
            "och", "tack", "kevie", "kiev"

    ));

    private Supplier<Stream<String>> wordStream;
    private BufferedWriter writer;

    public static void main(String[] args) throws IOException{
        File file = new File("output-"+System.currentTimeMillis() + ".txt");
        System.out.println(file.getAbsolutePath());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            new Test(writer).run();
            writer.flush();
        }
        System.out.println(file.getAbsolutePath());
    }

    public Test(BufferedWriter writer){
        this.writer = writer;
        List<String> list =
                new BufferedReader(new InputStreamReader(Test.class.getResourceAsStream("/dutch")))
                .lines()
                .map(Test::clean)
                .filter(s -> s.length() > 2)
                .filter(s -> ! REMOVE.contains(s))
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
                ;
    this.wordStream = () -> list.stream().parallel();
    }

    private void run(){

        List<String> namen = Arrays.asList(
                "Maarten Van Puymbroeck",
                "Robert DeNiro",
                "Bart De Wever",
                "Louis Tobback",
                "Sven Gatz",
                "Joke Schauvliege",
                "Theo Francken",
                "Benedict Cumberbatch"
        );

        Words words = new Words(namen.stream().map(Word::new).collect(Collectors.toList()));

            restStream(words)
                    .flatMap(Words::stream)
                    .distinct()
                    .forEach(this::output);
    }


    private void output(Word word){
        System.out.println(word);
        synchronized (writer) {
            try {
                writer.write(String.valueOf(word));
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Stream<Words> restStream(Words words){
        if (words.isFinal()){
            return Stream.of(words);
        } else {
            return wordStream()
                    .skip(0)
                    .map(words::minus)
                    .filter(w -> ! w.isEmpty())
                    .flatMap(this::restStream);
        }
    }

    private Stream<Word> restStream(Word word){
        if (word.isFinal()){
            return Stream.of(word);
        } else {
            return
                    wordStream()
                            .map(word::minus)
                            .filter(Objects::nonNull)
                            .flatMap(this::restStream);
        }
    }

    private Stream<String> wordStream(){
        return this.wordStream.get();
    }

    private static String clean(String s){
        s = s.toLowerCase();
        StringBuilder builder = new StringBuilder(s.length());
        char c=0;
        for (int i=0; i<s.length(); i++){
            c = s.charAt(i);
            if (c >= 'a' && c <= 'z'){
                builder.append(c);
            }
        }
        return builder.toString();
    }

    private static class Words implements Iterable<Word> {
        private List<Word> words;

        public Words(List<Word> words){
            this.words = words;
        }

        @Override
        public Iterator<Word> iterator() {
            return this.words.iterator();
        }

        public boolean isEmpty(){
            return this.words.isEmpty();
        }

        public Words minus(String other){
            List<Word> result = new ArrayList<>(words.size());
            for (Word word : words){
                if (word.isFinal()){
                    result.add(word);
                } else {
                    Word newWord = word.minus(other);
                    if (newWord != null) {
                        result.add(newWord);
                    }
                }
            }
            return new Words(result);
        }

        public boolean isFinal(){
            for (Word word : words){
                if (! word.isFinal()){
                    return false;
                }
            }
             return true;
        }

        public Stream<Word> stream(){
            return this.words.stream();
        }
    }

    private static class Word {
        final String original;
        final List<String> result;
        final char[] ordered;

        public Word(String text){
            this.original = clean(text);
            this.ordered = this.original.toCharArray();
            Arrays.sort(this.ordered);
            this.result = Collections.emptyList();
        }

        private Word(String original, char[] ordered, List<String> result){
            this.original = original;
            this.ordered = ordered;
            this.result = result;
        }

        public boolean isFinal(){
            return this.ordered.length == 0;
        }

        public Word minus(String other){

            if (other.length() > this.ordered.length) return null;
            if (this.result.size() >= 4) return null;
            if (result.contains(other)) return null;

            char[] chars = Arrays.copyOf(this.ordered, this.ordered.length);
            for (int i=0; i<other.length(); i++){
                int index = Arrays.binarySearch(chars, other.charAt(i));
                if (index < 0){
                    return null;
                } else {
                    chars[index] = 0;
                    Arrays.sort(chars);
                }
            }
            int index;
            for (index=0; index<chars.length && chars[index]==0; index++);
            chars = Arrays.copyOfRange(chars, index, chars.length);

            List<String> result = new ArrayList<>(this.result.size()+1);
            result.addAll(this.result);
            result.add(other);
            return new Word(this.original, chars, result);
        }

        public String toString(){
            return original + " = " + result + " + " + String.valueOf(this.ordered);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Word word = (Word) o;

            if (!result.equals(word.result)) return false;
            return Arrays.equals(ordered, word.ordered);

        }

        @Override
        public int hashCode() {
            int result1 = result.hashCode();
            result1 = 31 * result1 + Arrays.hashCode(ordered);
            return result1;
        }
    }

}
