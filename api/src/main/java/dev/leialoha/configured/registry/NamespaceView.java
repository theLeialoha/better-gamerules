package dev.leialoha.configured.registry;

import java.io.File;

import dev.leialoha.configured.core.ConfigBase2;
import dev.leialoha.configured.exceptions.DuplicateConfigException;

public final class NamespaceView {
    
    private final String namespace;
    private final File parentFolder;

    protected NamespaceView(String namespace) {
        this.namespace = namespace;
        File folder = ConfigRegistry.getFolder();
        this.parentFolder = new File(folder, namespace);
    }

    public <T extends ConfigBase2> void register(T config) {
        ConfigInstance<T> instance = new ConfigInstance<>(config);
        ConfigIdentifier identifier = new ConfigIdentifier(namespace, instance.header.name());

        if (ConfigRegistry.registry.containsKey(identifier))
            throw new DuplicateConfigException(identifier);

        ConfigRegistry.registry.put(identifier, instance);
    }

    public <T extends ConfigBase2> ConfigInstance<T> get(String path) {
        ConfigHandler.validateConfigName(path);

        ConfigIdentifier identifier = new ConfigIdentifier(namespace, path);
        return (ConfigInstance<T>) ConfigRegistry.registry.get(identifier);
    }

    protected File getConfigFile(String fileName) {
        ConfigHandler.validateConfigName(fileName);
        return new File(parentFolder, fileName);
    }
}
