package dev.leialoha.bettergamerules.enumerations.config;

import dev.leialoha.configured.annotations.ConfigAnnotation;

public enum Explosions {

    @ConfigAnnotation( comments = { "Allow all explosion damage" } )
    ALL,

    @ConfigAnnotation( comments = { "Allow explosion damage to related towards blocks" } )
    BLOCK_DAMAGE,

    @ConfigAnnotation( comments = { "Allow explosion damage to related towards mobs" } )
    MOB_DAMAGE,

    @ConfigAnnotation( comments = { "Deny any explosion damage" } )
    NONE;

}
