package active.engine.gui.swing.support;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class JStatusBar extends JPanel {

    private JLabel label;

    public JStatusBar(){
        this.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        JPanel centerPanel = new JPanel();
        JPanel rightPanel = new JPanel();

        this.add(leftPanel, BorderLayout.WEST);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(rightPanel, BorderLayout.EAST);


        this.setBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED)
        );

        this.label = new JLabel();
        this.label.setHorizontalAlignment(JLabel.LEFT);
        leftPanel.add(this.label);
    }

    public void setText(String text){
        this.label.setText(text);
    }

}
