package active.engine.internal.fight;

import active.engine.util.Lists;
import active.model.fight.Participant;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
class DefaultParticipantChain implements ParticipantChain {

    private final List<Participant> participants = new ArrayList<>(2);

    public DefaultParticipantChain(Participant parent){
        this.participants.add(parent);
    }

    @Override
    public ParticipantChain addFollower(Participant parent, Participant follower) {
        int index = Lists.indexOf(participants, parent).orElseThrow(() -> new IllegalStateException());

        this.participants.add(index+1, follower);

        return this;
    }

    @Override
    public boolean contains(Participant participant) {
        return this.participants.contains(participant);
    }

    public Stream<Participant> getParticipants(){
        return this.participants.stream();
    }

    @Override
    public Participant getLeader() {
        return this.participants.get(0);
    }
}
