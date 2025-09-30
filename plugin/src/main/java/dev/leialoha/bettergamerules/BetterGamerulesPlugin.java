package dev.leialoha.bettergamerules;

import java.lang.instrument.IllegalClassFormatException;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import dev.leialoha.bettergamerules.configs.GlobalPluginConfig;
import dev.leialoha.bettergamerules.configs.MobGriefingConfig;
import dev.leialoha.bettergamerules.events.MobGriefingEvents;

public class BetterGamerulesPlugin extends JavaPlugin {

    static Plugin PLUGIN = null;
    static Logger LOGGER = null;

    @Override
    public void onLoad() {
        PLUGIN = this;
    }

    @Override
    public void onEnable() {
        LOGGER = getLogger();
        LOGGER.info(getName() + " has been enabled");

        loadConfigs();
        loadEvents();
    }

    @Override
    public void onDisable() {
        LOGGER.info(getName() + " has been disabled");
    }
    
    public void loadConfigs() {
        try {
            GlobalPluginConfig.getConfig().loadConfig();
            MobGriefingConfig.getConfig().loadConfig();
        } catch (IllegalClassFormatException e) {
            LOGGER.info("Couldn't load configs, disabling...");
            getServer().getPluginManager().disablePlugin(this);
            e.printStackTrace();
        }
    }

    public void loadEvents() {
        getServer().getPluginManager().registerEvents(new MobGriefingEvents(), this);
    }


    public static Plugin getPlugin() {
        return PLUGIN;
    }

}
