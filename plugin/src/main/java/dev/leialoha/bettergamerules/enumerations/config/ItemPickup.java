package dev.leialoha.bettergamerules.enumerations.config;

import dev.leialoha.bettergamerules.configs.annotations.ConfigAnnotation;

public enum ItemPickup {

    @ConfigAnnotation( comments = { "Allow all item pickups by mobs" } )
    ALL,

    @ConfigAnnotation( comments = {
        "Allow mob interest items to be picked up by mobs",
        "These items are typically those that the mob is naturally attracted to",
        "Items such as food (Foxes) or items related to their behavior (Piglins and their gold)."
    } )
    INTEREST,

    @ConfigAnnotation( comments = {
        "Allow armor and weapon items to be picked up by mobs",
        "This includes items that are typically used for equipping mobs, such as armor pieces and weapons.",
    } )
    ARMOR_AND_WEAPONS,

    @ConfigAnnotation( comments = { "Allow armor items to be picked up by mobs" } )
    ARMOR,

    @ConfigAnnotation( comments = { "Allow weapon items to be pickup by mobs" } )
    WEAPONS,

    @ConfigAnnotation( comments = { "Deny any item from being picked up by mobs" } )
    NONE;

}
