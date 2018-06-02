package be.kwakeroni.dnd.ui.swing.gui.comp;

import javax.swing.*;
import java.awt.*;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class JExtTextField extends JTextField {

    private String placeHolder;

    public JExtTextField(String placeHolder, String text, int columns) {
        super(text, columns);
        this.placeHolder = placeHolder;
    }

    public JExtTextField(String placeHolder, String text) {
        super(text);
        this.placeHolder = placeHolder;
    }

    public JExtTextField(String text, int columns) {
        super(text, columns);
    }

    public JExtTextField(String text) {
        super(text);
    }

    public JExtTextField(int columns) {
        super(columns);
    }

    public JExtTextField() {
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder){
        this.placeHolder = placeHolder;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        String text = this.getText();

        if (this.placeHolder != null && this.placeHolder.length() > 0 && (text==null || text.isEmpty())){
            Graphics2D g2 = (Graphics2D) g;

            int margin = Math.abs((getHeight() - (int) g.getFontMetrics().getHeight()))/2;

            g2.setColor(new Color(0,0,0,64));
            g2.drawString(this.placeHolder, getInsets().left, getHeight() - margin - g.getFontMetrics().getMaxDescent());
        }
    }



}
