package dev.leialoha.bettergamerules.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import dev.leialoha.bettergamerules.enumerations.MobGriefingPickupItemCases;

public class MobGriefingEvents implements Listener {

    @EventHandler
    public void entityPickUpItem(EntityPickupItemEvent e) {
        MobGriefingPickupItemCases.entityPickUpItem(e);
    }



}
