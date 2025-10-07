package dev.leialoha.configured.values;

public class EnumConfigValue<T extends Enum<T>> extends ConfigValue<String> {

    private T actualValue;
    private T actualDefault;

    public EnumConfigValue(T initial) {
        super(initial.name());

        this.actualValue = initial;
        this.actualDefault = initial;
    }

    public EnumConfigValue(T initial, T defaultValue) {
        super(initial.name(), initial.name());

        this.actualValue = initial;
        this.actualDefault = defaultValue;
    }

    @Override
    public String get() {
        return actualValue.name();
    }

    @Override
    public void set(String newVal) {
        try {
            this.actualValue = Enum.valueOf(actualDefault.getDeclaringClass(), newVal);
            super.set(this.actualValue.name());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return;
        }
    }

    public void set(T newVal) {
        super.set(newVal.name());
        this.actualValue = newVal;
    }

    public T getActualValue() {
        return actualValue;
    }

    @Override
    public String getDefault() {
        return actualDefault.name();
    }

    public T getActualDefault() {
        return actualDefault;
    }
    
}
