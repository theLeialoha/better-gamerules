package dev.leialoha.bettergamerules.configs.exceptions;

public class DuplicateConfigException extends RuntimeException {

    public DuplicateConfigException(String namespace, String name) {
        super("Config already registered with key: " + namespace + ":" + name);
    }

}
