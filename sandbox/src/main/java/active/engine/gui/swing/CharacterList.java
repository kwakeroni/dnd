package active.engine.gui.swing;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.swing.*;

import active.model.cat.Named;
import active.model.event.Datum;

class CharacterList<C extends Named> {

    private static final Insets INSETS = new Insets(4,8,4,8);

    private Datum<? extends Stream<? extends C>> data;
    private final Function<C, ? extends CharacterLine<? super C>> lineFactory;
    private final JPanel panel = new JPanel();
    private Map<String, CharacterLine<? super C>> lines = new HashMap<>();

    CharacterList(Datum<? extends Stream<? extends C>> characters, Function<C, ? extends CharacterLine<? super C>> lineFactory){
        GridBagLayout layout = new GridBagLayout();
        this.panel.setLayout(layout);
        this.lineFactory = lineFactory;

        this.data = characters;
        this.data.onChanged(this::updateData);

        updateData();

    }

    public JComponent component(){
        return this.panel;
    }

    private synchronized void updateData(){

        Map<String, CharacterLine<? super C>> newLines = new HashMap<>();
        this.panel.removeAll();

        AtomicInteger columns = new AtomicInteger(0);
        data.get()
                  .forEach(named -> {
                      String name = named.getName();

                      CharacterLine<? super C> line = getLine(name).orElseGet(() -> this.lineFactory.apply(named));
                      newLines.put(name, line);

                      int l = newLines.size() - 1;
                      int n = 0;
                      for (JComponent c : line.components()) {
                          this.panel.add(c, constraint(l, n++));
                      }
                      int cols = n;
                      columns.getAndUpdate(i -> (i<cols)? cols : i);
                      //this.panel.add(new JLabel(""), spacer(l, n++));

                  });

                this.panel.add(new JLabel(""), spacer(newLines.size(), columns.get()));


        this.lines = newLines;

        this.panel.repaint();
        this.panel.revalidate();
    }

    private Optional<CharacterLine<? super C>> getLine(String name){
        return Optional.ofNullable(this.lines.get(name));
    }

    private GridBagConstraints constraint(int line, int component){
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = component;
        c.gridy = line;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = INSETS;
        return c;
    }

    private GridBagConstraints spacer(int line, int component){
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = component;
        c.gridy = line;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = INSETS;
        c.weightx = 1.0;
        c.weighty = 1.0;
        return c;
    }

    private CharacterLine<? super C> createLine(C named){
        return this.lineFactory.apply(named);
    }


}
