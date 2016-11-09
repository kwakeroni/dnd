package active.engine.gui.swing;

import active.engine.command.Command;
import active.engine.command.CommandHandler;

import javax.swing.*;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
@Deprecated // unused
class SwingCommandHandler implements CommandHandler {

    private final CommandHandler delegate;
    private final BlockingQueue<Command> queue;
    private final Thread thread;

    public SwingCommandHandler(CommandHandler delegate) {
        this.delegate = delegate;
        this.queue = new ArrayBlockingQueue<>(2);
        this.thread = new Thread(() -> {
            while (true){
                executeNext();
            }
        }, "CommandQueue");
    }

    @Override
    public void execute(Command command) {
        System.out.println("Received command in thread " + Thread.currentThread());
        try {
            this.queue.put(command);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeNext(){
        try {
            Command command = queue.take();
            this.delegate.execute(command);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <C> void registerContext(Class<C> contextType, C context) {
        this.delegate.registerContext(contextType, context);
    }

    @Override
    public void unregisterContext(Object context) {
        this.delegate.unregisterContext(context);
    }
}
