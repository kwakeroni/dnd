package active.engine.gui.swing;

import active.engine.command.CommandHandler;
import active.engine.gui.swing.menu.PluggableMenu;
import active.engine.internal.fight.BattleField;
import active.model.event.Datum;

import javax.swing.JFrame;

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
    Datum<PluggableContent> content();
}
