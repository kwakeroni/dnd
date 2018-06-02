package be.kwakeroni.dnd.io.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import be.kwakeroni.dnd.model.creature.Party;
import be.kwakeroni.dnd.model.fight.Fight;

public class XMLOutput {

    private final OutputStream destination;
    
    public XMLOutput(OutputStream destination){
        this.destination = destination;
    }
    
    public void write(Party party) throws IOException {
        XMLFormat.export(party).accept(this.destination);
        this.destination.flush();
    }

    public void write(Fight fight) throws IOException {
        XMLFormat.exportData(new XMLFormat.DndData(fight)).accept(this.destination);
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
    public static void writeToFile(Fight fight, File file) throws IOException {
        System.out.println("Writing to " + file.getName());
        try (FileOutputStream fos = new FileOutputStream(file)){
            new XMLOutput(fos).write(fight);
        }
    }
}
