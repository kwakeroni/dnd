package be.kwakeroni.dnd.io.test.model;

import be.kwakeroni.dnd.io.model.ModelImplementation;
import be.kwakeroni.dnd.io.model.MutableAttack;
import be.kwakeroni.dnd.io.model.MutableAttackActionType;
import be.kwakeroni.dnd.io.model.MutableCreature;
import be.kwakeroni.dnd.io.model.MutableFight;
import be.kwakeroni.dnd.io.model.MutableParticipant;
import be.kwakeroni.dnd.io.model.MutableParty;

public class TestModel implements ModelImplementation {

    @Override
    public Class<? extends MutableFight> fightImplementationClass() {
        return TestFight.class;
    }

    @Override
    public MutableFight newFight() {
        return new TestFight(this);
    }

    @Override
    public Class<? extends MutableParticipant> participantImplementationClass() {
        return TestParticipant.class;
    }

    @Override
    public MutableParticipant newParticipant() {
        return new TestParticipant(this);
    }

    @Override
    public Class<? extends MutableParty> partyImplementationClass() {
        return TestParty.class;
    }

    @Override
    public MutableParty newParty() {
        return new TestParty(this);
    }

    @Override
    public Class<? extends MutableCreature> creatureImplementationClass() {
        return TestCreature.class;
    }

    @Override
    public MutableCreature newCreature() {
        return new TestCreature(this);
    }

    @Override
    public Class<? extends MutableAttack> attackImplementationClass() {
        return TestAttack.class;
    }

    @Override
    public MutableAttack newAttack() {
        return new TestAttack(this);
    }

    @Override
    public MutableAttackActionType newAttackActionType() {
        return new TestAttackActionType();
    }
}
