package active.engine.internal.fight;

import active.model.fight.Participant;

import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
interface ParticipantChain<P extends Participant> {

    public ParticipantChain addFollower(Participant parent, P follower);

    public boolean contains(Participant participant);

    public Stream<P> getParticipants();

    public P getLeader();

}
