package active.engine.gui;

import active.model.cat.Hittable;
import active.model.fight.Participant;
import active.model.fight.command.Attack;

import java.awt.*;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface InteractionHandler {

   void withParticipant(Consumer<Participant> action);

   void requestAttackData(Attack.AttackData attack);
}
