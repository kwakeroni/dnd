package be.kwakeroni.dnd.engine.command;

import be.kwakeroni.dnd.engine.api.command.Command;
import be.kwakeroni.dnd.engine.api.command.CommandHandler;

public abstract class CommandHandlerSupport implements CommandHandler {

    protected final CommandContextSupport context;

    private CommandHandlerSupport(CommandContextSupport context){
        this.context = context;
    }

    public <C> void registerContext(Class<C> contextType, C context){
        this.context.register(contextType, context);
    }

    @Override
    public void unregisterContext(Object context) {
        this.context.unregister(context);
    }

    @Override
    public CommandHandler overlay() {
        return new Overlay(this);
    }

    @Override
    public void execute(Command command) {
        command.execute(context);
    }

    public static CommandHandler root(){
        return new Root();
    }

    private static class Root extends CommandHandlerSupport {
        public Root() {
            super(new CommandContextSupport());
        }

        @Override
        public void invalidate() {
            throw new UnsupportedOperationException("Cannot invalidate of Root command handler");
        }
    }

    private static class Overlay extends CommandHandlerSupport {
        public Overlay(CommandHandlerSupport delegate){
            super(new CommandContextSupport(delegate.context));
        }

        @Override
        public void invalidate() {
            this.context.invalidate();
        }
    }

}
