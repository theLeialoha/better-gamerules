package dev.leialoha.configured.exceptions;

public class DuplicateNamespaceException extends RuntimeException {
    
    public DuplicateNamespaceException(String namespace) {
        super("Namespace already registered with key: " + namespace);
    }

}
