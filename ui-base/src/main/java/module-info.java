module be.kwakeroni.dnd.ui.base {

    requires be.kwakeroni.dnd.engine.api;
    requires be.kwakeroni.dnd.model;
    requires be.kwakeroni.dnd.util.function;
    requires be.kwakeroni.dnd.event;

    exports be.kwakeroni.dnd.ui.base;
    exports be.kwakeroni.dnd.ui.base.action;
    exports be.kwakeroni.dnd.ui.base.command;
    exports be.kwakeroni.dnd.ui.base.config;
    exports be.kwakeroni.dnd.ui.base.fight;
    exports be.kwakeroni.dnd.ui.base.model;

}