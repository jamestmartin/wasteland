package me.jamestmartin.wasteland.config;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import me.jamestmartin.wasteland.world.MoonPhase;

public class MonsterSpawnConfig {
    private final int maximumLightLevel;
    private final float blocklightWeight;
    private final float sunlightWeight;
    private final float moonlightWeight;
    
    private final int maximumYLevel;
    private final int minimumYLevel;
    
    private final Map<MoonPhase, Float> phaseMultipliers;
    
    public MonsterSpawnConfig(final ConfigurationSection c) {
        if (!c.isConfigurationSection("light")) {
            this.maximumLightLevel = c.getInt("light", 9);
            this.blocklightWeight = 1.0f;
            this.sunlightWeight = 1.0f;
            this.moonlightWeight = 0.0f;
        } else {
            final ConfigurationSection cLight = c.getConfigurationSection("light");
            this.maximumLightLevel = cLight.getInt("maximum", 9);
            this.blocklightWeight = (float) cLight.getDouble("weights.block", 1.0);
            this.sunlightWeight = (float) cLight.getDouble("weights.sun", 1.0);
            this.moonlightWeight = (float) cLight.getDouble("weights.moon", 0.0);
        }
        
        this.minimumYLevel = c.getInt("height.minimum", 0);
        this.maximumYLevel = c.getInt("height.maximum", Integer.MAX_VALUE);
        
        final ConfigurationSection cPhases = c.getConfigurationSection("phases");
        this.phaseMultipliers = new HashMap<>();
        if (cPhases != null) {
            for (String phaseKey : cPhases.getKeys(false)) {
                MoonPhase phase = MoonPhase.valueOf(phaseKey);
                float multiplier = (float) cPhases.getDouble(phaseKey);
                this.phaseMultipliers.put(phase, multiplier);
            }
        }
    }
    
    public int maximumLightLevel() {
        return maximumLightLevel;
    }
    
    public float blocklightWeight() {
        return blocklightWeight;
    }
    
    public float sunlightWeight() {
        return sunlightWeight;
    }
    
    public float moonlightWeight() {
        return moonlightWeight;
    }
    
    public int minimumYLevel() {
        return minimumYLevel;
    }
    
    public int maximumYLevel() {
        return maximumYLevel;
    }
    
    public Map<MoonPhase, Float> phaseMultipliers() {
        return phaseMultipliers;
    }
}
