package active.engine.gui.swing;

import active.model.cat.Actor;
import active.model.cat.Hittable;
import active.model.creature.event.StatChanged;
import active.model.creature.stats.Base;
import active.model.creature.stats.Mod;
import active.model.creature.stats.Statistic;
import active.model.fight.Participant;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Maarten Van Puymbroeck
 */
class ParticipantLine extends CharacterLine<Participant> {

    private final Participant participant;
    private final Map<Statistic<?>, JLabel> components;

    public ParticipantLine(Participant participant){
        this.participant = participant;

        LinkedHashMap<Statistic<?>, JLabel> map = new LinkedHashMap<>(3);
        map.put(Base.NAME, name());
        map.put(Mod.INIT, init());
        map.put(Base.HP, hp());

        this.components = Collections.unmodifiableMap(map);

        this.participant.on()
                        .statChanged()
                        .forEach(sc -> update(sc));

    }

    private <S> void update(StatChanged<S> event){
        Statistic<S> stat = event.getStat();
        S newValue = event.getNewValue();
        JLabel component = components.get(stat);
        if (component != null){
            component.setText(String.valueOf(newValue));
        }
    }

    private JLabel name(){
        return newLabel(participant.getName()).right().create();
    }

    private JLabel init(){
        return newLabel(participant.getInitiative().map(Object::toString).orElse("")).center().create();
    }

    private JLabel hp(){
        return newLabel(participant.asTarget().map(h -> h.getHP()).map(Object::toString).orElse("")).center().create();
    }

    public Participant getParticipant(){
        return this.participant;
    }


    public Collection<? extends JComponent> components(){
        return this.components.values();
    }

    @Override
    public void select() {
        System.out.println("SELECT");
        this.components.values().forEach(label -> { label.setOpaque(true); label.repaint(); });
    }

    @Override
    public void deselect() {
        this.components.values().forEach(label -> { label.setOpaque(false); label.repaint(); });
    }
    
    
}
