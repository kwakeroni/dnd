package be.kwakeroni.dnd.engine.command;

import be.kwakeroni.dnd.engine.api.command.CommandContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class CommandContextSupport implements CommandContext {

    private final Map<Class<?>, Object> contexts = new HashMap<>();
    private final CommandContext parent;
    private boolean invalidated = false;

    public CommandContextSupport(){
        this(null);
    }

    public CommandContextSupport(CommandContext parent) {
        this.parent = parent;
        logContext();
    }

    @Override
    public <C> C getContext(Class<C> context) {
        ensureValid();

        if (contexts.containsKey(context)) {
            return context.cast(contexts.get(context));
        } else if (parent != null) {
            return parent.getContext(context);
        } else {
            throw new IllegalStateException("No context " + context);
        }
    }

    @Override
    public <C> Optional<C> getOptional(Class<C> context) {
        ensureValid();

        if (contexts.containsKey(context)) {
            return Optional.of(context.cast(contexts.get(context)));
        } else if (parent != null) {
            return parent.getOptional(context);
        } else {
            return Optional.empty();
        }
    }

    public <C> void register(Class<C> type, C context){
        ensureValid();
        this.contexts.put(type, Objects.requireNonNull(context));
        logContext();
    }

    public void unregister(Object context){
        ensureValid();
        logContext();
        if (! this.contexts.values().remove(context)){
            throw new IllegalStateException("no context registered: " + context);
        }
    }

    public void invalidate() {
        this.invalidated = true;
        this.contexts.clear();
    }

    private void ensureValid() {
        if (this.invalidated){
            throw new IllegalStateException("Attempt to use invalidated command context");
        }
    }

    public String toString(){
        StringBuffer result = new StringBuffer((this.parent == null)? "" : this.parent.toString())
                .append("  + Context ")
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append((this.invalidated)? " (invalidated)" : "")
                .append(System.lineSeparator());
        for (Class<?> $class : this.contexts.keySet()) {
            result.append("- ")
                    .append($class.getName())
                    .append(" : ")
                    .append(this.contexts.get($class))
                    .append(System.lineSeparator());
        }
        return result.toString();
    }

    private void logContext(){
        System.out.println("--- Command Context Changed ---" + System.lineSeparator() + this.toString());
    }
}
