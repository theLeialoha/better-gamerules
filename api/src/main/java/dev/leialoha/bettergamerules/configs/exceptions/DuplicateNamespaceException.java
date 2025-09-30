package dev.leialoha.bettergamerules.configs.exceptions;

public class DuplicateNamespaceException extends RuntimeException {
    
    public DuplicateNamespaceException(String namespace) {
        super("Namespace already registered with key: " + namespace);
    }

}
