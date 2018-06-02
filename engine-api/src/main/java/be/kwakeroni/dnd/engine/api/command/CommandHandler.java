package be.kwakeroni.dnd.engine.api.command;

public interface CommandHandler {

    void execute(Command command);

    public <C> void registerContext(Class<C> contextType, C context);

    public void unregisterContext(Object context);

    public CommandHandler overlay();

    public void invalidate();
}
