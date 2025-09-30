package dev.leialoha.bettergamerules.configs.generic;

import dev.leialoha.bettergamerules.utilities.ObservableField;

public class ConfigValue<T> extends ObservableField<T> {

    private final T defaultValue;

    public ConfigValue(T initial) {
        super(initial);
        this.defaultValue = initial;
    }

    public ConfigValue(T initial, T defaultValue) {
        super(initial);
        this.defaultValue = defaultValue;
    }

    public T getDefault() {
        return defaultValue;
    }

    public void resetToDefault() {
        set(defaultValue);
    }

}
