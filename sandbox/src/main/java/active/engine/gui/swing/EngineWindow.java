package active.engine.gui.swing;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import active.engine.util.gui.swing.WindowAdapter;
import active.model.cat.Named;
import active.model.event.Datum;
import active.model.fight.Participant;
import active.model.fight.event.FightData;

public class EngineWindow {

    private JFrame frame;

    public EngineWindow(FightData fightData){
        this.frame = new JFrame();
        this.frame.setTitle("Dungeons and Dragons 3.5 Fight Assistant");
        this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.frame.addWindowListener(WindowAdapter.closing(event -> showCloseDialog()));

        this.frame.getContentPane().add(new CharacterList(fightData).component());
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
    
}
