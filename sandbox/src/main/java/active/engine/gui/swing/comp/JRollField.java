package active.engine.gui.swing.comp;

import active.engine.gui.swing.support.ComponentAdapter;
import active.engine.gui.swing.support.builder.EventListenerBuilder;
import active.model.die.Die;
import active.model.die.Roll;
import active.model.event.Reaction;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class JRollField<D extends Die> implements ComponentAdapter {

    private static final String DIE_IMG = String.valueOf(Character.toChars(0x1F3B2));

    private final Map<String, Reaction> rollListeners = new HashMap<>();
    private final JPanel panel;
    private final JExtTextField rollField;
    private final D die;

    public JRollField(D die) {
        this.die = die;

        this.rollField = new JExtTextField(die.toString(), "", 3);
        JButton button = newRollButton();

        this.panel = new JPanel();
        this.panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        this.panel.add(rollField);
        this.panel.add(button);

        Color standard = rollField.getBackground();
        Color error = new Color(255, 196, 196);

        EventListenerBuilder
                .of(this.rollField)
                .onKeyReleased(e -> {
                    if (isValid()) {
                        rollField.setBackground(standard);
                    } else {
                        rollField.setBackground(error);
                    }
                    this.fireRollChange();
                });

        EventListenerBuilder
                .of(button)
                .onAction(this::roll);
    }

    public void roll() {
        setRoll(Roll.of(die));
    }

    public void clear() {
        setRoll(null);
    }

    private void setRoll(Roll<D> roll) {
        if (roll == null) {
            this.rollField.setText("");
        } else {
            this.rollField.setText(String.valueOf(roll.asInt()));
        }
        this.fireRollChange();
    }

    public Optional<Roll<D>> getRoll() {
        return getIntText()
                .filter(die::isValid)
                .map(r -> Roll.of(die, r));
    }

    private Optional<Integer> getIntText() throws NumberFormatException {
        String text = rollField.getText();
        if (text == null || text.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Integer.valueOf(text));
    }

    private boolean isValid() {
        try {
            return getIntText().map(die::isValid).orElse(true);
        } catch (NumberFormatException exc) {
            return false;
        }
    }


    @Override
    public Component component() {
        return this.panel;
    }

    public void addRollListener(String name, Reaction listener) {
        this.rollListeners.put(name, listener);
    }

    public void removeRollListener(String name) {
        this.rollListeners.remove(name);
    }

    public void onRoll(Reaction reaction) {
        addRollListener(UUID.randomUUID().toString(), reaction);
    }

    private void fireRollChange() {
        for (Reaction listener : this.rollListeners.values()) {
            listener.react();
        }
    }

    public static final JButton newRollButton() {
        return newRollButton("Roll");
    }

    public static final JButton newRollButton(String toolTipText) {
        JButton button = new JButton(DIE_IMG);
        button.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        button.setMargin(new Insets(1, 1, 1, 1));
        button.setToolTipText(toolTipText);
        return button;
    }
}
