package words;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class Sort {

    public static void main(String[] args) throws Exception {
        File in = new File("C:\\Projects\\intellij-workspace\\beroepen-compleet.txt");
        File out = new File(in.getParentFile(), "beroepen-compleet-sorted.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(in))){
            Files.write(out.toPath(), (Iterable<String>) ()-> reader.lines()
                    .sorted()
                    .iterator()
            );
        }
    }

}
