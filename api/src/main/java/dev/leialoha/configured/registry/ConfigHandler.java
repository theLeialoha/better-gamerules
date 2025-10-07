package dev.leialoha.configured.registry;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import dev.leialoha.configured.annotations.Config;
import dev.leialoha.configured.annotations.ConfigAnnotation;
import dev.leialoha.configured.annotations.ConfigEntry;
import dev.leialoha.configured.core.ConfigBase2;
import dev.leialoha.configured.exceptions.InvalidConfigNameException;
import dev.leialoha.configured.exceptions.MissingAnnotationException;
import dev.leialoha.configured.utilities.Reflection;
import dev.leialoha.configured.values.ConfigValue;

class ConfigHandler {

    private static final HashMap<ConfigBase2, Config> CONFIG_ANNOTATIONS = new HashMap<>();

    private static final Pattern CONFIG_NAME_VALIDATION = Pattern.compile("^(?:[\\w]+(?!\\/$)\\/?)+$", Pattern.CASE_INSENSITIVE);

    public static Config getConfigAnnotation(ConfigBase2 config) {
        Config configAnnotation = Reflection.getAnnotation(config, Config.class);
        if (configAnnotation == null)
            throw new MissingAnnotationException(config.getClass(), Config.class);

        CONFIG_ANNOTATIONS.putIfAbsent(config, configAnnotation);
        return configAnnotation;
    }

    public static File getConfigFile(ConfigBase2 config) {
        ConfigIdentifier identifier = ConfigRegistry.getConfigIdentifier(config);
        if (identifier == null) return null;
        return identifier.getConfigFile();
    }

    protected static void validateConfigName(String configName) {
        if (configName == null) throw new InvalidConfigNameException();
        if (configName.isEmpty() || configName.isBlank()) throw new InvalidConfigNameException(configName);
        if (!CONFIG_NAME_VALIDATION.matcher(configName).matches()) throw new InvalidConfigNameException(configName);
        // No issues
    }

    protected static boolean isConfigEntry(Field field) {
        Reflection.makeAccessible(field);

        return !Modifier.isTransient(field.getModifiers())
            && Reflection.isType(field, ConfigValue.class)
            && Reflection.hasAnnotation(field, ConfigEntry.class);
    }

    protected static Field[] getConfigFields(ConfigBase2 config) {
        Field[] fields = config.getClass().getDeclaredFields();

        return Stream.of(fields)
            .filter(ConfigHandler::isConfigEntry)
            .toArray(Field[]::new);

            // .map(Reflection::makeAccessible);
    }

    protected static ConfigAnnotation[] fetchAnnotation(Field field) {
        Reflection.makeAccessible(field);

        return field.getAnnotationsByType(ConfigAnnotation.class);
    }

    protected static ConfigAnnotation[] getConfigExtras(ConfigBase2 config) {
        Field[] fields = config.getClass().getDeclaredFields();

        return Stream.of(fields)
            .map(ConfigHandler::fetchAnnotation)
            .filter(Reflection::notNull)
            .flatMap(annotations -> Stream.of(annotations))
            .toArray(ConfigAnnotation[]::new);
    }


}
