package be.kwakeroni.dnd.engine.api;

import be.kwakeroni.dnd.model.actor.Actor;
import be.kwakeroni.dnd.model.effect.Damage;
import be.kwakeroni.dnd.model.fight.Participant;
import be.kwakeroni.dnd.model.target.Hittable;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface InteractionHandler {

   void withParticipant(Consumer<Participant> action);

   void withAttackData(InteractiveAttackData attackData, Consumer<? super InteractiveAttackData> action);

   public static interface InteractiveAttackData {
      public Actor getActor();
      public Hittable getTarget();
      public void addHit(Hittable target, Damage dmg);
      public void execute();
   }
}
