package active.engine.internal.fight;

import active.model.fight.Participant;

import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
class SingletonParticipantChain implements ParticipantChain {

    private Participant participant;

    public SingletonParticipantChain(Participant participant) {
        this.participant = participant;
    }

    @Override
    public ParticipantChain addFollower(Participant parent, Participant follower) {
        return new DefaultParticipantChain(this.participant).addFollower(parent, follower);
    }

    @Override
    public boolean contains(Participant participant) {
        return this.equals(participant);
    }

    @Override
    public Stream<Participant> getParticipants() {
        return Stream.of(this.participant);
    }

    @Override
    public Participant getLeader() {
        return participant;
    }
}
