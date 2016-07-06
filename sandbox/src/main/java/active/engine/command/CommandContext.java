package active.engine.command;

public interface CommandContext {

    <C> C getContext(Class<C> context);
    
}
