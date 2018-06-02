package be.kwakeroni.dnd.ui.swing.gui.pane;

import be.kwakeroni.dnd.event.EventBroker;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.parser.ParserDelegator;
import java.awt.*;
import java.io.IOException;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class LogPane {

    private JPanel panel;
    private HTMLDocument logDoc;

    public LogPane(EventBroker<?> events){
        this.panel = new JPanel();
        this.logDoc = new HTMLDocument();
        this.logDoc.setParser(new ParserDelegator());
        try {
            logDoc.setInnerHTML(logDoc.getDefaultRootElement(), "<html><body><div>Log <b>some</b> message</div></body></html>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JTextPane textPane = new JTextPane(logDoc);
        textPane.setText("<html><body><div>Log <b>some</b> message</div></body></html>");


        JScrollPane scrollPane = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.panel.add(scrollPane);

//        insertBeforeEnd("<div>Log <b>some</b> message</div>");

        events.on().event()
                    .forEach(event -> {
                        insertBeforeEnd(String.format("<div>%s</div><br />\\r\\n", event.toString()));
                    });

    }

    public Component component(){
        return this.panel;
    }

    private void insertBeforeEnd(String html){
        try {
            this.logDoc.insertBeforeEnd(
                    this.logDoc.getDefaultRootElement(),
                    html
            );
        } catch (BadLocationException | IOException exc) {
            throw new RuntimeException(exc);
        }

    }
}
