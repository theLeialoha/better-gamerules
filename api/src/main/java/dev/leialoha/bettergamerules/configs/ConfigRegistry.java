package dev.leialoha.bettergamerules.configs;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import dev.leialoha.bettergamerules.configs.exceptions.DuplicateNamespaceException;

public final class ConfigRegistry {

    protected static final Map<String, Map<String, ConfigBase2>> registry = new HashMap<>();
    protected static final Map<String, NamespaceView> namespaces = new HashMap<>();

    public static NamespaceView namespace(String namespace) {
        if (namespaces.containsKey(namespace))
            throw new DuplicateNamespaceException(namespace);

        NamespaceView view = new NamespaceView(namespace);
        namespaces.put(namespace, view);
        return view;
    }

    protected static NamespaceView getNamespace(ConfigBase2 config) {
        for (Entry<String, Map<String, ConfigBase2>> nsRegistry : registry.entrySet()) {
            String namespaceKey = nsRegistry.getKey();
            if (nsRegistry.getValue().containsValue(config))
                return namespaces.get(namespaceKey);
        }

        return null;
    }

    protected static File getFolder() {
        // TODO: Let's not hard code this here
        return new File("configs");
    }

}
