package active.engine.command;

import java.util.HashMap;
import java.util.Map;

public class CommandContextSupport implements CommandContext {

    private Map<Class<?>, Object> contexts = new HashMap<>();
    
    
    
    @Override
    public <C> C getContext(Class<C> context) {
        if (contexts.containsKey(context)){
            return context.cast(contexts.get(context));
        } else {
            throw new IllegalStateException("No context " + context);
        }
    }
    
    public <C> void register(Class<C> type, C context){
        this.contexts.put(type, context);
    }

}
