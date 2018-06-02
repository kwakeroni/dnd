package be.kwakeroni.dnd.io.model;

import be.kwakeroni.dnd.model.fight.AttackActionType;

public interface ModelImplementation {

    public Class<? extends MutableFight> fightImplementationClass();

    public MutableFight newFight();

    public Class<? extends MutableParticipant> participantImplementationClass();

    public MutableParticipant newParticipant();

    public Class<? extends MutableParty> partyImplementationClass();

    public MutableParty newParty();

    public Class<? extends MutableCreature> creatureImplementationClass();

    public MutableCreature newCreature();

    public Class<? extends MutableAttack> attackImplementationClass();

    public MutableAttack newAttack();

    public MutableAttackActionType newAttackActionType();

}
