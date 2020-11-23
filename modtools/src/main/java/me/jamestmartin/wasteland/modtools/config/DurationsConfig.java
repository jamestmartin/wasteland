package me.jamestmartin.wasteland.modtools.config;

import java.util.Map;

import org.bukkit.command.CommandSender;

import me.jamestmartin.wasteland.modtools.infraction.Duration;

public class DurationsConfig {
    private final Map<String, Duration> maxima;

    public DurationsConfig(Map<String, Duration> maxima) {
        this.maxima = maxima;
    }
    
    public Map<String, Duration> getMaximumDurations() {
        return maxima;
    }
    
    public Duration getMaximumBanDuration(CommandSender moderator) {
        Duration duration = maxima.getOrDefault("default", Duration.ZERO);
        for (String perm : maxima.keySet()) {
            if (moderator.hasPermission(perm)) {
                duration = duration.max(maxima.get(perm));
            }
        }
        return duration;
    }
}
