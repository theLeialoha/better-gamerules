package dev.leialoha.configured.registry;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import dev.leialoha.configured.core.BaseConfig;
import dev.leialoha.configured.exceptions.DuplicateNamespaceException;

public final class ConfigRegistry {

    protected static final Map<String, NamespaceView> namespaces = new HashMap<>();
    protected static final Map<ConfigIdentifier, ConfigInstance<?>> registry = new HashMap<>();

    public static NamespaceView namespace(String namespace) {
        if (namespaces.containsKey(namespace))
            throw new DuplicateNamespaceException(namespace);

            // I don't know if I want the exception or not
        return namespaces.computeIfAbsent(namespace, NamespaceView::new);
    }

    protected static ConfigIdentifier getConfigIdentifier(BaseConfig config) {
        return registry.entrySet().stream()
            .filter(entry -> entry.getValue().config.equals(config))
            .map(entry -> entry.getKey())
            .findFirst()
            .orElse(null);
    }

    protected static File getFolder() {
        // TODO: Let's not hard code this here
        return new File("configs");
    }

}
