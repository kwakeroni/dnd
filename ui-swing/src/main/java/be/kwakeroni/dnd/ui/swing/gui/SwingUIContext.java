package be.kwakeroni.dnd.ui.swing.gui;

import java.awt.*;

public class SwingUIContext {

    private final Component activeComponent;

    public SwingUIContext(Component activeComponent) {
        this.activeComponent = activeComponent;
    }

    public Component getActiveComponent() {
        return this.activeComponent;
    }

}
