package be.kwakeroni.dnd.ui.base;

import be.kwakeroni.dnd.engine.api.command.CommandHandler;
import be.kwakeroni.dnd.engine.api.command.fight.FightCommandFactory;
import be.kwakeroni.dnd.engine.api.command.io.IOCommandFactory;
import be.kwakeroni.dnd.ui.base.command.UICommandFactory;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface GUIController<WindowType, ContentType> {

    WindowType getAncestorWindow();
    CommandHandler getCommandHandler();

    public <C> void registerContext(Class<C> contextType, C context);
    public void unregisterContext(Object context);

    public <C> void registerGlobalContext(Class<C> contextType, C context);
    public void unregisterGlobalContext(Object context);


//    BattleField getBattleField();
//    void registerMenu(PluggableMenu<MenuType> menu);
//    void unregisterMenu(PluggableMenu<MenuType> menu);

    public void setContent(PluggableContent<? super WindowType, ContentType> content);
    public void clearContent(PluggableContent<? super WindowType, ContentType> content);

    public FightCommandFactory getFightCommandFactory();
    public IOCommandFactory getIOCommandFactory();
    public UICommandFactory getUICommandFactory();

}
