package be.kwakeroni.dnd.type.collection;

import java.util.Optional;

public final class Box<T> {
    private T value;

    public Box() {
        this(null);
    }

    public Box(T value) {
        this.value = null;
    }

    public T get() {
        return value;
    }

    public Optional<T> optional(){
        return Optional.ofNullable(get());
    }

    public void set(T value) {
        this.value = value;
    }

    public T capture(T value) {
        set(value);
        return value;
    }
}
