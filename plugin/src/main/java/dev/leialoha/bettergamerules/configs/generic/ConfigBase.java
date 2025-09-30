package dev.leialoha.bettergamerules.configs.generic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import dev.leialoha.bettergamerules.BetterGamerulesPlugin;
import dev.leialoha.bettergamerules.configs.annotations.ConfigEntry;
import dev.leialoha.bettergamerules.configs.annotations.EmptyEnum;
import dev.leialoha.bettergamerules.utilities.Reflection;
import dev.leialoha.bettergamerules.configs.GlobalPluginConfig;
import dev.leialoha.bettergamerules.configs.annotations.Config;
import dev.leialoha.bettergamerules.configs.annotations.ConfigAnnotation;

public abstract class ConfigBase {

    private static final Logger LOGGER;
    private static final Pattern DOT_PATTERN;
    private static final Pattern EMPTY_COMMENTS_PATTERN;
    private YamlConfiguration configuration;

    private static final GlobalPluginConfig GLOBAL_CONFIG = GlobalPluginConfig.getConfig();

    static {
        LOGGER = BetterGamerulesPlugin.getPlugin().getLogger();
        DOT_PATTERN = Pattern.compile("\\.");
        EMPTY_COMMENTS_PATTERN = Pattern.compile(" *# ?\\u200D$", Pattern.MULTILINE);
    }

    protected File getPluginConfigFile(String name) {
        Plugin plugin = BetterGamerulesPlugin.getPlugin();
        File dataFolder = plugin.getDataFolder();
        File configFolder = new File(dataFolder, "configs");
        return new File(configFolder, name);
    }

    public void loadConfig() throws IllegalClassFormatException {
        Reflection.validateAnnotation(this, Config.class);
        Config configAnnotation = Reflection.getAnnotation(this, Config.class);

        File configFile = getConfigFile();

        if (!configFile.exists()) {
            regenerateConfig(configFile);
            return;
        }

        // Load existing config
        configuration = YamlConfiguration.loadConfiguration(configFile);
        int configVersion = configuration.getInt("version", -1); // If version is -1, reset config
        int expectedVersion = configAnnotation.version(); // Get expected version from annotation

        if (configVersion == -1) {
            // Reset config
            LOGGER.info("Resetting config " + configFile.getName() + " to default values");
            regenerateConfig(configFile);
        } else if (configVersion > expectedVersion) {
            // Config is from a newer version of the plugin
            // Warn the user, but don't modify the config
            LOGGER.warning("Config version mismatch in " + configFile.getName() + ": expected " + expectedVersion + ", found " + configVersion);
            LOGGER.warning("This config is from a newer version of this plugin");
        } else if (configVersion < expectedVersion) {
            // Config is from an older version of the plugin
            // Update config to new version, preserving old values
            LOGGER.info("Config version mismatch in " + configFile.getName() + ": expected " + expectedVersion + ", found " + configVersion);
            LOGGER.info("Updating " + configFile.getName() + " to version " + expectedVersion);
            
            // Update config to with previous config values and new defaults
            YamlConfiguration expectedConfiguration = new YamlConfiguration();
            loadDefaults(expectedConfiguration);

            for (String key : configuration.getKeys(true)) {
                // Skip version key, it has already been set
                if (key.equals("version")) continue;

                Object oldValue = configuration.get(key);
                if (oldValue == null) continue;

                // We don't want to copy sections, only actual values
                if (MemorySection.class.isAssignableFrom(oldValue.getClass())) continue;

                // If the key doesn't exist in the new config, skip it (removing deprecated keys)
                if (!expectedConfiguration.contains(key)) {
                    LOGGER.info("Removing deprecated key " + key + " in " + configFile.getName());
                    continue;
                }

                expectedConfiguration.set(key, oldValue);
            }

            // Update the config to the new config
            configuration = expectedConfiguration;

            try {
                saveConfig();
            } catch (IOException e) {
                LOGGER.warning("Failed to save updated config to " + configFile.getName());
                if (GLOBAL_CONFIG.ShowStackTraces.get()) e.printStackTrace();
            }
        }

        loadConfigValues();
        LOGGER.info("Loaded " + configFile.getName() + " successfully");
    }

