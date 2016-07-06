package active.engine.command;

public interface CommandHandler {

    void execute(Command command);

    public <C> void registerContext(Class<C> contextType, C context);
    
}
