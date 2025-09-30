package dev.leialoha.bettergamerules.configs.exceptions;

public class InvalidConfigNameException extends RuntimeException {

    public InvalidConfigNameException(String name) {
        super("Invalid config name '" + name + "'. It must follow the path naming convention: [a-z0-9_/]+, no trailing '/'");
    }

    public InvalidConfigNameException() {
        super("Invalid config name: expected non-null String, got null");
    }

}
