package active.engine.gui.swing.support;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.CardLayout;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class EditableJLabel extends JPanel {
    private static final String LABEL = "LABEL";
    private static final String FIELD = "FIELD";

    private final CardLayout layout;
    private final JLabel label;

    public EditableJLabel(){
        this(new JLabel());
    }

    public EditableJLabel(String text) {
        this(new JLabel(text));
    }

    public EditableJLabel(String text, int horizontalAlignment) {
        this(new JLabel(text, horizontalAlignment));
    }

    public EditableJLabel(JLabel label){
        this.label = label;
        this.layout = new CardLayout();
        setLayout(this.layout);
        this.add(label);

//        cards.add(card1, BUTTONPANEL);
//        cards.add(card2, TEXTPANEL);

//        CardLayout cl = (CardLayout)(cards.getLayout());
//        cl.show(cards, (String)evt.getItem());

    }

    public void setText(String text){
        this.label.setText(text);
    }

    public JLabel getBackingLabel(){
        return this.label;
    }
}
