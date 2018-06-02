package be.kwakeroni.dnd.ui.swing.gui.fight;

import be.kwakeroni.dnd.model.creature.stats.Base;
import be.kwakeroni.dnd.model.creature.stats.Mod;
import be.kwakeroni.dnd.model.creature.stats.Statistic;
import be.kwakeroni.dnd.model.fight.Participant;
import be.kwakeroni.dnd.ui.base.model.ParticipantUI;
import be.kwakeroni.dnd.ui.swing.gui.support.ContainerAdapter;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Maarten Van Puymbroeck
 */
class JParticipantLine extends CharacterLine<Participant> implements ParticipantUI, ContainerAdapter {

    private final Participant participant;
    private final Map<Statistic<?>, JLabel> components;

    public JParticipantLine(Participant participant) {
        this.participant = participant;

        LinkedHashMap<Statistic<?>, JLabel> map = new LinkedHashMap<>(3);
        map.put(Base.NAME, name());
        map.put(Mod.INIT, init());
        map.put(Base.HP, hp());

        this.components = Collections.unmodifiableMap(map);

        initBehaviour(this.participant);
    }

    @Override
    public boolean has(Statistic<?> stat) {
        return this.components.containsKey(stat);
    }

    @Override
    public <S> void update(Statistic<S> stat, S oldValue, S newValue) {
        components.get(stat).setText(String.valueOf(newValue));
    }

    private JLabel name() {
        return newLabel(participant.getName()).right().build();
    }

    private JLabel init() {
        return newLabel(participant.getInitiative().map(Object::toString).orElse("")).center().build();
    }

    private JLabel hp() {
        return newLabel(participant.asTarget().map(h -> h.getHP()).map(Object::toString).orElse("")).center().build();
    }

    public Participant getParticipant() {
        return this.participant;
    }


    public Collection<? extends Component> components() {
        return this.components.values();
    }

    @Override
    public void select() {
        this.components.values().forEach(label -> {
            label.setOpaque(true);
            label.repaint();
        });
    }

    @Override
    public void deselect() {
        this.components.values().forEach(label -> {
            label.setOpaque(false);
            label.repaint();
        });
    }


}
