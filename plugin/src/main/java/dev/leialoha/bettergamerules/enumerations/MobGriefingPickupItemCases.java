package dev.leialoha.bettergamerules.enumerations;

import java.util.function.Supplier;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import dev.leialoha.bettergamerules.configs.GlobalPluginConfig;
import dev.leialoha.bettergamerules.configs.MobGriefingConfig;

public enum MobGriefingPickupItemCases {

    ALLAY_INTEREST_PICKUP(EntityType.ALLAY, "AllayInterestPickup"),

    DOLPHIN_INTEREST_PICKUP(EntityType.DOLPHIN, "DolphinInterestPickup"),

    FOX_INTEREST_PICKUP(EntityType.FOX, "FoxInterestPickup"),

    PIGLIN_ARMOR_PICKUP(EntityType.PIGLIN, "PiglinArmorPickup", true),
    PIGLIN_WEAPON_PICKUP(EntityType.PIGLIN, "PiglinWeaponPickup", false),
    PIGLIN_INTEREST_PICKUP(EntityType.PIGLIN, "PiglinInterestPickup"),

    PIGLIN_BRUTE_ARMOR_PICKUP(EntityType.PIGLIN_BRUTE, "PiglinBruteArmorPickup", true),
    PIGLIN_BRUTE_WEAPON_PICKUP(EntityType.PIGLIN_BRUTE, "PiglinBruteWeaponPickup", false),

    ZOMBIFIED_PIGLIN_ARMOR_PICKUP(EntityType.ZOMBIFIED_PIGLIN, "ZombifiedPiglinArmorPickup", true),
    ZOMBIFIED_PIGLIN_WEAPON_PICKUP(EntityType.ZOMBIFIED_PIGLIN, "ZombifiedPiglinWeaponPickup", false),

    SKELETON_ARMOR_PICKUP(EntityType.SKELETON, "SkeletonArmorPickup", true),
    SKELETON_WEAPON_PICKUP(EntityType.SKELETON, "SkeletonWeaponPickup", false),

    BOGGED_ARMOR_PICKUP(EntityType.BOGGED, "BoggedArmorPickup", true),
    BOGGED_WEAPON_PICKUP(EntityType.BOGGED, "BoggedWeaponPickup", false),

    STRAY_ARMOR_PICKUP(EntityType.STRAY, "StrayArmorPickup", true),
    STRAY_WEAPON_PICKUP(EntityType.STRAY, "StrayWeaponPickup", false),

    WITHER_SKELETON_ARMOR_PICKUP(EntityType.WITHER_SKELETON, "WitherSkeletonArmorPickup", true),
    WITHER_SKELETON_WEAPON_PICKUP(EntityType.WITHER_SKELETON, "WitherSkeletonWeaponPickup", false),

    DROWNED_ARMOR_PICKUP(EntityType.DROWNED, "DrownedArmorPickup", true),
    DROWNED_WEAPON_PICKUP(EntityType.DROWNED, "DrownedWeaponPickup", false),

    ZOMBIE_ARMOR_PICKUP(EntityType.ZOMBIE, "ZombieArmorPickup", true),
    ZOMBIE_WEAPON_PICKUP(EntityType.ZOMBIE, "ZombieWeaponPickup", false),

    ZOMBIE_VILLAGER_ARMOR_PICKUP(EntityType.ZOMBIE_VILLAGER, "ZombieVillagerArmorPickup", true),
    ZOMBIE_VILLAGER_WEAPON_PICKUP(EntityType.ZOMBIE_VILLAGER, "ZombieVillagerWeaponPickup", false),

    HUSK_ARMOR_PICKUP(EntityType.HUSK, "HuskArmorPickup", true),
    HUSK_WEAPON_PICKUP(EntityType.HUSK, "HuskWeaponPickup", false),

    VILLAGER_INTEREST_PICKUP(EntityType.VILLAGER, "VillagerInterestPickup"),

    DEFAULT_ARMOR_PICKUP(null, () -> false, true),
    DEFAULT_INTEREST_PICKUP(null, () -> false, false),
    DEFAULT_WEAPON_PICKUP(null, () -> false);

    private final EntityType type;
    private final Supplier<Boolean> function;
    private final boolean isArmorPickup;
    private final boolean isToolPickup;

    MobGriefingPickupItemCases(EntityType type, Supplier<Boolean> function) {
        this.type = type;
        this.function = function;
        this.isArmorPickup = false;
        this.isToolPickup = false;
    }

    MobGriefingPickupItemCases(EntityType type, Supplier<Boolean> function, boolean isArmorPickup) {
        this.type = type;
        this.function = function;
        this.isArmorPickup = isArmorPickup;
        this.isToolPickup = !isArmorPickup;
    }

    MobGriefingPickupItemCases(EntityType type, String methodName) {
        this(type, GlobalPluginConfig.getConfig().getSelfSupplier(methodName));
    }

    MobGriefingPickupItemCases(EntityType type, String methodName, boolean isArmorPickup) {
        this(type, GlobalPluginConfig.getConfig().getSelfSupplier(methodName), isArmorPickup);
    }

    private boolean checkEntityType(EntityType other) {
        if (type == null) return true;
        return type.equals(other);
    }

    private boolean check(MobGriefingConfig config) {
        return function.get();
    }


    @EventHandler
    public static void entityPickUpItem(EntityPickupItemEvent e) {
        EntityType type = e.getEntityType();
        
        Item itemEntity = e.getItem();
        ItemStack item = itemEntity.getItemStack();
        Material material = item.getType();
        ItemMeta meta = item.getItemMeta();
        
        boolean isArmor = meta instanceof ArmorMeta || material.equals(Material.SHIELD);
        boolean isTool = (meta instanceof Damageable) && !isArmor;

        // Cancelled elsewhere
        if (e.isCancelled()) return;

        MobGriefingConfig config = MobGriefingConfig.getConfig();

        for (MobGriefingPickupItemCases griefCase : values()) {
            if (!griefCase.checkEntityType(type)) continue;
            if (griefCase.isArmorPickup != isArmor) continue;
            if (griefCase.isToolPickup != isTool) continue;

            if (!griefCase.check(config))
                e.setCancelled(true);
                
            break;
        }

    }


}
