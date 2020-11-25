package me.jamestmartin.wasteland.config;

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.entity.EntityType;

/**
 * Collections of {@link EntityType}s.
 * 
 * The primary purpose of this class is to provide {@link #lookupEntityType(String)}.
 */
class EntityTypes {
    /** The ender dragon and wither. */
    public static final EntityType[] BOSSES = {
            EntityType.ENDER_DRAGON,
            EntityType.WITHER,
    };
    
    /** Monsters which will attack the player on sight. */
    public static final EntityType[] HOSTILES = {
            EntityType.BLAZE,
            EntityType.CREEPER,
            EntityType.DROWNED,
            EntityType.ELDER_GUARDIAN,
            EntityType.ENDER_DRAGON,
            EntityType.ENDERMITE,
            EntityType.EVOKER,
            EntityType.GHAST,
            EntityType.GUARDIAN,
            EntityType.HOGLIN,
            EntityType.HUSK,
            EntityType.ILLUSIONER,
            EntityType.MAGMA_CUBE,
            EntityType.PHANTOM,
            EntityType.PIGLIN_BRUTE,
            EntityType.PILLAGER,
            EntityType.RAVAGER,
            EntityType.SHULKER,
            EntityType.SILVERFISH,
            EntityType.SKELETON,
            EntityType.SLIME,
            EntityType.STRAY,
            EntityType.VEX,
            EntityType.VINDICATOR,
            EntityType.WITCH,
            EntityType.WITHER,
            EntityType.WITHER_SKELETON,
            EntityType.ZOGLIN,
            EntityType.ZOMBIE,
            EntityType.ZOMBIE_VILLAGER,
    };
    
    /** Mobs which will attack the player only if provoked. */
    public static final EntityType[] NEUTRALS = {
            EntityType.BEE,
            EntityType.CAVE_SPIDER,
            EntityType.DOLPHIN,
            EntityType.ENDERMAN,
            EntityType.IRON_GOLEM,
            EntityType.LLAMA,
            EntityType.PIGLIN,
            EntityType.PANDA,
            EntityType.POLAR_BEAR,
            EntityType.PUFFERFISH,
            EntityType.SPIDER,
            EntityType.WOLF,
            EntityType.ZOMBIFIED_PIGLIN,
    };
    
    /**
     * Monsters which are eligible for the monster hunter achievement but are not hostile.
     * All hostile monsters are eligible.
     */
    public static final EntityType[] NON_HOSTILE_MONSTERS = {
            EntityType.CAVE_SPIDER,
            EntityType.ENDERMAN,
            EntityType.SPIDER,
            EntityType.ZOMBIFIED_PIGLIN,
    };
    
    /** Spiders and cave spiders. */
    public static final EntityType[] SPIDERS = {
            EntityType.CAVE_SPIDER,
            EntityType.SPIDER,
    };
    
    /** All zombie variants, including zombie villagers, zoglins, etc. */
    public static final EntityType[] ZOMBIES = {
            EntityType.GIANT,
            EntityType.ZOGLIN,
            EntityType.ZOMBIE,
            EntityType.ZOMBIE_VILLAGER,
            EntityType.ZOMBIE_HORSE,
            EntityType.ZOMBIFIED_PIGLIN,
    };
    
    /**
     * @param typeName
     *   Either a string representing an EntityType value,
     *   or a lower-case string representing one of the entity collections provided by this class.
     * @return
     *   Either the single entity type represented by the string provided,
     *   or the collection of entity types specified by the string provided.
     * 
     * @see EntityType#valueOf(String)
     * @see BOSSES bosses
     * @see HOSTILES hostiles
     * @see NEUTRALS neutrals
     * @see NON_HOSTILE_MONSTERS monsters
     * @see SPIDERS spiders
     * @see ZOMBIES zombies
     */
    public static EntityType[] lookupEntityType(String typeName) {
        switch(typeName) {
            case "bosses":
                return BOSSES;
            case "hostiles":
                return HOSTILES;
            case "monsters":
                HashSet<EntityType> monsters = new HashSet<>();
                monsters.addAll(Arrays.asList(HOSTILES));
                monsters.addAll(Arrays.asList(NON_HOSTILE_MONSTERS));
                return monsters.toArray(EntityType[]::new);
            case "neutrals":
                return NEUTRALS;
            case "spiders":
                return SPIDERS;
            case "zombies":
                return ZOMBIES;
            default:
                EntityType type = EntityType.valueOf(typeName);
                if (type == null) {
                    throw new IllegalArgumentException("Unknown entity type: " + typeName);
                }
                EntityType[] rv = { type };
                return rv;
        }
    };
}
