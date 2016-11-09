package active.engine.gui.swing;

import javax.swing.JMenuBar;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface PluggableMenu {

    void attach(JMenuBar menu);
    void detach();

}
