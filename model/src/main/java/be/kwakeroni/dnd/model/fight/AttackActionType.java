package be.kwakeroni.dnd.model.fight;

import be.kwakeroni.dnd.model.actor.ActionType;
import be.kwakeroni.dnd.model.effect.Attack;

import java.util.List;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface AttackActionType extends ActionType {

    public List<Attack<?>> getAttacks();

}
