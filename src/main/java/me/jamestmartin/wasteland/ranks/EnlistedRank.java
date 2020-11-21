package me.jamestmartin.wasteland.ranks;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.permissions.Permission;

public class EnlistedRank extends Rank {
    private final Optional<Integer> kills;
    private final Permission preferencePermission;

    public EnlistedRank(
            String id,
            String fullName,
            String abbreviation,
            Optional<String> description,
            Optional<ChatColor> color,
            Optional<ChatColor> decoration,
            Permission permission,
            Optional<Integer> kills,
            Permission preferencePermission) {
        super(id, fullName, abbreviation, description, color, decoration, permission);
        
        this.kills = kills;
        this.preferencePermission = preferencePermission;
    }

    /** The number of kills you must receive to be promoted to this rank. */
    public Optional<Integer> getKills() {
        return kills;
    }
    
    /** Whether this rank has a set number of kills required to receive promotion. */
    public boolean hasKills() {
        return getKills().isPresent();
    }
    
    /**
     * If a player has this permission set, they will receive this rank
     * instead of any alternative ranks, even if another rank is preferred by default.
     */
    public Permission getPreferencePermission() {
        return preferencePermission;
    }
}
