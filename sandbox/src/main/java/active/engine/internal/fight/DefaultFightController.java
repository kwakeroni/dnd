package active.engine.internal.fight;

import active.model.fight.Fight;
import active.model.fight.FightController;
import active.model.fight.Round;
import active.model.fight.Turn;

/**
 * @author Maarten Van Puymbroeck
 */
public class DefaultFightController implements FightController {

    private DefaultFight fight;

    public DefaultFightController(DefaultFight fight) {
        this.fight = fight;
    }

    @Override
    public Fight getState() {
        return fight;
    }

    @Override
    public DefaultRound startRound() {
        return fight.startRound();
    }

    @Override
    public void endRound() {
        fight.endRound();
    }

    @Override
    public Turn startTurn() {
        return fight.getCurrentRound().get().startTurn();
    }

    @Override
    public void endTurn() {
        fight.getCurrentRound().get().endTurn();
    }

    public void nextTurn(){

        DefaultRound round = fight.getCurrentRound().orElseGet(() -> fight.startRound());

        if (round.getCurrentTurn().isPresent()){
            round.endTurn();
        }

        if (round.isFinished()){
            endRound();
            round = startRound();
        }

        round.startTurn();

    }
    
    
}
