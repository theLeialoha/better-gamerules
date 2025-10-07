package dev.leialoha.configured.registry;

import java.io.File;

import dev.leialoha.configured.core.BaseConfig;
import dev.leialoha.configured.exceptions.DuplicateConfigException;

public final class ConfigNamespace {

    //TODO: protected String name;
    //TODO: protected List<BaseConfig> configs;
    
    private final String namespace;
    private final File parentFolder;

    // TODO: void registerConfig(BaseConfig config)
    //   - Adds config to this namespace
    //   - Calls registry.add(config) to track globally

    // TODO: BaseConfig getConfig(String key)
    //   - Retrieves from local list
    // TODO: List<BaseConfig> getAllConfigs()

    protected ConfigNamespace(String namespace) {
        this.namespace = namespace;
        File folder = ConfigRegistry.getFolder();
        this.parentFolder = new File(folder, namespace);
    }

    public <T extends BaseConfig> void register(T config) {
        ConfigInstance<T> instance = new ConfigInstance<>(config);
        ConfigIdentifier identifier = new ConfigIdentifier(namespace, instance.header.name());

        if (ConfigRegistry.registry.containsKey(identifier))
            throw new DuplicateConfigException(identifier);

        ConfigRegistry.registry.put(identifier, instance);
    }

    public <T extends BaseConfig> ConfigInstance<T> get(String path) {
        ConfigHandler.validateConfigName(path);

        ConfigIdentifier identifier = new ConfigIdentifier(namespace, path);
        return (ConfigInstance<T>) ConfigRegistry.registry.get(identifier);
    }

    protected File getConfigFile(String fileName) {
        ConfigHandler.validateConfigName(fileName);
        return new File(parentFolder, fileName);
    }
}
