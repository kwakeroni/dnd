package be.kwakeroni.dnd.ui.base;

import be.kwakeroni.dnd.engine.api.InteractionHandler;
import be.kwakeroni.dnd.engine.api.command.CommandHandler;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface PluggableContent<WindowType, ContainerType> {

    CommandHandler getCommandHandler();

    ContainerType getComponent();

    void activate(GUIController<? extends WindowType, ? super ContainerType> gui, CommandHandler parentHandler);

    void deactivate(GUIController<? extends WindowType, ? super ContainerType> gui);

}
