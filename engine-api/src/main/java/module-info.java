module be.kwakeroni.dnd.engine.api {

    requires be.kwakeroni.dnd.model;
    requires be.kwakeroni.dnd.util.function;

    exports be.kwakeroni.dnd.engine.api;
    exports be.kwakeroni.dnd.engine.api.command;
    exports be.kwakeroni.dnd.engine.api.command.fight;
    exports be.kwakeroni.dnd.engine.api.command.io;

}