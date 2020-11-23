package me.jamestmartin.wasteland.kit;

import java.util.Collection;
import java.util.Map;

import org.bukkit.Material;

public class KitConfig {
    private final long kitPeriod;
    private final Collection<Material> kitTools;
    private final Map<Material, Integer> kitItems;
    
    public KitConfig(long kitPeriod, Collection<Material> kitTools, Map<Material, Integer> kitItems) {
        this.kitPeriod = kitPeriod;
        this.kitTools = kitTools;
        this.kitItems = kitItems;
    }
    
    public long getKitPeriod() {
        return kitPeriod;
    }
    
    public Collection<Material> getKitTools() {
        return kitTools;
    }
    
    public Map<Material, Integer> getKitItems() {
        return kitItems;
    }
}
