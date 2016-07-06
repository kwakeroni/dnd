package active.engine.gui.swing;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.swing.*;

import active.engine.command.CommandContext;
import active.engine.command.CommandHandler;
import active.engine.event.EventBroker;
import active.engine.gui.InteractionHandler;
import active.engine.util.gui.swing.WindowAdapter;
import active.model.cat.Named;
import active.model.event.Datum;
import active.model.fight.Participant;
import active.model.fight.event.FightData;

public class EngineWindow implements InteractionHandler, ContainerAdapter {

    private final JFrame frame;
    private final CharacterList characterList;

    public EngineWindow(CommandHandler appCommandHandler, FightData fightData, EventBroker<?> events){
        CommandHandler context = appCommandHandler;

        this.frame = new JFrame();
        this.frame.setMinimumSize(new Dimension(640,480));
        this.frame.setTitle("Dungeons and Dragons 3.5 Fight Assistant");
        this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.frame.addWindowListener(WindowAdapter.closing(event -> showCloseDialog()));

        this.characterList = new CharacterList(fightData);
        this.frame.getContentPane().add(this.characterList.component(), BorderLayout.CENTER);

        JPanel rightPane = new JPanel();
        this.frame.getContentPane().add(rightPane, BorderLayout.EAST);

        rightPane.add(new ActionPane(context, fightData).component(), BorderLayout.WEST);
//        rightPane.add(new LogPane(events).component(), BorderLayout.EAST);

        context.registerContext(InteractionHandler.class, this);
    }
    
    public void show(){
        this.frame.pack();
        this.frame.setVisible(true);
    }
    
    private void close(){
        this.frame.setVisible(false);
        this.frame.dispose();
    }
    
    private void showCloseDialog(){
        int choice = JOptionPane.showOptionDialog(this.frame, "Do you really want to quit ?", "Quit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (choice == JOptionPane.YES_OPTION){
            this.close();
        }
    }

    @Override
    public Stream<? extends Component> components() {
        return Arrays.stream(this.frame.getContentPane().getComponents());
    }

    @Override
    public void withParticipant(Consumer<Participant> action) {
        System.out.println("withp");
        Snapshot snapshot = setEnabled(c -> c == this.characterList.component());
        this.characterList.withParticipant(action.andThen(p -> snapshot.restore()));
    }

}
