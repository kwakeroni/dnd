package active.io.xml;

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
        try (FileOutputStream fos = new FileOutputStream(fileName)){
            new XMLOutput(fos).write(party);
        }
    }
}
