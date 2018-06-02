module be.kwakeroni.dnd.engine {

    requires be.kwakeroni.dnd.engine.api;
    requires be.kwakeroni.dnd.io;
    requires be.kwakeroni.dnd.model;
    requires be.kwakeroni.dnd.ui.swing;

    provides be.kwakeroni.dnd.io.model.ModelImplementation with be.kwakeroni.dnd.engine.model.DefaultModelImplementation;

    opens be.kwakeroni.dnd.engine.model to be.kwakeroni.dnd.io;
    opens be.kwakeroni.dnd.engine.model.fight to be.kwakeroni.dnd.io;
}