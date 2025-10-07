package dev.leialoha.configured.exceptions;

import dev.leialoha.configured.core.BaseConfig;

public class MissingConfigException extends RuntimeException {

    public MissingConfigException(BaseConfig config) {
        this(config.getClass());
    }

    public MissingConfigException(Class<? extends BaseConfig> config) {
        super(config.getName() + " hasn't been initalized yet.");
    }

}