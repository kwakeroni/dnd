package be.kwakeroni.dnd.io;

import java.io.OutputStream;

@FunctionalInterface
public interface Exporter<Element> {

    public void export(Element element, OutputStream destination);
    
}
