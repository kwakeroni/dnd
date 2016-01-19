package active.engine.gui.swing;

import java.util.stream.Stream;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;

import active.model.cat.Named;

class CharacterList extends JPanel {

    CharacterList(Stream<? extends Named> characters){
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        characters.forEach(named -> this.add(new JLabel(named.getName()))); // Binding
        
        
    }
    
}
