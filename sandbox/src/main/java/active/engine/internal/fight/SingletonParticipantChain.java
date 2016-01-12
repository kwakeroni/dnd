package active.engine.internal.fight;

import active.model.fight.Participant;

import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
class SingletonParticipantChain<P extends Participant> implements ParticipantChain<P> {

    private P participant;

    public SingletonParticipantChain(P participant) {
        this.participant = participant;
    }

    @Override
    public ParticipantChain addFollower(Participant parent, P follower) {
        return new DefaultParticipantChain(this.participant).addFollower(parent, follower);
    }

    @Override
    public boolean contains(Participant participant) {
        return this.equals(participant);
    }

    @Override
    public Stream<P> getParticipants() {
        return Stream.of(this.participant);
    }

    @Override
    public P getLeader() {
        return participant;
    }
}
