package dev.leialoha.configured.utilities;

import java.util.function.BiConsumer;

public class ObservableField<T> {
    private T value;
    private BiConsumer<T, T> listener;

    public ObservableField(T initial) {
        this.value = initial;
    }

    public void set(T newVal) {
        T old = value;
        value = newVal;
        if (listener != null) listener.accept(old, newVal);
    }

    public T get() {
        return value;
    }

    public void onChange(BiConsumer<T, T> listener) {
        this.listener = listener;
    }

}
