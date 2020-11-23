package me.jamestmartin.wasteland.permissions.api;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.OfflinePlayer;

import me.jamestmartin.wasteland.permissions.WastelandPermissions;

public class Permissions {
    private final Optional<Group> defaultGroup;
    private final Map<String, Group> groups;
    private final Map<UUID, PlayerGroup> playerGroups;
    
    public Permissions(Map<String, Group> groups, Map<UUID, PlayerGroup> playerGroups) {
        this.groups = groups;
        this.playerGroups = playerGroups;
        this.defaultGroup = Optional.ofNullable(groups.get("default"));
    }
    
    /** Get the permissions of all players specified in the configuration. */
    public Map<UUID, PlayerGroup> getPlayerGroups() {
        return playerGroups;
    }
    
    /** Get every group specified in the configuration. */
    public Map<String, Group> getGroups() {
        return groups;
    }
    
    public PlayerGroup getPlayerGroup(OfflinePlayer player) {
        return Optional.ofNullable(getPlayerGroups().get(player.getUniqueId()))
            .or(() -> defaultGroup.map(group -> new PlayerGroup(player, group)))
            .orElseGet(() -> new PlayerGroup(player));
    }
    
    public PlayerGroup getPlayerGroup(UUID player) {
        return getPlayerGroup(WastelandPermissions.getInstance().getServer().getOfflinePlayer(player));
    }
    
    public Optional<Group> getGroup(String id) {
        return Optional.ofNullable(groups.get(id));
    }
    
    public Optional<Group> getDefaultGroup() {
        return defaultGroup;
    }
}
