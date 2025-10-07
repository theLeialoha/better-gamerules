package dev.leialoha.configured.exceptions;

import dev.leialoha.configured.core.ConfigBase2;

public class MissingConfigException extends RuntimeException {

    public MissingConfigException(ConfigBase2 config) {
        this(config.getClass());
    }

    public MissingConfigException(Class<? extends ConfigBase2> config) {
        super(config.getName() + " hasn't been initalized yet.");
    }

}