package be.kwakeroni.dnd.ui.base.fight;

import be.kwakeroni.dnd.engine.api.FightController;
import be.kwakeroni.dnd.engine.api.InteractionHandler;
import be.kwakeroni.dnd.ui.base.PluggableContent;

public interface PluggableFightUI<WindowType, ContainerType> extends PluggableContent<WindowType, ContainerType> {

    public FightController getFightController();
    public FightLogger getFightLogger();
    public InteractionHandler getInteractionHandler();

}
