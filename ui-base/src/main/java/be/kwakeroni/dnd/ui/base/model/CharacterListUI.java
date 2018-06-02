package be.kwakeroni.dnd.ui.base.model;

import be.kwakeroni.dnd.model.fight.event.FightData;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface CharacterListUI {

    default void initBehaviour(FightData data){
        data.participants().onChanged(this::updateParticipantData);
        data.turn().onChanged(this::updateTurn);
        updateParticipantData();
    }

    void updateParticipantData();
    void updateTurn();

}
