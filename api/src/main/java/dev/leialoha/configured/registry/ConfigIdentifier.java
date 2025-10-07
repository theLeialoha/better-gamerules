package dev.leialoha.configured.registry;

import java.io.File;

public record ConfigIdentifier(String namespace, String path) {

    public NamespaceView namespaceView() {
        return ConfigRegistry.namespaces.get(namespace);
    }

    public String identifier() {
        return namespace + ":" + path;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof ConfigIdentifier other)
            return other.identifier().equals(identifier());
        return false;
    }

    public final File getConfigFile() {
        return namespaceView().getConfigFile(path);
    }

}
