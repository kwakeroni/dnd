package active.engine.gui.swing.support;

import active.engine.gui.swing.MouseListenerSupport;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import static active.engine.gui.swing.MouseListenerSupport.*;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class EditableJLabel extends JPanel {
    private static final String LABEL = "LABEL";
    private static final String FIELD = "FIELD";

    private final CardLayout layout;
    private final JLabel label;
    private final JTextField field;
    private Consumer<String> listener;

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
        this.field = new JTextField();
        this.layout = new CardLayout();
        setLayout(this.layout);
        this.add(label, LABEL);
        this.add(this.field, FIELD);


//        cards.add(card1, BUTTONPANEL);
//        cards.add(card2, TEXTPANEL);

//        CardLayout cl = (CardLayout)(cards.getLayout());
//        cl.show(cards, (String)evt.getItem());
        PrimitiveIterator.OfInt ints = ThreadLocalRandom.current().ints(0,255).iterator();
        this.label.addMouseListener(onMouseDoubleClicked(e -> {
            this.field.setFont(label.getFont());
            this.field.setHorizontalAlignment(this.label.getHorizontalAlignment());
            this.field.setText(this.label.getText());


            int length = field.getText().length();
            field.setCaretPosition(length);
            field.setSelectionStart(0);
            field.setSelectionEnd(length);
            field.grabFocus();

            this.layout.show(this, FIELD);
        }));

        this.field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                processInput();
            }
        });

        this.field.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER){
                    processInput();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

    }

    private void processInput(){
//        label.setText(field.getText());
        listener.accept(field.getText());
        layout.show(EditableJLabel.this, LABEL);
    }

    public void setText(String text){
        this.label.setText(text);
    }

    public JLabel getBackingLabel(){
        return this.label;
    }

    public void setInputListener(Consumer<String> listener){
        this.listener = listener;
    }
}
