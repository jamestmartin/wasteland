package me.jamestmartin.wasteland.modtools.config;

public class DurationsConfigs {
    private final DurationsConfig bansConfig;
    private final DurationsConfig mutesConfig;

    public DurationsConfigs(DurationsConfig bansConfig, DurationsConfig mutesConfig) {
        this.bansConfig = bansConfig;
        this.mutesConfig = mutesConfig;
    }
    
    public DurationsConfig getBansConfig() {
        return bansConfig;
    }
    
    public DurationsConfig getMutesConfig() {
        return mutesConfig;
    }
}
