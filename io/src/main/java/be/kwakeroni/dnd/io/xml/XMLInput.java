package be.kwakeroni.dnd.io.xml;

import be.kwakeroni.dnd.io.model.MutableFight;
import be.kwakeroni.dnd.model.creature.Party;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class XMLInput {

    private final InputStream source;

    public XMLInput(InputStream source) {
        this.source = source;
    }

    public Collection<Party> readParties() throws IOException {
        return XMLFormat.importParties().apply(this.source);
    }

    public MutableFight readFight() throws IOException {
        return XMLFormat.importFight().apply(this.source);
    }

    public static Collection<Party> readParties(String fileName) throws IOException {
        return readParties(new File(fileName));
    }

    public static Collection<Party> readParties(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return new XMLInput(fis).readParties();
        }
    }

    public static Collection<Party> readParties(URL url) throws IOException {
        try (InputStream input = url.openStream()) {
            return new XMLInput(input).readParties();
        }
    }

    public static MutableFight readFight(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return new XMLInput(fis).readFight();
        }
    }

}
