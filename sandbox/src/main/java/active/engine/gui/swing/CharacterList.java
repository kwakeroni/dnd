package active.engine.gui.swing;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.swing.*;

import active.model.fight.Participant;
import active.model.fight.event.FightData;

import static active.engine.gui.swing.MouseListenerSupport.onMouseClicked;

class CharacterList implements ContainerAdapter{

    private static final Insets INSETS = new Insets(4,8,4,8);

    private FightData data;
    private final JPanel panel = new JPanel();
    private Map<String, ParticipantLine> lines = new HashMap<>();

    CharacterList(FightData fightData){
        GridBagLayout layout = new GridBagLayout();
        this.panel.setLayout(layout);

        this.data = fightData;
        this.data.participants().onChanged(this::updateData);
        this.data.turn().onChanged(this::updateTurn);

        updateData();

    }

    public JComponent component(){
        return this.panel;
    }

    private synchronized void updateTurn(){
        lines.values().forEach(CharacterLine::deselect);
        data.turn().get().ifPresent(turn -> {
            ParticipantLine line = lines.get(turn.getActor().getName());
            line.select();
        });
        
    }

    private synchronized void updateData(){

        Map<String, ParticipantLine> newLines = new HashMap<>();
        this.panel.removeAll();

        AtomicInteger columns = new AtomicInteger(0);
        data.participants().get()
                  .forEach(named -> {
                      String name = named.getName();

                      ParticipantLine line = getLine(name).orElseGet(() -> new ParticipantLine(named));
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

    private Optional<ParticipantLine> getLine(String name){
        return Optional.ofNullable(this.lines.get(name));
    }

    private GridBagConstraints constraint(int line, int component){
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = component;
        c.gridy = line;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 24;
        c.ipady = 12;
        //c.insets = INSETS;
        return c;
    }

    private GridBagConstraints spacer(int line, int component){
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = component;
        c.gridy = line;
        c.fill = GridBagConstraints.HORIZONTAL;
        //c.insets = INSETS;
        c.weightx = 1.0;
        c.weighty = 1.0;
        return c;
    }


    public void withParticipant(Consumer<Participant> action) {

        AtomicReference<Optional<Snapshot>> snapshot= new AtomicReference<>(Optional.empty());

        MouseListener listener = new MouseListenerSupport(){
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Clicked " + e.getComponent());
                lines.values().stream()
                        .peek(line -> System.out.println(line.components()))
                        .filter(line -> line.components().contains(e.getComponent()))
                        .findAny()
                        .map(ParticipantLine::getParticipant)
                        .ifPresent(action);
                snapshot.get().ifPresent(Snapshot::restore);
            }
        };

        snapshot.set(Optional.of(modify(c -> c.addMouseListener(listener), c -> c.removeMouseListener(listener))));

    }

    @Override
    public Stream<? extends Component> components() {
        return this.lines.values()
                .stream()
                .flatMap(line -> line.components().stream());
    }
}
