package active.engine.gui.swing;

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
