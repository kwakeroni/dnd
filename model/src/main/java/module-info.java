module be.kwakeroni.dnd.model {

    requires transitive be.kwakeroni.dnd.type;
    requires transitive be.kwakeroni.dnd.event;

    exports be.kwakeroni.dnd.model.actor;
    exports be.kwakeroni.dnd.model.creature;
    exports be.kwakeroni.dnd.model.creature.event;
    exports be.kwakeroni.dnd.model.creature.stats;
    exports be.kwakeroni.dnd.model.effect;
    exports be.kwakeroni.dnd.model.fight;
    exports be.kwakeroni.dnd.model.fight.event;
    exports be.kwakeroni.dnd.model.target;

}