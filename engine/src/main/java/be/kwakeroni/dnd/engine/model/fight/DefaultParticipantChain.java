package be.kwakeroni.dnd.engine.model.fight;

import be.kwakeroni.dnd.type.collection.Lists;
import be.kwakeroni.dnd.model.fight.Participant;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
class DefaultParticipantChain<P extends Participant> implements ParticipantChain<P> {

    private final List<P> participants = new ArrayList<>(2);

    public DefaultParticipantChain(P parent){
        this.participants.add(parent);
    }

    @Override
    public ParticipantChain<P> addFollower(Participant parent, P follower) {
        int index = Lists.indexOf(participants, parent).orElseThrow(() -> new IllegalStateException());

        this.participants.add(index+1, follower);

        return this;
    }

    @Override
    public boolean contains(Participant participant) {
        return this.participants.contains(participant);
    }

    public Stream<P> getParticipants(){
        return this.participants.stream();
    }

    @Override
    public P getLeader() {
        return this.participants.get(0);
    }
}
