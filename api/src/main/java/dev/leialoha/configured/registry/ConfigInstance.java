package dev.leialoha.configured.registry;

import java.io.File;
import java.lang.reflect.Field;

import dev.leialoha.configured.core.BaseConfig;
import dev.leialoha.configured.annotations.Config;
import dev.leialoha.configured.annotations.ConfigAnnotation;

class ConfigInstance<T extends BaseConfig> {

    final T config;
    final File file;

    // Attributes
    final Config header;
    final Field[] properties;
    final ConfigAnnotation[] extras;

    public ConfigInstance(T config) {
        this.config = config;
        this.file = ConfigHandler.getConfigFile(config);

        this.header = ConfigHandler.getConfigAnnotation(config);
        this.properties = ConfigHandler.getConfigFields(config);
        this.extras = ConfigHandler.getConfigExtras(config);

        // Run validations
        ConfigHandler.validateConfigName(this.header.name());
    }

    
    public void load() {

    }

}
