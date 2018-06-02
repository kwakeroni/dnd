package be.kwakeroni.dnd.ui.swing.gui;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface Snapshot {

    void restore();

    default Snapshot and(Snapshot other){
        return () -> {
            this.restore();
            other.restore();
        };
    }

}
