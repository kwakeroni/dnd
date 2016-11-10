package active.engine.gui.swing.fight;

import active.engine.gui.business.CharacterList;
import active.engine.gui.swing.Snapshot;
import active.engine.gui.swing.support.ContainerAdapter;
import active.engine.gui.swing.support.TableLayout;
import active.model.fight.Participant;
import active.model.fight.event.FightData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static active.engine.gui.swing.MouseListenerSupport.onMouseClicked;

public class JCharacterList implements CharacterList, ContainerAdapter {

    private static final Insets INSETS = new Insets(4, 8, 4, 8);

    private FightData data;
    private final JPanel panel = new JPanel();
    private Map<String, JParticipantLine> lines = new HashMap<>();

    public JCharacterList(FightData fightData) {
        TableLayout layout = new TableLayout(this.panel, true, 24, 12);
        this.panel.setLayout(layout);
        this.data = fightData;
        initBehaviour(this.data);
    }

    public JComponent component() {
        return this.panel;
    }

    public synchronized void updateTurn() {
        lines.values().forEach(CharacterLine::deselect);
        data.turn().get().ifPresent(turn -> {
            JParticipantLine line = lines.get(turn.getActor().getName());
            line.select();
        });

    }

    public synchronized void updateParticipantData() {

        Map<String, JParticipantLine> newLines = new HashMap<>();
        this.panel.removeAll();

        AtomicInteger columns = new AtomicInteger(0);
        data.participants().get()
                .forEach(named -> {
                    String name = named.getName();

                    JParticipantLine line = getLine(name).orElseGet(() -> new JParticipantLine(named));
                    newLines.put(name, line);

                    TableLayout.addRow(this.panel, line);
            });
        TableLayout.addFillerRow(this.panel);

        this.lines = newLines;

        this.panel.repaint();
        this.panel.revalidate();
    }

    private Optional<JParticipantLine> getLine(String name) {
        return Optional.ofNullable(this.lines.get(name));
    }





    public void withParticipant(Consumer<Participant> action) {

        AtomicReference<Optional<Snapshot>> snapshot = new AtomicReference<>(Optional.empty());

        MouseListener listener = onMouseClicked(event -> {
            participantOf(event.getComponent()).ifPresent(action);
            snapshot.get().ifPresent(Snapshot::restore);
        });

        snapshot.set(Optional.of(modify(
                c -> c.addMouseListener(listener),
                c -> c.removeMouseListener(listener))));
    }

    private Optional<Participant> participantOf(Component component) {
        if (component == null || component == this.panel){
            return Optional.empty();
        } else {
            Optional<Participant> result = lines.values().stream()
                    .filter(line -> line.components().contains(component))
                    .findAny()
                    .map(JParticipantLine::getParticipant);
            if (result.isPresent()){
                return result;
            } else {
                return participantOf(component.getParent());
            }
        }
    }

    @Override
    public Stream<? extends Component> componentStream() {
        return this.lines.values()
                .stream()
                .flatMap(line -> line.components().stream());
    }

    @Override
    public Collection<? extends Component> components() {
        return componentStream().collect(Collectors.<Component>toSet());
    }
}
