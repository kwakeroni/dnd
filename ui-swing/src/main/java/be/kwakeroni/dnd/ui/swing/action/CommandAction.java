package be.kwakeroni.dnd.ui.swing.action;

import be.kwakeroni.dnd.engine.api.command.Command;
import be.kwakeroni.dnd.engine.api.command.CommandHandler;
import be.kwakeroni.dnd.ui.swing.gui.SwingUIContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Supplier;

public class CommandAction extends AbstractAction {

    private final Supplier<Component> activeComponentSupplier;
    private final Supplier<Command> commandSupplier;
    private final Supplier<CommandHandler> commandHandler;

    public CommandAction(Supplier<Component> activeComponentSupplier, Supplier<Command> commandSupplier, Supplier<CommandHandler> commandHandler) {
        this.activeComponentSupplier = activeComponentSupplier;
        this.commandSupplier = commandSupplier;
        this.commandHandler = commandHandler;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CommandHandler handler = commandHandler.get().overlay();
        handler.registerContext(SwingUIContext.class, new SwingUIContext(activeComponentSupplier.get()));

        Command command = commandSupplier.get();
        handler.execute(command);
    }

}
