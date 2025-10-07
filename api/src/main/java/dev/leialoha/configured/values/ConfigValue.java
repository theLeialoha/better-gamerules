package dev.leialoha.configured.values;

import dev.leialoha.configured.annotations.ConfigEntry;
import dev.leialoha.configured.annotations.Requirement;
import dev.leialoha.configured.utilities.ObservableField;

@Requirement(classType = ConfigEntry.class)
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
