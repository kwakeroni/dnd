package be.kwakeroni.dnd.io.test.model;

import be.kwakeroni.dnd.io.model.MutableParty;
import be.kwakeroni.dnd.model.creature.Creature;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class TestParty implements MutableParty {

    private List<Creature> creatures = new ArrayList<>();
    private String name;

    TestParty(TestModel model){
        // avoid instantiation by reflection
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public void add(Creature creature) {
        this.creatures.add(creature);
    }

    @Override
    public Stream<Creature> members() {
        return this.creatures.stream();
    }
}
