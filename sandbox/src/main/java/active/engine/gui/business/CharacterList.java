package active.engine.gui.business;

import active.model.fight.event.FightData;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface CharacterList {

    default void initBehaviour(FightData data){
        data.participants().onChanged(this::updateParticipantData);
        data.turn().onChanged(this::updateTurn);
        updateParticipantData();
    }

    void updateParticipantData();
    void updateTurn();

}
