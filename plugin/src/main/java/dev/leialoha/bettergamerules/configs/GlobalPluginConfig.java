package dev.leialoha.bettergamerules.configs;

import dev.leialoha.bettergamerules.configs.annotations.ConfigEntry;
import dev.leialoha.bettergamerules.configs.generic.ConfigBase;
import dev.leialoha.bettergamerules.configs.generic.ConfigValue;

import dev.leialoha.bettergamerules.configs.annotations.Config;

@Config(
    name = "../global-config",
    version = 1,
    header = {
        "This is the global configuration file for BetterGameRules."
    }
)
public class GlobalPluginConfig extends ConfigBase {

    private transient static GlobalPluginConfig CONFIG;

    @ConfigEntry (
        key = "debug.enableDebugLogs",
        comments = { "Show debug logs in console" }
    ) public final ConfigValue<Boolean> EnableDebugLogs = new ConfigValue<>(true, false);

    @ConfigEntry (
        key = "debug.showStackTraces",
        comments = { "Show stack traces for config errors in console" }
    ) public final ConfigValue<Boolean> ShowStackTraces = new ConfigValue<>(true, false);

    public static GlobalPluginConfig getConfig() {
        if (CONFIG == null) CONFIG = new GlobalPluginConfig();
        return CONFIG;
    }

}
