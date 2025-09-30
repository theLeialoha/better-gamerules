package dev.leialoha.bettergamerules.configs;


import java.io.File;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import dev.leialoha.bettergamerules.configs.annotations.Config;
import dev.leialoha.bettergamerules.configs.annotations.ConfigEntry;
import dev.leialoha.bettergamerules.configs.exceptions.InvalidConfigNameException;
import dev.leialoha.bettergamerules.configs.exceptions.MissingAnnotationException;
import dev.leialoha.bettergamerules.configs.exceptions.MissingConfigException;
import dev.leialoha.bettergamerules.configs.generic.ConfigValue;
import dev.leialoha.bettergamerules.utilities.Reflection;

class ConfigHandler {

    private static final HashMap<ConfigBase2, Config> CONFIG_ANNOTATIONS = new HashMap<>();

    private static final Pattern CONFIG_NAME_VALIDATION = Pattern.compile("^(?:[\\w]+(?!\\/$)\\/?)+$", Pattern.CASE_INSENSITIVE);

    public static Config getConfigAnnotation(ConfigBase2 config) {
        try {
            Config configAnnotation = Reflection.getAnnotation(config, Config.class);
            CONFIG_ANNOTATIONS.putIfAbsent(config, configAnnotation);
            return configAnnotation;
        } catch (IllegalClassFormatException e) {
            throw new MissingAnnotationException(config.getClass(), Config.class);
        }
    }

    public static File getConfigFile(ConfigBase2 config) {
        if (!CONFIG_ANNOTATIONS.containsKey(config))
            throw new MissingConfigException(config);

        String configName = CONFIG_ANNOTATIONS.get(config).name();
        NamespaceView namespace = ConfigRegistry.getNamespace(config);

        if (namespace == null) return null;
        return namespace.getConfigFile(configName);
    }

    protected static void validateConfigName(String configName) {
        if (configName == null) throw new InvalidConfigNameException();
        if (configName.isEmpty() || configName.isBlank()) throw new InvalidConfigNameException(configName);
        if (!CONFIG_NAME_VALIDATION.matcher(configName).matches()) throw new InvalidConfigNameException(configName);
        // No issues
    }

    // TODO: Update this method or split it into seperate methods
    // I don't know if I like this setup
    // I might change the reflection class
    private static boolean isConfigEntry(Field field) {
        try {
            Reflection.validateAnnotation(field, ConfigEntry.class);
            Reflection.validateType(field, ConfigValue.class);
        } catch (IllegalClassFormatException e) {
            return false;
        }

        return true;
    }

    private static boolean isValidField(Field field) {
        return !Modifier.isTransient(field.getModifiers()) && isConfigEntry(field);
    }



    protected static Field[] getConfigValues(ConfigBase2 config) {
        Field[] fields = config.getClass().getDeclaredFields();

        return Stream.of(fields)
            .filter(ConfigHandler::isValidField)
            .toArray(Field[]::new);

            // .map(Reflection::makeAccessible);
    }


}
