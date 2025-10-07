package dev.leialoha.bettergamerules.configs;

import dev.leialoha.bettergamerules.configs.generic.ConfigBase;
import dev.leialoha.bettergamerules.enumerations.config.BlockManipulation;
import dev.leialoha.bettergamerules.enumerations.config.Explosions;
import dev.leialoha.bettergamerules.enumerations.config.ItemPickup;
import dev.leialoha.configured.annotations.Config;
import dev.leialoha.configured.annotations.ConfigAnnotation;
import dev.leialoha.configured.annotations.ConfigEntry;
import dev.leialoha.configured.values.ConfigValue;
import dev.leialoha.configured.values.EnumConfigValue;

import java.util.List;

@Config(
    name = "mobGriefing",
    version = 1,
    header = {
        "Configuration for mob griefing changes",
        "This includes changes to mob item pickup behavior",
        "and other various mob-related behaviors",
    }
)
public class MobGriefingConfig extends ConfigBase {

    private transient static MobGriefingConfig CONFIG;


    //
    // Additional Config Annotations
    //

    @ConfigAnnotation(
        key = "itemPickup",
        comments = { "Controls how mobs can pickup items" },
        enumType = ItemPickup.class
    )

    @ConfigAnnotation(
        key = "blockConversion",
        comments = { "Controls how mobs can convert or interact with blocks" }
    )

    @ConfigAnnotation(
        key = "blockManipulation",
        comments = { "Controls how mobs can manipulate blocks" },
        enumType = BlockManipulation.class
    )

    @ConfigAnnotation(
        key = "explosions",
        comments = { "Controls what explosions can destroy" },
        enumType = Explosions.class
    )

    @ConfigAnnotation(
        key = "misc",
        comments = { "Miscellaneous mob behaviors" }
    )

    @ConfigAnnotation(
        key = "explosions.excludes",
        comments = { "Exclude blocks or entites from taking explosion damage" },
        executeLast = true,
        includePadding = false
    )

    // 
    // Item Pickup Settings
    //

    @ConfigEntry(
        key = "itemPickup.allay",
        comments = { "Allows Allays to pickup items similar to their held item" }
    ) public final EnumConfigValue<ItemPickup> AllayInterestPickup = new EnumConfigValue<>(ItemPickup.INTEREST);

    @ConfigEntry (
        key = "itemPickup.dolphin",
        comments = { "Allows Dolphins to \"play\" (interact) with items in water", "Any interacted item will have their despawn timer reset" }
    ) public final EnumConfigValue<ItemPickup> DolphinPickup = new EnumConfigValue<>(ItemPickup.INTEREST);

    @ConfigEntry (
        key = "itemPickup.fox",
        comments = {
            "Allows Foxes to pickup food and other items, such as weapons and armor",
            "Any kills with a weapon also applies any enchants on the mobs that they kill"
        }
    ) public final EnumConfigValue<ItemPickup> FoxPickup = new EnumConfigValue<>(ItemPickup.ALL);

    @ConfigEntry (
        key = "itemPickup.piglin",
        comments = { "Allows Piglins to pickup armor and weapons, weapons & armor" }
    ) public final EnumConfigValue<ItemPickup> PiglinPickup = new EnumConfigValue<>(ItemPickup.ALL);

    @ConfigEntry (
        key = "itemPickup.piglinBrute",
        comments = { "Allows Piglin Brutes to pickup armor and weapons", "Piglin Brutes will never pickup gold items" }
    ) public final EnumConfigValue<ItemPickup> PiglinBrutePickup = new EnumConfigValue<>(ItemPickup.ARMOR_AND_WEAPONS);

    @ConfigEntry (
        key = "itemPickup.zombifiedPiglin",
        comments = { "Allows ZombifiedPiglins to pickup armor and weapons" }
    ) public final EnumConfigValue<ItemPickup> ZombifiedPiglinPickup = new EnumConfigValue<>(ItemPickup.ARMOR_AND_WEAPONS);

    @ConfigEntry (
        key = "itemPickup.skeleton",
        comments = { "Allows Skeletons to pickup armor and weapons" }
    ) public final EnumConfigValue<ItemPickup> SkeletonPickup = new EnumConfigValue<>(ItemPickup.ARMOR_AND_WEAPONS);

    @ConfigEntry (
        key = "itemPickup.bogged",
        comments = { "Allows Boggeds to pickup armor and weapons" }
    ) public final EnumConfigValue<ItemPickup> BoggedPickup = new EnumConfigValue<>(ItemPickup.ARMOR_AND_WEAPONS);

