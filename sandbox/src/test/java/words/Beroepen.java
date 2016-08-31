package words;

import java.io.*;
import java.text.Collator;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class Beroepen {

    private Supplier<Stream<String>> voornamen;
    private Supplier<Stream<String>> achternamen;
    private Supplier<Stream<String>> plaatsnamen;
    private BufferedWriter writer;

    public static void main(String[] args) throws IOException {
        File file = new File("beroepen-"+System.currentTimeMillis() + ".txt");
        System.out.println(file.getAbsolutePath());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            new Beroepen(writer).run();
            writer.flush();
        }
        System.out.println(file.getAbsolutePath());
    }

    public Beroepen(BufferedWriter writer) throws IOException{
        this.writer = writer;
        Collection<String> voornamen = words("voornamen.txt");
        Collection<String> achternamen = words("achternamen.txt");
        Collection<String> plaatsnamen = words("gemeenten.txt");

        this.voornamen = () -> voornamen.stream().parallel();
        this.achternamen = () -> achternamen.stream().parallel();
        this.plaatsnamen = () -> plaatsnamen.stream().parallel();
    }

    private Collection<String> words(String file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Beroepen.class.getResourceAsStream("/"+file)))) {
            return reader
                    .lines()
                    .map(Beroepen::clean)
                    .filter(s -> ! s.trim().isEmpty())
                    .collect(collectingAndThen(toList(), Collections::unmodifiableList));
        }

    }


// dropping, instinker

// geschiedkundige, fietsenmaker, procureur des konings, gamedesigner,
// beiaardier, kleuterleidster, belastingambtenaar, koordirigent,
// belastingconsulent, bloemsierkunstenaar, beleidsmedewerker,
// beroepsmilitair, besteksorteerder, criminoloog, decorschilder,
// dierenarts(-assistent), hondentoiletteerder, binnenhuisarchitect,
// huurmoordenaar, animeermeisje, apothekersassistent,
// arbeidsbemiddelaar, arbeidsgeneesheer, toneelmeester, toneelregisseur,
// toneelfigurant, toneelschrijver, toneelspeler,
// balletdanser, sportinstructeur, trambestuurder, treinbestuurder,
// treinbegeleider, uitvaartbegeleider, verloskundige, systeemanalist,
// systeembeheerder, tandartsassistente, luchtverkeersleider,
// technisch tekenaar, verzekeringsagent, scharensliep,
// fabrieksarbeider, schoenmaker, schooldirecteur,
// schoorsteenveger, minister president, museumconservator,
// lichtmatroos,
// loodgieter,landschapsarchitect, meubelstoffeerder,
// restauranthouder
    private void run()throws IOException{
        List<String> beroepen;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Beroepen.class.getResourceAsStream("/beroepen.txt")))) {
            beroepen = reader.lines()
                    .filter(s -> !s.trim().isEmpty())
                    .collect(Collectors.toList());
        }
                    beroepen.parallelStream()
                    .map(Word::new)
                    .flatMap( word ->
                            plaatsnamen.get()
                            .map(word::minus)
                            .filter(Objects::nonNull)
                    ).flatMap( word ->
                            achternamen.get()
                            .map(word::minus)
                            .filter(Objects::nonNull)
                    ).flatMap( word ->
                            voornamen.get()
                            .map(word::minus)
                            .filter(Objects::nonNull)
                    )
                    .filter(Word::isFinal)
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

    private static Collator COLLATOR = Collator.getInstance(new Locale("nl", "BE"));

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


    private static class Word {
        final String original;
        final List<String> result;
        final char[] ordered;

        public Word(String text){
            this.original = text;
            this.ordered = clean(text).toCharArray();
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
