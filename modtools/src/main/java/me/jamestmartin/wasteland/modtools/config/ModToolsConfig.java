package me.jamestmartin.wasteland.modtools.config;

import me.jamestmartin.wasteland.manual.config.ManualConfig;

public class ModToolsConfig {
    private final ManualConfig manuals;
    private final DurationsConfigs durations;
    
    public ModToolsConfig(ManualConfig manuals, DurationsConfigs durations) {
        this.manuals = manuals;
        this.durations = durations;
    }
    
    public ManualConfig getManuals() {
        return manuals;
    }
    
    public DurationsConfigs getDurations() {
        return durations;
    }
}