    private void loadConfigValues() {
        if (configuration == null)
            throw new NullPointerException("Configuration hasn't been initalized");

        Class<? extends ConfigBase> clazz = this.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            try {
                int modifiers = field.getModifiers();
                if (Modifier.isTransient(modifiers)) continue;
                Reflection.makeAccessible(field);

                ConfigEntry configEntryAnnotation = Reflection.getAnnotation(field, ConfigEntry.class);

                String configKey = configEntryAnnotation.key();

                // Let's not throw an exception if the key doesn't exist, just skip it
                if (!configuration.contains(configKey)) {
                    if (GLOBAL_CONFIG.EnableDebugLogs.get())
                        LOGGER.info("Key " + configKey + " not found in config " + clazz.getName() + ", skipping...");
                    continue;
                }

                loadConfigEntry(configKey, field);
            } catch (IllegalArgumentException | IllegalClassFormatException e) {
                LOGGER.warning("Couldn't process " + field.getName() + " in " + clazz.getName() + ", skipping...");
                if (GLOBAL_CONFIG.ShowStackTraces.get()) e.printStackTrace();
            }
        }
    }

    private <T> void loadConfigEntry(String configKey, Field field) {
        ConfigValue<T> configValue = Reflection.getField(field, this);
        validateConfigEntry(configValue);

        if (configValue instanceof EnumConfigValue<?> enumConfigValue) {
            String value = configuration.getString(configKey, null);
            if (value != null) enumConfigValue.set(value);
            return;
        }

        Class<?> parentClass = field.getDeclaringClass();
        Type configType = field.getGenericType();

        if (!(configType instanceof ParameterizedType parameterizedType))
            throw new IllegalArgumentException("Config value field " + field.getName() + " in " + parentClass.getName() + " is not parameterized");

        // No need to check if it's a GenericConfigValue, we already did that
        Type rawType = parameterizedType.getActualTypeArguments()[0];

        if (rawType instanceof Class<?> clazz) {
            if (clazz.equals(String.class)) {
                String value = configuration.getString(configKey, null);
                ((ConfigValue<String>) configValue).set(value);
            } else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
                Integer value = configuration.getInt(configKey, 0);
                ((ConfigValue<Integer>) configValue).set(value);
            } else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
                Boolean value = configuration.getBoolean(configKey, false);
                ((ConfigValue<Boolean>) configValue).set(value);
            } else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
                Double value = configuration.getDouble(configKey, 0.0D);
                ((ConfigValue<Double>) configValue).set(value);
            } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
                Long value = configuration.getLong(configKey, 0L);
                ((ConfigValue<Long>) configValue).set(value);
            } else if (clazz.equals(OfflinePlayer.class)) {
                OfflinePlayer value = configuration.getOfflinePlayer(configKey, null);
                ((ConfigValue<OfflinePlayer>) configValue).set(value);
            } else if (clazz.equals(ItemStack.class)) {
                ItemStack value = configuration.getItemStack(configKey, null);
                ((ConfigValue<ItemStack>) configValue).set(value);
            } else if (clazz.equals(Color.class)) {
                Color value = configuration.getColor(configKey, null);
                ((ConfigValue<Color>) configValue).set(value);
            } else if (clazz.equals(Location.class)) {
                Location value = configuration.getLocation(configKey, null);
                ((ConfigValue<Location>) configValue).set(value);
            } else if (clazz.equals(Vector.class)) {
                Vector value = configuration.getVector(configKey, null);
                ((ConfigValue<Vector>) configValue).set(value);
            } else if (ConfigurationSerializable.class.isAssignableFrom(clazz)) {
                // If the class implements ConfigurationSerializable, we can use getSerializable
                Class<ConfigurationSerializable> serializableClass = (Class<ConfigurationSerializable>) clazz;
                ConfigurationSerializable value = configuration.getSerializable(configKey, serializableClass);
                ((ConfigValue<ConfigurationSerializable>) configValue).set(value);
            } else {
                throw new IllegalArgumentException("Unsupported config value type " + clazz + " for field " + field.getName() + " in " + parentClass.getName());
            }
        }  else if (rawType instanceof ParameterizedType parameterizedRawType) {

            if (!(parameterizedRawType.getRawType() instanceof Class<?> rawClass))
                throw new IllegalArgumentException("Unsupported parameterized type " + rawType + " for field " + field.getName() + " in " + parentClass.getName());
             
            if (!List.class.isAssignableFrom(rawClass))
                throw new IllegalArgumentException("Unsupported parameterized type " + rawType + " for field " + field.getName() + " in " + parentClass.getName() + ": only List is supported, found " + rawClass);
                // We only support lists for now, so rawType must be a List

            Type paramType = parameterizedRawType.getActualTypeArguments()[0];

            if (paramType.equals(String.class)) {
                List<String> value = configuration.getStringList(configKey);
                ((ConfigValue<List<String>>) configValue).set(value);
            } else if (paramType.equals(Integer.class)) {
                List<Integer> value = configuration.getIntegerList(configKey);
                ((ConfigValue<List<Integer>>) configValue).set(value);
            } else if (paramType.equals(Boolean.class)) {
                List<Boolean> value = configuration.getBooleanList(configKey);
                ((ConfigValue<List<Boolean>>) configValue).set(value);
            } else if (paramType.equals(Double.class)) {
                List<Double> value = configuration.getDoubleList(configKey);
                ((ConfigValue<List<Double>>) configValue).set(value);
            } else if (paramType.equals(Float.class)) {
                List<Float> value = configuration.getFloatList(configKey);
                ((ConfigValue<List<Float>>) configValue).set(value);
            } else if (paramType.equals(Long.class)) {
                List<Long> value = configuration.getLongList(configKey);
                ((ConfigValue<List<Long>>) configValue).set(value);
            } else if (paramType.equals(Byte.class)) {
                List<Byte> value = configuration.getByteList(configKey);
                ((ConfigValue<List<Byte>>) configValue).set(value);
            } else if (paramType.equals(Character.class)) {
                List<Character> value = configuration.getCharacterList(configKey);
                ((ConfigValue<List<Character>>) configValue).set(value);
            } else if (paramType.equals(Short.class)) {
                List<Short> value = configuration.getShortList(configKey);
                ((ConfigValue<List<Short>>) configValue).set(value);
            } else if (paramType.equals(Map.class)) {
                List<Map<?, ?>> value = configuration.getMapList(configKey);
                ((ConfigValue<List<Map<?, ?>>>) configValue).set(value);
            } else {
                throw new IllegalArgumentException("Unsupported list parameter type " + paramType + " for field " + field.getName() + " in " + parentClass.getName());
            }
        } else {
            throw new IllegalArgumentException("Unsupported config value type " + rawType + " for field " + field.getName() + " in " + parentClass.getName());
        }

    }

    private void regenerateConfig(File configFile) {
        LOGGER.info("Generating config " + configFile.getName() + ", using default values");
        loadDefaults();
        try {
            saveConfig();
        } catch (Exception e) {
            LOGGER.warning("Failed to save defaults to " + configFile.getName());
            if (GLOBAL_CONFIG.ShowStackTraces.get()) e.printStackTrace();
        }
    }

    public void saveConfig() throws IllegalClassFormatException, IOException {
        if (configuration == null)
            throw new NullPointerException("Configuration hasn't been initalized");
        
        File configFile = getConfigFile();

        String configurationData = configuration.saveToString();
        Matcher matcher = EMPTY_COMMENTS_PATTERN.matcher(configurationData);
        configurationData = matcher.replaceAll("");

        FileOutputStream outputStream = new FileOutputStream(configFile);
        outputStream.write(configurationData.getBytes());
        outputStream.close();
    }

    private void loadDefaults() {
        configuration = new YamlConfiguration();
        loadDefaults(configuration);
    }

    private void loadDefaults(YamlConfiguration configuration) {
        HashMap<String, ConfigurationSection> subConfigurations = new HashMap<>();

        Class<? extends ConfigBase> clazz = this.getClass();
        Config configAnnotation;

        try {
            configAnnotation = Reflection.getAnnotation(clazz, Config.class);
        } catch (IllegalClassFormatException e) {
            LOGGER.warning("Invalid config class " + clazz.getName() + ", aborting...");
            if (GLOBAL_CONFIG.ShowStackTraces.get()) e.printStackTrace();
            return;
        }

        configuration.options().setHeader(List.of(configAnnotation.header()));

        configuration.set("version", configAnnotation.version());
        configuration.addDefault("version", configAnnotation.version());
        configuration.setComments("version", List.of(
            "Any commented out entries will use their default values",
            "Set the config version to 0 if you want to generate any missing entries",
            "Set the config version to -1 to reset this config",
            "",
            "Do not change this value unless you know what you're doing"
        ));

        for (Field field : clazz.getDeclaredFields()) {
            try {
                updateConfigAnnotations(configuration, subConfigurations, field);

                int modifiers = field.getModifiers();
                if (Modifier.isTransient(modifiers)) continue;
                Reflection.makeAccessible(field);

                updateConfigValue(configuration, subConfigurations, field);
            } catch (IllegalArgumentException | IllegalClassFormatException | IllegalAccessException e) {
                LOGGER.warning("Couldn't process " + field.getName() + " in " + clazz.getName() + ", skipping...");
                if (GLOBAL_CONFIG.ShowStackTraces.get()) e.printStackTrace();
            }
        }
    }

    private void updateConfigAnnotations(YamlConfiguration configuration, HashMap<String, ConfigurationSection> subConfigurations, Field field) {
        Reflection.makeAccessible(field);

        ConfigAnnotation[] configAnnotations = field.getAnnotationsByType(ConfigAnnotation.class);

        for (ConfigAnnotation configAnnotation : configAnnotations) {
            String configKey = configAnnotation.key();
            List<String> comments = new ArrayList<>(List.of(configAnnotation.comments()));
            comments.addAll(0, List.of("\u200D", ""));

            if (configKey.isBlank()) continue;

            if (!configAnnotation.enumType().equals(EmptyEnum.class)) {
                Class<? extends Enum<?>> enumType = configAnnotation.enumType();
                Enum<?>[] enumConstants = enumType.getEnumConstants();

                // Let's skip if there are no entries
                if (enumConstants.length == 0) break;

                String possibleValues = Stream.of(enumConstants).map(Enum<?>::name)
                    .collect(Collectors.joining(", "));

                int nameSize = Stream.of(enumConstants).map(Enum<?>::name)
                    .map(String::length).reduce(Integer::max).orElse(0);

                comments.add("");
                comments.add("Possible values: " + possibleValues);

                for (Enum<?> enumConstant : enumConstants) {
                    String enumName = enumConstant.name();

                    try {
                        Field enumField = Reflection.getFieldByName(enumConstant, enumConstant.name());
                        ConfigAnnotation enumAnnotation = Reflection.getAnnotation(enumField, ConfigAnnotation.class);

                        String[] enumComments = enumAnnotation.comments();
                        String namePadding = " ".repeat(nameSize - enumName.length());
                        String defaultPadding = " ".repeat(nameSize + 2);

                        for (int i = 0; i < enumComments.length; i++) {
                            if (i == 0) comments.add(namePadding + enumName + ": " + enumComments[i]);
                            else comments.add(defaultPadding + enumComments[i]);
                        }

                    } catch (IllegalArgumentException | IllegalClassFormatException e) {
                        LOGGER.warning("Couldn't process " + enumName + " in " + enumType);
                        if (GLOBAL_CONFIG.ShowStackTraces.get()) e.printStackTrace();
                    }
                }
            }

            comments.add("");

            String finalizedKey = getFinalizedKey(configKey);
            ConfigurationSection section = getConfigurationSection(configuration, subConfigurations, configKey);

            if (!section.contains(finalizedKey))
                // Let's create the section if it doesn't exist
                getConfigurationChildSection(section, subConfigurations, configKey);

            section.setComments(finalizedKey, comments);
        }
        
    }

    private String getFinalizedKey(String configKey) {
        Matcher matcher = DOT_PATTERN.matcher(configKey);
        int lastIndex = matcher.results().map(MatchResult::start).reduce(Integer::max).orElse(0);
        return configKey.substring(lastIndex);
    }

    protected <T> void updateConfigValue(YamlConfiguration configuration, HashMap<String, ConfigurationSection> subConfigurations, Field field) throws IllegalClassFormatException, IllegalAccessException {
        ConfigEntry configEntryAnnotation = Reflection.getAnnotation(field, ConfigEntry.class);
        ConfigValue<T> configValue = Reflection.getField(field, this, ConfigValue.class);

        validateConfigEntry(configValue);

        String configKey = configEntryAnnotation.key();
        List<String> comments = new ArrayList<>() {{
            this.add("\u200D");
            this.addAll(List.of(configEntryAnnotation.comments()));
        }};

        String finalizedKey = getFinalizedKey(configKey);
        ConfigurationSection section = getConfigurationSection(configuration, subConfigurations, configKey);

        Object value = configValue.get();
        Object defaultValue = configValue.getDefault();

        section.set(finalizedKey, value);
        section.addDefault(finalizedKey, defaultValue);
        section.setComments(finalizedKey, comments);
    }

    protected <T> void validateConfigEntry(ConfigValue<T> configValue) {
        if (configValue == null)
            throw new IllegalArgumentException("Config value is null");
        if (configValue.get() == null)
            throw new IllegalArgumentException("Config value is null");
        if (configValue.getDefault() == null)
            throw new IllegalArgumentException("Config default value is null");
    }

    protected ConfigurationSection getConfigurationSection(ConfigurationSection configuration, Map<String, ConfigurationSection> subConfigurations, String sectionPath) {
        ConfigurationSection section = configuration;
        Matcher matcher = DOT_PATTERN.matcher(sectionPath);
    
        List<String> subSectionPaths = matcher.results()
            .map(match -> sectionPath.substring(0, match.start())).toList();

        for (String subSectionPath : subSectionPaths)
            section = getConfigurationChildSection(section, subConfigurations, subSectionPath);

        return section;
    }

    protected ConfigurationSection getConfigurationChildSection(ConfigurationSection section, Map<String, ConfigurationSection> subConfigurations, String sectionPath) {
        return subConfigurations.computeIfAbsent(sectionPath, k -> {
            int index = k.lastIndexOf('.');
            if (index == -1) index = 0;

            String sectionName = k.substring(index);
            return section.createSection(sectionName);
        });
    }

    public File getConfigFile() throws IllegalClassFormatException {
        Config configPathAnnotation = Reflection.getAnnotation(this, Config.class);
        String configFileName = configPathAnnotation.name();
        return getPluginConfigFile(configFileName);
    }

    public <T> Supplier<T> getSelfSupplier(String fieldName) throws IllegalArgumentException {
        return getSupplier(this, fieldName);
    }

    public static <T> Supplier<T> getSupplier(ConfigBase config, String fieldName) throws IllegalArgumentException {
        Field field = Reflection.getFieldByName(config, fieldName);
        ConfigValue<T> configValue = Reflection.getField(field, config, ConfigValue.class);

        return configValue::get;
    }

}
