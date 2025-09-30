package dev.leialoha.bettergamerules.configs;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import dev.leialoha.bettergamerules.configs.annotations.Config;
import dev.leialoha.bettergamerules.configs.exceptions.DuplicateConfigException;

public final class NamespaceView {
    
    private final String namespace;
    private final File parentFolder;

    protected NamespaceView(String namespace) {
        this.namespace = namespace;
        File folder = ConfigRegistry.getFolder();
        this.parentFolder = new File(folder, namespace);
    }

    public void register(ConfigBase2 config) {
        Config configAnnotation = ConfigHandler.getConfigAnnotation(config);
        String configName = configAnnotation.name();
        ConfigHandler.validateConfigName(configName);

        Map<String, ConfigBase2> namespaceRegistry = ConfigRegistry.registry
            .computeIfAbsent(namespace, ns -> new HashMap<>());

        if (namespaceRegistry.containsKey(configName))
            throw new DuplicateConfigException(namespace, configName);

        namespaceRegistry.put(configName, config);
    }

    public ConfigBase2 get(String path) {
        ConfigHandler.validateConfigName(path);

        return ConfigRegistry.registry
            .getOrDefault(namespace, Map.of())
            .get(path);
    }

    public void loadAll() {
        ConfigRegistry.registry
            .getOrDefault(namespace, Map.of())
            .values()
            .forEach(ConfigBase2::loadConfig);
    }

    public void saveAll() {
        ConfigRegistry.registry
            .getOrDefault(namespace, Map.of())
            .values()
            .forEach(ConfigBase2::saveConfig);
    }

    protected File getConfigFile(String configFileName) {
        ConfigHandler.validateConfigName(configFileName);
        return new File(parentFolder, configFileName);
    }
}
