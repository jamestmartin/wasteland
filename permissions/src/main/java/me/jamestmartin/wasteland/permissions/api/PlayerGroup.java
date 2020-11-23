package me.jamestmartin.wasteland.permissions.api;

import java.util.List;
import java.util.Map;

import org.bukkit.OfflinePlayer;

public class PlayerGroup extends PseudoGroup {
    private final OfflinePlayer player;

    public PlayerGroup(OfflinePlayer player, List<Group> parents, Map<String, Boolean> permissions) {
        super(parents, permissions);
        this.player = player;
    }
    
    public PlayerGroup(OfflinePlayer player, PseudoGroup group) {
        this(player, group.getParents(), group.getDirectPermissions());
    }
    
    public PlayerGroup(OfflinePlayer player, Group group) {
        this(player, List.of(group), Map.of());
    }
    
    public PlayerGroup(OfflinePlayer player) {
        this(player, List.of(), Map.of());
    }
    
    public OfflinePlayer getPlayer() {
        return player;
    }
}
