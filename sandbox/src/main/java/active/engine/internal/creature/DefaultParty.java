package active.engine.internal.creature;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import active.model.creature.Creature;
import active.model.creature.Party;
import static java.util.Comparator.*;

public class DefaultParty implements Party {
    
    private final Set<Creature> creatures;
    
    public DefaultParty(){
        this.creatures = new TreeSet<>(comparing(Creature::getName));
    }
    
    public void add(Creature creature){
        this.creatures.add(creature);
    }

    @Override
    public Stream<Creature> members() {
        return this.creatures.stream();
    }

}
