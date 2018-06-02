package be.kwakeroni.dnd.engine.model;

import be.kwakeroni.dnd.engine.model.fight.DefaultAttackActionType;
import be.kwakeroni.dnd.engine.model.fight.DefaultFight;
import be.kwakeroni.dnd.engine.model.fight.DefaultParticipant;
import be.kwakeroni.dnd.io.model.ModelImplementation;
import be.kwakeroni.dnd.io.model.MutableAttack;
import be.kwakeroni.dnd.io.model.MutableAttackActionType;
import be.kwakeroni.dnd.io.model.MutableCreature;
import be.kwakeroni.dnd.io.model.MutableFight;
import be.kwakeroni.dnd.io.model.MutableParticipant;
import be.kwakeroni.dnd.io.model.MutableParty;

public class DefaultModelImplementation implements ModelImplementation {

    @Override
    public Class<? extends MutableFight> fightImplementationClass() {
        return DefaultFight.class;
    }

    @Override
    public MutableFight newFight() {
        return new DefaultFight();
    }

    @Override
    public Class<? extends MutableParticipant> participantImplementationClass() {
        return DefaultParticipant.class;
    }

    @Override
    public MutableParticipant newParticipant() {
        return new DefaultParticipant();
    }

    @Override
    public Class<? extends MutableParty> partyImplementationClass() {
        return DefaultParty.class;
    }

    @Override
    public MutableParty newParty() {
        return new DefaultParty();
    }

    @Override
    public Class<? extends MutableCreature> creatureImplementationClass() {
        return DefaultCreature.class;
    }

    @Override
    public MutableCreature newCreature() {
        return new DefaultCreature();
    }

    @Override
    public Class<? extends MutableAttack> attackImplementationClass() {
        return DefaultAttack.class;
    }

    @Override
    public MutableAttack newAttack() {
        return new DefaultAttack();
    }

    @Override
    public MutableAttackActionType newAttackActionType() {
        return new DefaultAttackActionType();
    }
}
