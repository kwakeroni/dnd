package be.kwakeroni.dnd.ui.base;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface PluggableMenu<MenuType> {

    void attach(MenuType menu);
    void detach();

}
