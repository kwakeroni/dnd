package active.engine.internal.creature;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import active.model.cat.Named;
import active.model.creature.Creature;
import active.model.creature.Party;
import static java.util.Comparator.*;

public class DefaultParty implements Party {
    
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


    public String toString(){
        return "Party["+name+"]{" + this.members + "}";
    }
}
