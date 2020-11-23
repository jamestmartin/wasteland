package me.jamestmartin.wasteland.permissions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.Player;

public class PermissionsConfig {
    private final Map<String, Map<String, Boolean>> groups;
    private final Map<UUID, Set<String>> players;
    
    public PermissionsConfig(Map<String, Map<String, Boolean>> groups, Map<UUID, Set<String>> players) {
        this.groups = groups;
        this.players = players;
    }
    
    public Map<String, Boolean> getPermissions(Player player) {
        Set<String> playerGroups = players.get(player.getUniqueId());
        if (playerGroups == null) {
            return getDefaultPermissions();
        }
        
        Map<String, Boolean> permissions = new HashMap<>();
        for (String group : playerGroups) {
            permissions.putAll(groups.get(group));
        }
        
        return permissions;
    }
    
    public Map<String, Boolean> getDefaultPermissions() {
        return groups.getOrDefault("default", Map.of());
    }
}
