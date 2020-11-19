package me.jamestmartin.wasteland.spawns;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import me.jamestmartin.wasteland.config.MonsterSpawnConfig;
import me.jamestmartin.wasteland.world.MoonPhase;

public class WastelandSpawner {
    private final Map<MonsterType, MonsterSpawnConfig> monsters;
    
    public WastelandSpawner(Map<MonsterType, MonsterSpawnConfig> monsters) {
        this.monsters = monsters;
    }
    
    public static Collection<MonsterType> spawnableMonstersAt(Location location) {
        // It is impossible to spawn any monster without some empty space.
        if (!location.getBlock().isPassable()) {
            return Set.of();
        }
        
        // It is impossible to spawn any monster without a floor.
        if (!location.getBlock().getRelative(BlockFace.DOWN).getType().isSolid()) {
            return Set.of();
        }

        Set<MonsterType> spawnableMonsters = new HashSet<>();
        
        // 1x2x1 monsters
        if (location.getBlock().getRelative(BlockFace.UP).isPassable()) {
            spawnableMonsters.add(MonsterType.CREEPER);
            spawnableMonsters.add(MonsterType.SKELETON);
            spawnableMonsters.add(MonsterType.ZOMBIE);
        }
        
        // 2x1x2 monsters
        if (
                // There is space for the monster to spawn
                location.getBlock().getRelative(1, 0, 0).isPassable()
                && location.getBlock().getRelative(0, 0, 1).isPassable()
                && location.getBlock().getRelative(1, 0, 1).isPassable()
                // There is a complete floor for it to spawn on
                && location.getBlock().getRelative(1, -1, 0).getType().isSolid()
                && location.getBlock().getRelative(0, -1, 1).getType().isSolid()
                && location.getBlock().getRelative(1, -1, 1).getType().isSolid()
            ) {
            spawnableMonsters.add(MonsterType.SPIDER);
        }
        
        return spawnableMonsters;
    }
    
    /** 
     * Takes a number on the interval and maps it to another number of the interval according to the logistic curve.
     * 
     * Graph: https://www.wolframalpha.com/input/?i=f%28x%29+%3D++e%5E%286%282x+-+1%29%29+%2F+%28e%5E%286%282x+-+1%29%29+%2B+1%29++from+0+to+1
     */
    private static float logistic(float x) {
        float eToX = (float) Math.pow(Math.E, 12 * x - 6);
        return eToX / (eToX + 1);
    }
    
    public static float calculateWeightedLightLevel(Block block, float sunlightWeight, float moonlightWeight, float blocklightWeight) {
        float weightedSkylight = MoonPhase.getMoonlight(block) * moonlightWeight + MoonPhase.getSunlight(block) * sunlightWeight;
        float weightedBlocklight = block.getLightFromBlocks() * blocklightWeight;
        return Math.max(weightedSkylight, weightedBlocklight);
    }
    
    public static float calculateWeightedLightLevelProbability(Block block, int maximumLight, float sunlightWeight, float moonlightWeight, float blocklightWeight) {
        float weightedLightLevel = calculateWeightedLightLevel(block, sunlightWeight, moonlightWeight, blocklightWeight);
        if (weightedLightLevel >= maximumLight) {
            return 0.0f;
        }
        return 1 - logistic(weightedLightLevel / maximumLight);
    }
    
    public float calculateLightLevelProbability(MonsterType type, Block block) {
        MonsterSpawnConfig cfg = monsters.get(type);
        return calculateWeightedLightLevelProbability(block, cfg.maximumLightLevel(), cfg.sunlightWeight(), cfg.moonlightWeight(), cfg.blocklightWeight());
    }
    
    public float calculateLightLevelProbability(MonsterType type, Location location) {
        return calculateLightLevelProbability(type, location.getBlock());
    }
    
    public float getMoonPhaseMultiplier(MonsterType type, MoonPhase phase) {
        return monsters.get(type).phaseMultipliers().getOrDefault(phase, 1.0f);
    }
    
    public float getMoonPhaseMultiplier(MonsterType type, World world) {
        return getMoonPhaseMultiplier(type, MoonPhase.fromWorld(world));
    }
    
    public float getMoonPhaseMultiplier(MonsterType type, Location location) {
        return getMoonPhaseMultiplier(type, location.getWorld());
    }
    
    public float getMoonPhaseMultiplier(MonsterType type, Block block) {
        return getMoonPhaseMultiplier(type, block.getWorld());
    }
    
    public static int countNearbyEntities(Class<? extends LivingEntity> clazz, Location location) {
        return location.getWorld().getNearbyEntities(location, 50, 50, 50, e -> { return clazz.isInstance(e); }).size();
    }
    
    public static int countNearbyMonsters(Location location) {
        return countNearbyEntities(Monster.class, location);
    }
    
    public static int countNearbyPlayers(Location location) {
        return countNearbyEntities(Player.class, location);
    }
    
    public static float calculateNearbyEntitiesMultiplier(Location location) {
        final int nearbyMonsters = countNearbyMonsters(location);
        // TODO: Change to be a weighted value based on player distance.
        final int nearbyPlayers = countNearbyPlayers(location);
        
        // This algorithm is pretty ad-hoc. Don't overthink it.
        // Basically, I want lots of nearby players to result in a higher spawn cap,
        // but with diminishing returns so that more players is beneficial.
        // (If it were linear, you would be at a serious disadvantage due to radiation effects and lag,
        // and screwed unless all the other players were helping you fight.)
        // It's also not a hard spawn cap, but past a point, the spawn rate needs to be severely diminished
        // to prevent massive lag and infinitely-sized hordes.
        final float idealMobQuantityMultiplier = (float) Math.max(0.5, Math.sqrt(nearbyPlayers)) * 25;
        
        // constant factor of 2 allows some mobs to spawn beyond the ideal quantity,
        // which in the logistic curve makes spawning mobs up to that point far more likely.
        return 1 - logistic(nearbyMonsters / idealMobQuantityMultiplier / 2);
    }
    
    public static float calculateNearbyEntitiesMultiplier(Block block) {
        return calculateNearbyEntitiesMultiplier(block.getLocation());
    }
    
    public float calculateSpawnProbability(MonsterType type, Location location) {
        final float lightLevelProbability = calculateLightLevelProbability(type, location);
        
        long time = location.getWorld().getTime();
        float moonPhaseMultiplier = 0f;
        if (time > 12000) { // dawn, dusk, or night
            moonPhaseMultiplier = getMoonPhaseMultiplier(type, location);
            if (time < 13000 || time > 23000) { // dawn or dusk
                moonPhaseMultiplier /= 2;
            }
        }
        
        final float nearbyEntitiesMultiplier = calculateNearbyEntitiesMultiplier(location);
        
        return lightLevelProbability * moonPhaseMultiplier * nearbyEntitiesMultiplier;
    }
    
    public float calculateSpawnProbability(MonsterType type, Block block) {
        return calculateSpawnProbability(type, block.getLocation());
    }
    
    public HashMap<MonsterType, Float> calculateSpawnProbabilities(Location location) {
        HashMap<MonsterType, Float> spawnWeights = new HashMap<>();
        for (MonsterType type : spawnableMonstersAt(location)) {
            spawnWeights.put(type, calculateSpawnProbability(type, location));
        }
        return spawnWeights;
    }
    
    public HashMap<MonsterType, Float> calculateSpawnProbabilities(Block block) {
        return calculateSpawnProbabilities(block.getLocation());
    }
}
