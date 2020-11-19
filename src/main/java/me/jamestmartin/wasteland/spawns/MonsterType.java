package me.jamestmartin.wasteland.spawns;

import org.bukkit.entity.EntityType;

public enum MonsterType {
    /** A creeper or charged creeper, as appropriate. */
    CREEPER(EntityType.CREEPER),
    /** A skeleton, wither skeleton, stray, etc., as appropriate. */
    SKELETON(EntityType.SKELETON),
    /** A spider, cave spider, or spider jockey, as appropriate. */
    SPIDER(EntityType.SPIDER),
    /** A zombie, husk, pig zombie, etc., as appropriate. */
    ZOMBIE(EntityType.ZOMBIE);
    
    private final EntityType defaultVariant;
    
    private MonsterType(final EntityType defaultVariant) {
        this.defaultVariant = defaultVariant;
    }
    
    public EntityType getDefaultVariant() {
        return defaultVariant;
    }
}
