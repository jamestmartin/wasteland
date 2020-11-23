package me.jamestmartin.wasteland.modtools.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;

import me.jamestmartin.wasteland.manual.config.ManualConfig;
import me.jamestmartin.wasteland.manual.config.ManualConfigParser;
import me.jamestmartin.wasteland.modtools.infraction.Duration;

public class ConfigParser {
    public static ModToolsConfig parse(ConfigurationSection c) {
        ManualConfig manualConfig = ManualConfigParser.parse(c.getConfigurationSection("manuals"));
        DurationsConfigs durationsConfig = parseDurationsConfig(c.getConfigurationSection("permissions"));
        
        return new ModToolsConfig(manualConfig, durationsConfig);
    }
    
    private static DurationsConfigs parseDurationsConfig(ConfigurationSection c) {
        Map<String, Duration> maximumBans = new HashMap<>();
        Map<String, Duration> maximumMutes = new HashMap<>();
        
        if (c.isString("ban")) {
            maximumBans.put("", Duration.parse(c.getString("ban")).get());
        }
        if (c.isString("mute")) {
            maximumMutes.put("", Duration.parse(c.getString("mute")).get());
        }
        
        for (String key : c.getKeys(false)) {
            if (c.isString(key)) {
                continue;
            }
            
            DurationsConfigs sub = parseDurationsConfig(c.getConfigurationSection(key));
            for (Entry<String, Duration> ban : sub.getBansConfig().getMaximumDurations().entrySet()) {
                maximumBans.put(key + "." + ban.getKey(), ban.getValue());
            }
            for (Entry<String, Duration> mute : sub.getMutesConfig().getMaximumDurations().entrySet()) {
                maximumMutes.put(key + "." + mute.getKey(), mute.getValue());
            }
        }
        
        return new DurationsConfigs(new DurationsConfig(maximumBans), new DurationsConfig(maximumMutes));
    }
}
