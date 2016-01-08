package active.engine.internal.fight;

import active.model.fight.Fight;
import active.model.fight.FightController;
import active.model.fight.Participant;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public class FightSetup {

    Collection<Participant> participants = new HashSet<>();

    public void add(Participant participant){
        this.participants.add(participant);
    }

    public Stream<Participant> participants() { return this.participants.stream(); }

    public FightController start(){
        DefaultFight fight = new DefaultFight();
        this.participants.forEach(fight::add);
        return new DefaultFightController(fight);
    }

}
