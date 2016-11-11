package active.engine.gui.swing.support;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.util.PrimitiveIterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import static active.engine.gui.swing.support.listener.FocusListenerSupport.onFocusLost;
import static active.engine.gui.swing.support.listener.KeyListenerSupport.onKeyTyped;
import static active.engine.gui.swing.support.listener.MouseListenerSupport.onMouseDoubleClicked;

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

    public EditableJLabel() {
        this(new JLabel());
    }

    public EditableJLabel(String text) {
        this(new JLabel(text));
    }

    public EditableJLabel(String text, int horizontalAlignment) {
        this(new JLabel(text, horizontalAlignment));
    }

    public EditableJLabel(JLabel label) {
        this.label = label;
        this.field = new JTextField();
        this.layout = new CardLayout();
        setLayout(this.layout);
        this.add(label, LABEL);
        this.add(this.field, FIELD);

        PrimitiveIterator.OfInt ints = ThreadLocalRandom.current().ints(0, 255).iterator();

        this.label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.label.addMouseListener(onMouseDoubleClicked(this::requestInput));
        this.field.addFocusListener(onFocusLost(this::processInput));
        this.field.addKeyListener(onKeyTyped(e -> {
            if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                processInput();
            }
        }));
    }

    private void requestInput() {
        this.field.setFont(label.getFont());
        this.field.setHorizontalAlignment(this.label.getHorizontalAlignment());
        this.field.setText(this.label.getText());


        int length = field.getText().length();
        field.setCaretPosition(length);
        field.setSelectionStart(0);
        field.setSelectionEnd(length);
        field.grabFocus();

        this.layout.show(this, FIELD);
    }

    private void processInput() {
        listener.accept(field.getText());
        layout.show(EditableJLabel.this, LABEL);
    }

    public void setText(String text) {
        this.label.setText(text);
    }

    public JLabel getBackingLabel() {
        return this.label;
    }

    public void setInputListener(Consumer<String> listener) {
        this.listener = listener;
    }
}
