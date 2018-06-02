module be.kwakeroni.dnd.io {

    requires be.kwakeroni.dnd.util.function;
    requires be.kwakeroni.dnd.model;
    requires xstream;
    requires org.ogce.xpp3.clean;

    exports be.kwakeroni.dnd.io;
    exports be.kwakeroni.dnd.io.model;
    exports be.kwakeroni.dnd.io.xml;

    uses be.kwakeroni.dnd.io.model.ModelImplementation;
}