package active.engine.util;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Maarten Van Puymbroeck
 */
public class HierarchicalClassMap<K, V> {

    private static final Comparator<Class<?>> CLASS_COMPARATOR = new ClassComparator();

    private final Class<K> superClass;
    private final SortedMap<Class<? extends K>, V> map = new TreeMap<>(CLASS_COMPARATOR);

    public HierarchicalClassMap(Class<K> superClass){
        this.superClass = superClass;
    }

    public void put(Class<? extends K> key, V value) {
        map.put(key, value);
    }

    public Optional<V> getClosest(Class<? extends K> type){
        return map.keySet().stream()
                           .filter( key -> key.isAssignableFrom(type))
                           .findFirst()
                           .map( key -> map.get(key) );
    }

    public Optional<V> getForInstance(K instance){
        return getClosest(instance.getClass().asSubclass(superClass));
    }

    private static class ClassComparator implements Comparator<Class<?>> {
        @Override
        public int compare(Class<?> o1, Class<?> o2) {
            if (o1 == o2){
                return 0;
            } else if (o1.isAssignableFrom(o2)){
                    // superclass to the back
                return 1;
            } else if (o2.isAssignableFrom(o1)){
                    // subclass to the front
                return -1;
            }

            return o1.getName().compareTo(o2.getName());


        }
    }

}
