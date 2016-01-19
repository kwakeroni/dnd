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
    
    private final String name;
    private final Set<Creature> members;
    
    public DefaultParty(String name){
        this.name = name;
        this.members = new TreeSet<>(comparing(Creature::getName));
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

    
}
