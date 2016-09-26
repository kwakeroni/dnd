package active.io.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import active.model.creature.Party;

public class XMLOutput {

    private final OutputStream destination;
    
    public XMLOutput(OutputStream destination){
        this.destination = destination;
    }
    
    public void write(Party party) throws IOException {
        XMLFormat.export(party).accept(this.destination);
        this.destination.flush();
    }

    public static void writeToFile(Party party, String fileName) throws IOException {
        writeToFile(party, new File(fileName));
    }
    public static void writeToFile(Party party, File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)){
            new XMLOutput(fos).write(party);
        }
    }
}
