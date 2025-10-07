package dev.leialoha.bettergamerules.enumerations.config;

import dev.leialoha.configured.annotations.ConfigAnnotation;

public enum BlockManipulation {

    @ConfigAnnotation( comments = { "Allow all block manipulations by mobs" } )
    ALL,

    @ConfigAnnotation( comments = { "Allow block breaking by mobs" } )
    BLOCK_BREAK,

    @ConfigAnnotation( comments = { "Allow block placement by mobs" } )
    BLOCK_PLACE,

    @ConfigAnnotation( comments = { "Deny any block manipulation by mobs" } )
    NONE;

}
