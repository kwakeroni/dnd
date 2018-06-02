package be.kwakeroni.dnd.engine.api.command;

import java.util.Optional;

public interface CommandContext {

    public <C> C getContext(Class<C> context);

    public <C> Optional<C> getOptional(Class<C> context);

}
