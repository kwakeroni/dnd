package active.engine.gui.swing;

import active.engine.command.CommandHandler;
import active.engine.internal.fight.BattleField;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import java.awt.Component;
import java.awt.Container;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface GUIController {

    JFrame getAncestorWindow();
    CommandHandler getCommandHandler();
    BattleField getBattleField();
    void registerMenu(PluggableMenu menu);
    void unregisterMenu(PluggableMenu menu);
    void setContent(PluggableContent content);
    void clearContent(PluggableContent content);

}
