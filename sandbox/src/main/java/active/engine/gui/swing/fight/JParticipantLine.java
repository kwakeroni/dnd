package active.engine.gui.swing.fight;

import active.engine.gui.business.ParticipantData;
import active.engine.gui.swing.support.ContainerAdapter;
import active.engine.gui.swing.support.EditableJLabel;
import active.engine.util.gui.swing.LabelBuilder;
import active.model.creature.stats.Base;
import active.model.creature.stats.Mod;
import active.model.creature.stats.Statistic;
import active.model.fight.Participant;
import active.model.value.Score;

import javax.swing.JLabel;
import java.awt.Component;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
class JParticipantLine extends CharacterLine<Participant> implements ParticipantData, ContainerAdapter {

    private final Participant participant;
    private final Map<Statistic<?>, Element> elements;
    private StatUpdateListener statUpdateListener;

    public JParticipantLine(Participant participant) {
        this.participant = participant;

        LinkedHashMap<Statistic<?>, Element> map = new LinkedHashMap<>(3);
        map.put(Base.NAME, name());
        map.put(Mod.INIT, init());
        map.put(Base.HP, hp());

        this.elements = Collections.unmodifiableMap(map);

        initBehaviour(this.participant);
    }

    @Override
    public boolean has(Statistic<?> stat) {
        return this.elements.containsKey(stat);
    }

    @Override
    public <S> void update(Statistic<S> stat, S oldValue, S newValue) {
        elements.get(stat).setText(String.valueOf(newValue));
    }

    private Element name() {
        return ineditable(newLabel(participant.getName()).right());
    }

    private Element init() {
        return ineditable(newLabel(participant.getInitiative().map(Object::toString).orElse("")).center());
    }

    private Element hp() {
        return editable(
                Base.HP, Score::fromString,
                newLabel(participant.asTarget().map(h -> h.getHP()).map(Object::toString).orElse("")).center());
    }

    private Element ineditable(LabelBuilder label) {
        return new JLabelElement(label.create());
    }

    private <S> Element editable(Statistic<S> stat, Function<String, S> transformer, LabelBuilder label) {
        EditableJLabel editable = new EditableJLabel(label.create());
        editable.setInputListener(string -> statUpdateListener.onStatUpdate(stat, transformer.apply(string)));
        return new EditableElement(editable);
    }

    public Participant getParticipant() {
        return this.participant;
    }


    public Collection<? extends Component> components() {
        return componentStream().collect(Collectors.<Component> toList());
    }

    @Override
    public Stream<? extends Component> componentStream() {
        return this.elements.values().stream().map(Element::getComponent);
    }

    @Override
    public void select() {
        this.elements.values().forEach(e -> e.select(true));
    }

    @Override
    public void deselect() {
        this.elements.values().forEach(e -> e.select(false));
    }

    @Override
    public void setStatUpdateListener(StatUpdateListener listener) {
        this.statUpdateListener = listener;
    }

    private static interface Element {
        void setText(String text);

        Component getComponent();

        void select(boolean isSelected);
    }

    private static class JLabelElement implements Element {
        private JLabel label;

        public JLabelElement(JLabel label) {
            this.label = label;
        }

        @Override
        public Component getComponent() {
            return label;
        }

        @Override
        public void setText(String text) {
            label.setText(text);
        }

        @Override
        public void select(boolean isSelected) {
            label.setOpaque(isSelected);
            label.repaint();
        }
    }

    private static class EditableElement implements Element {
        private EditableJLabel label;

        public EditableElement(EditableJLabel label) {
            this.label = label;
        }

        @Override
        public Component getComponent() {
            return label;
        }

        @Override
        public void setText(String text) {
            label.setText(text);
        }

        @Override
        public void select(boolean isSelected) {
            label.getBackingLabel().setOpaque(isSelected);
            label.repaint();
        }
    }
}
