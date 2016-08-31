package active.engine.gui.swing.support.builder;

import java.awt.TextField;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class TextFieldBuilder implements
        ComponentBuilder<TextField, TextFieldBuilder>,
        ComponentListenerBuilder<TextField, TextFieldBuilder> {

    private final TextField textField;

    public TextFieldBuilder() {
        this.textField = new TextField();
        this.textField.setEditable(true);
    }

    public TextFieldBuilder columns(int i){
        this.textField.setColumns(i);
        return this;
    }

    public TextFieldBuilder disabled(){
        this.textField.setEditable(false);
        return this;
    }

    public TextField build(){
        return this.textField;
    }

    @Override
    public final TextField component() {
        return this.textField;
    }

    @Override
    public final TextFieldBuilder self() {
        return this;
    }
}
