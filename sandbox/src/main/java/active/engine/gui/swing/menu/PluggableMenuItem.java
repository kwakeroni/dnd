package active.engine.gui.swing.menu;

import javax.swing.JMenu;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface PluggableMenuItem {

    void attach(JMenu menu);
    void detach();

}
