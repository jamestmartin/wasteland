package me.jamestmartin.wasteland.kills;

import java.util.Set;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class KillsConfig {
    private final String eligibleMobsName;
    private final Set<EntityType> eligibleMobs;
    
    public KillsConfig(String eligibleMobsName, Set<EntityType> eligibleMobs) {
        this.eligibleMobsName = eligibleMobsName;
        this.eligibleMobs = eligibleMobs;
    }
    
    public String getEligibleMobsName() {
        return eligibleMobsName;
    }
    
    public Set<EntityType> getEligibleMobs() {
        return eligibleMobs;
    }
    
    public boolean isMobEligible(EntityType type) {
        return getEligibleMobs().contains(type);
    }
    
    public boolean isMobEligible(Entity entity) {
        return isMobEligible(entity.getType());
    }
}
