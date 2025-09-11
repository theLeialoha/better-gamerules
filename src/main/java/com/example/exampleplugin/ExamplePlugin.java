package com.example.exampleplugin;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class ExamplePlugin extends JavaPlugin {

    static Logger LOGGER = null;

    @Override
    public void onEnable() {
        LOGGER = getLogger();
        LOGGER.info(getName() + " has been enabled");
    }

    @Override
    public void onDisable() {
        LOGGER.info(getName() + " has been disabled");
    }

}
