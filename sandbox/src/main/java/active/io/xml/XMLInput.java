package active.io.xml;

import active.model.creature.Party;

import java.io.*;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class XMLInput {

    private final InputStream source;

    public XMLInput(InputStream source){
        this.source = source;
    }

    public Party readParty() throws IOException {
        return XMLFormat.importParty().apply(this.source);
    }

    public static Party readParty(String fileName) throws IOException {
        try (FileInputStream fis = new FileInputStream(fileName)){
            return new XMLInput(fis).readParty();
        }
    }
}