    @ConfigEntry (
        key = "itemPickup.stray",
        comments = { "Allows Strays to pickup armor and weapons" }
    ) public final EnumConfigValue<ItemPickup> StrayPickup = new EnumConfigValue<>(ItemPickup.ARMOR_AND_WEAPONS);

    @ConfigEntry (
        key = "itemPickup.witherSkeleton",
        comments = { "Allows WitherSkeletons to pickup armor and weapons" }
    ) public final EnumConfigValue<ItemPickup> WitherSkeletonPickup = new EnumConfigValue<>(ItemPickup.ARMOR_AND_WEAPONS);

    @ConfigEntry (
        key = "itemPickup.drowned",
        comments = { "Allows Drowneds to pickup armor and weapons" }
    ) public final EnumConfigValue<ItemPickup> DrownedPickup = new EnumConfigValue<>(ItemPickup.ARMOR_AND_WEAPONS);

    @ConfigEntry (
        key = "itemPickup.zombie",
        comments = { "Allows Zombies to pickup armor and weapons" }
    ) public final EnumConfigValue<ItemPickup> ZombiePickup = new EnumConfigValue<>(ItemPickup.ARMOR_AND_WEAPONS);

    @ConfigEntry (
        key = "itemPickup.zombieVillager",
        comments = { "Allows ZombieVillagers to pickup armor and weapons" }
    ) public final EnumConfigValue<ItemPickup> ZombieVillagerPickup = new EnumConfigValue<>(ItemPickup.ARMOR_AND_WEAPONS);

    @ConfigEntry (
        key = "itemPickup.husk",
        comments = { "Allows Husks to pickup armor and weapons" }
    ) public final EnumConfigValue<ItemPickup> HuskPickup = new EnumConfigValue<>(ItemPickup.ARMOR_AND_WEAPONS);

    @ConfigEntry (
        key = "itemPickup.villager",
        comments = { "Allows Villagers to pickup bread on crops" }
    ) public final EnumConfigValue<ItemPickup> VillagerPickup = new EnumConfigValue<>(ItemPickup.INTEREST);

    //
    // Block Conversion Settings
    //
  
    @ConfigEntry (
        key = "blockConversion.blazeIgniteBlocks",
        comments = { "Allows Blazes to ignite blocks like TNT or set them on fire" }
    ) public final ConfigValue<Boolean> BlazeIgniteBlocks = new ConfigValue<>( true);

    @ConfigEntry (
        key = "blockConversion.blazeIgniteCampfires",
        comments = { "Allows Blazes to ignite campfires" }
    ) public final ConfigValue<Boolean> BlazeIgniteCampfires = new ConfigValue<>( true);

    @ConfigEntry (
        key = "blockConversion.snowmanCreateSnow",
        comments = { "Allows Snowmen to create snow layers on the ground" }
    ) public final ConfigValue<Boolean> SnowmanCreateSnow = new ConfigValue<>( true);

    @ConfigEntry (
        key = "blockConversion.foxesPickSweetBerries",
        comments = { "Allows Foxes to pick Sweet Berries from Bushes" }
    ) public final ConfigValue<Boolean> FoxesPickSweetBerries = new ConfigValue<>( true);

    @ConfigEntry (
        key = "blockConversion.rabbitsEatCarrots",
        comments = { "Allows Rabbits to eat carrots planted in farmland" }
    ) public final ConfigValue<Boolean> RabbitsEatCarrots = new ConfigValue<>( true);

    @ConfigEntry (
        key = "blockConversion.sheepEatGrass",
        comments = { "Allows Sheep to eat grass blocks to regrow their wool" }
    ) public final ConfigValue<Boolean> SheepEatGrass = new ConfigValue<>( true);

    @ConfigEntry (
        key = "blockConversion.silverfishInfestStone",
        comments = { "Allows Silverfish to infest stone blocks" }
    ) public final ConfigValue<Boolean> SilverfishInfestStone = new ConfigValue<>( true);

    @ConfigEntry (
        key = "blockConversion.farmlandTrample",
        comments = { "Allow entities to trample farmland" }
    ) public final ConfigValue<Boolean> FarmlandTrample = new ConfigValue<>( true);

    @ConfigEntry (
        key = "blockConversion.meltSnow",
        comments = { "Allow mobs to melt snow if they are on fire" }
    ) public final ConfigValue<Boolean> PowderedSnowMelt = new ConfigValue<>( true);

    @ConfigEntry (
        key = "blockConversion.witherPlacesWitherRoses",
        comments = { "Allows Withers to place Wither Roses when they kill mobs" }
    ) public final ConfigValue<Boolean> WitherRosePlace = new ConfigValue<>( true);

