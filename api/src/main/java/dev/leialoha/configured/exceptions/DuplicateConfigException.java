package dev.leialoha.configured.exceptions;

import dev.leialoha.configured.registry.ConfigIdentifier;

public class DuplicateConfigException extends RuntimeException {

    public DuplicateConfigException(String namespace, String name) {
        super("Config already registered with key: " + namespace + ":" + name);
    }

    public DuplicateConfigException(ConfigIdentifier identifier) {
        this(identifier.namespace(), identifier.path());
    }

}
