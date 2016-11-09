package active.engine.command;

public class CommandHandlerSupport implements CommandHandler {

    private CommandContextSupport context = new CommandContextSupport();
    
    @Override
    public void execute(Command command) {
        command.execute(context);
    }

    public <C> void registerContext(Class<C> contextType, C context){
        this.context.register(contextType, context);
    }

    @Override
    public void unregisterContext(Object context) {
        this.context.unregister(context);
    }
}
