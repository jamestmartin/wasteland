package me.jamestmartin.wasteland.spawns;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;

import me.jamestmartin.wasteland.world.MoonPhase;

public class MonsterSpawnConfig {
    private final double maximumLightLevel;
    private final double blocklightWeight;
    private final double sunlightWeight;
    private final double moonlightWeight;
    
    private final int maximumYLevel;
    private final int minimumYLevel;
    
    private final Map<MoonPhase, Double> phaseMultipliers;
    
    public MonsterSpawnConfig(
            double maximumLightLevel,
            double blocklightWeight,
            double sunlightWeight,
            double moonlightWeight,
            int maximumYLevel,
            int minimumYLevel,
            Map<MoonPhase, Double> phaseMultipliers
            ) {
        if (maximumLightLevel < 0) {
            throw new IllegalArgumentException("Maximum light level cannot be negative.");
        }
        if (maximumYLevel < 0) {
            throw new IllegalArgumentException("Maximum Y level cannot be negative.");
        }
        if (minimumYLevel < 0) {
            throw new IllegalArgumentException("Minimum Y level cannot be negative.");
        }
        
        this.maximumLightLevel = maximumLightLevel;
        this.blocklightWeight = blocklightWeight;
        this.sunlightWeight = sunlightWeight;
        this.moonlightWeight = moonlightWeight;
        this.maximumYLevel = maximumYLevel;
        this.minimumYLevel = minimumYLevel;
        this.phaseMultipliers = phaseMultipliers;
    }
    
    public double getMaximumLightLevel() {
        return maximumLightLevel;
    }
    
    public double getBlocklightWeight() {
        return blocklightWeight;
    }
    
    public double getSunlightWeight() {
        return sunlightWeight;
    }
    
    public double getMoonlightWeight() {
        return moonlightWeight;
    }
    
    public double calculateWeightedBlocklightLevel(int blocklight) {
        return blocklight * getBlocklightWeight();
    }
    
    public double calculateWeightedBlocklightLevel(Block block) {
        return calculateWeightedBlocklightLevel(block.getLightFromBlocks());
    }
    
    public double calculateWeightedSkylightLevel(int sunlight, int moonlight) {
        return sunlight * getSunlightWeight() + moonlight * getMoonlightWeight();
    }
    
    public double calculateWeightedSkylightLevel(Block block) {
        return calculateWeightedSkylightLevel(MoonPhase.getSunlight(block), MoonPhase.getMoonlight(block));
    }
    
    public double calculateWeightedLightLevel(int blocklight, int sunlight, int moonlight) {
        return Math.max(calculateWeightedBlocklightLevel(blocklight), calculateWeightedSkylightLevel(sunlight, moonlight));
    }
    
    public double calculateWeightedLightLevel(Block block) {
        return Math.max(calculateWeightedBlocklightLevel(block), calculateWeightedSkylightLevel(block));
    }
    
    public boolean isWeightedLightLevelWithinBounds(int blocklight, int sunlight, int moonlight) {
        return calculateWeightedLightLevel(blocklight, sunlight, moonlight) < getMaximumLightLevel();
    }
    
    public boolean isWeightedLightLevelWithinBounds(Block block) {
        return calculateWeightedLightLevel(block) < getMaximumLightLevel();
    }
    
    public int getMinimumYLevel() {
        return minimumYLevel;
    }
    
    public int getMaximumYLevel() {
        return maximumYLevel;
    }
    
    public boolean isYLevelWithinBounds(int yLevel) {
        return yLevel > getMinimumYLevel() && yLevel < getMaximumYLevel();
    }
    
    public boolean isWithinBounds(Block block) {
        return isYLevelWithinBounds(block.getY());
    }
    
    public boolean isWithinBounds(Location location) {
        return isYLevelWithinBounds(location.getBlockY());
    }
    
    public Map<MoonPhase, Double> getPhaseMultipliers() {
        return phaseMultipliers;
    }
    
    public Double getPhaseMultiplier(MoonPhase phase) {
        return getPhaseMultipliers().get(phase);
    }
}
