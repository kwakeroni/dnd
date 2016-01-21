package active.engine.channel;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Maarten Van Puymbroeck
 */
public class SwitchOp<T> implements Channel.Switch<T>, Consumer<T> {

    private LinkedHashMap<Predicate<? super T>, Consumer<? super T>> cases;
    private Consumer<? super T> otherwise = DO_NOTHING;
    private Consumer<? super T> afterwards = DO_NOTHING;

    public SwitchOp(){
        this.cases = new LinkedHashMap<>();
    }

    @Override
    public Channel<T> afterwards() {
        ChannelEntryOp<T> entry = new ChannelEntryOp<>();
        this.afterwards = entry;
        return entry;
    }

    @Override
    public Channel<T> when(Predicate<? super T> predicate) {
        ChannelEntryOp<T> entry = new ChannelEntryOp<>();
        this.cases.put(predicate, entry);
        return entry;
    }

    @Override
    public Channel<T> otherwise() {
        ChannelEntryOp<T> entry = new ChannelEntryOp<>();
        this.otherwise = entry;
        return entry;
    }

    @Override
    public void accept(T t) {
        getApplicableCase(t).accept(t);
        this.afterwards.accept(t);
    }

    private Consumer<? super T> getApplicableCase(T t){
        for (Map.Entry<Predicate<? super T>, Consumer<? super T>> entry : cases.entrySet()){
            if (entry.getKey().test(t)){
                return entry.getValue();
            }
        }
        return otherwise;
    }

    private static final Consumer<Object> DO_NOTHING = new Consumer<Object>() {
        @Override
        public void accept(Object o) {

        }
    };
}
