package active.engine.internal.fight;

import active.model.fight.Participant;

import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
interface ParticipantChain {

    public ParticipantChain addFollower(Participant parent, Participant follower);

    public boolean contains(Participant participant);

    public Stream<Participant> getParticipants();

    public Participant getLeader();

}
