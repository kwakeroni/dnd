package be.kwakeroni.dnd.engine.model.fight;

import be.kwakeroni.dnd.model.fight.Participant;

import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
interface ParticipantChain<P extends Participant> {

    public ParticipantChain<P> addFollower(Participant parent, P follower);

    public boolean contains(Participant participant);

    public Stream<P> getParticipants();

    public P getLeader();

}