    //
    // Block Breaking/Placing Settings
    //

    @ConfigEntry (
        key = "blockManipulation.ravagersDestroyLeaves",
        comments = { "Allows Ravagers to destroy leaves when moving through them" }
    ) public final EnumConfigValue<BlockManipulation> RavagersDestroyLeaves = new EnumConfigValue<>(BlockManipulation.BLOCK_BREAK);

    @ConfigEntry (
        key = "blockManipulation.ravagersDestroyCrops",
        comments = { "Allows Ravagers to destroy crops when moving through them" }
    ) public final EnumConfigValue<BlockManipulation> RavagersDestroyCrops = new EnumConfigValue<>(BlockManipulation.BLOCK_BREAK);

    @ConfigEntry (
        key = "blockManipulation.villagerCrops",
        comments = { "Allows Villagers to harvest fully grown crops and replant" }
    ) public final EnumConfigValue<BlockManipulation> VillagerHarvestCrops = new EnumConfigValue<>(BlockManipulation.ALL);

    @ConfigEntry (
        key = "blockManipulation.endermanPickupBlocks",
        comments = { "Allows Endermen to pickup and place blocks" }
    ) public final EnumConfigValue<BlockManipulation> EndermanPickupBlocks = new EnumConfigValue<>(BlockManipulation.ALL);

    //
    // Explosion Settings
    //

    @ConfigEntry (
        key = "explosions.tnt",
        comments = { "Controls what TNT can destroy when it explodes" }
    ) public final EnumConfigValue<Explosions> TNTExplosion = new EnumConfigValue<>(Explosions.ALL);

    @ConfigEntry (
        key = "explosions.creeper",
        comments = { "Controls what Creepers can destroy when they explode" }
    ) public final EnumConfigValue<Explosions> CreeperExplosion = new EnumConfigValue<>(Explosions.ALL);

    @ConfigEntry (
        key = "explosions.fireball",
        comments = { "Controls what Ghast and Blaze fireballs can destroy when they explode" }
    ) public final EnumConfigValue<Explosions> FireballExplosion = new EnumConfigValue<>(Explosions.ALL);

    @ConfigEntry (
        key = "explosions.wither",
        comments = { "Controls what Wither skulls can destroy when they explode" }
    ) public final EnumConfigValue<Explosions> WitherExplosion = new EnumConfigValue<>(Explosions.ALL);

    @ConfigEntry (
        key = "explosions.enderCrystal",
        comments = { "Controls what Ender Crystals can destroy when they explode" }
    ) public final EnumConfigValue<Explosions> EnderCrystalExplosion = new EnumConfigValue<>(Explosions.ALL);

    @ConfigEntry (
        key = "explosions.excludes.entities",
        comments = { "Entities that are excluded from explosion damage" }
    ) public final ConfigValue<List<String>> ExplosionExcludesEntities = new ConfigValue<>( List.of(
        "minecraft:armor_stand",
        "minecraft:painting",
        "minecraft:item_frame",
        "minecraft:glow_item_frame",
        "minecraft:leash_knot"
    ));

    @ConfigEntry (
        key = "explosions.excludes.blocks",
        comments = { "Blocks that are excluded from explosion block damage" }
    ) public final ConfigValue<List<String>> ExplosionExcludesBlocks = new ConfigValue<>( List.of(
        "minecraft:spawner"
    ));

    //
    // Misc Settings
    //

    @ConfigEntry (
        key = "misc.evokerBlueSheep",
        comments = { "Allows Evokers to turn sheep from blue to red" }
    ) public final ConfigValue<Boolean> EvokerBlueSheep = new ConfigValue<>( true);

    @ConfigEntry (
        key = "misc.weavingEffect",
        comments = { "Allows other entities to spawn cobwebs with the weaving effect" }
    ) public final ConfigValue<Boolean> WeavingEffectGeneric = new ConfigValue<>( true);

    @ConfigEntry (
        key = "misc.weavingEffectOnCaveSpider",
        comments = { "Allows Cave Spiders to spawn cobwebs with the weaving effect" }
    ) public final ConfigValue<Boolean> WeavingEffectOnCaveSpider = new ConfigValue<>( true);

    @ConfigEntry (
        key = "misc.weavingEffectOnSpider",
        comments = { "Allows Spiders to spawn cobwebs with the weaving effect" }
    ) public final ConfigValue<Boolean> WeavingEffectOnSpider = new ConfigValue<>( true);



    public static MobGriefingConfig getConfig() {
        if (CONFIG == null) CONFIG = new MobGriefingConfig();
        return CONFIG;
    }

}
