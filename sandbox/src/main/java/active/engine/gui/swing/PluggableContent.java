package active.engine.gui.swing;

import java.awt.Container;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface PluggableContent {

    Container getComponent();

    void activate(GUIController gui);

    void deactivate(GUIController gui);

}
