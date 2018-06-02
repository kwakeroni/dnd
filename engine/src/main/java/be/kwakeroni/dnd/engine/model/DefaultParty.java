package be.kwakeroni.dnd.engine.model;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import be.kwakeroni.dnd.io.model.MutableParty;
import be.kwakeroni.dnd.model.creature.Creature;
import be.kwakeroni.dnd.model.creature.Party;
import static java.util.Comparator.*;

public class DefaultParty implements Party, MutableParty {
    
    private String name;
    private final Set<Creature> members;

    public DefaultParty(){
        this("unknown");
    }

    public DefaultParty(String name){
        this.members = new TreeSet<>(comparing(Creature::getName));
        this.name = name;

    }
    
    public void add(Creature creature){
        this.members.add(creature);
    }

    @Override
    public Stream<Creature> members() {
        return this.members.stream();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return "Party["+name+"]{" + this.members + "}";
    }
}
