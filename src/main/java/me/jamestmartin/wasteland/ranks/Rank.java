package me.jamestmartin.wasteland.ranks;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class Rank {
    private final String id;
    private final String fullName;
    private final String abbreviation;
    private final Optional<String> description;
    private final Optional<ChatColor> color;
    private final Optional<ChatColor> decoration;
    private final Permission permission;
    
    public Rank(
            String id,
            String fullName,
            String abbreviation,
            Optional<String> description,
            Optional<ChatColor> color,
            Optional<ChatColor> decoration,
            Permission permission) {
        this.id = id;
        this.fullName = fullName;
        this.abbreviation = abbreviation;
        this.description = description;
        this.color = color;
        this.decoration = decoration;
        this.permission = permission;
    }
    
    /** Get this rank's internal identifier, e.g. gysgt. */
    public String getId() {
        return id;
    }
    
    /** Get the full name of this rank, e.g. Gunnery Sergeant. */
    public String getFullName() {
        return fullName;
    }
    
    /** Get the abbreviation for this rank, e.g. GySgt. */
    public String getAbbreviation() {
        return abbreviation;
    }
    
    /** Get this rank's description, if it has one. */
    public Optional<String> getDescription() {
        return description;
    }
    
    /** Get this rank's color, if it has one. */
    public Optional<ChatColor> getColor() {
        return color;
    }
    
    /** Get this rank's decoration (e.g. bold or italic), if it has one. */
    public Optional<ChatColor> getDecoration() {
        return decoration;
    }
    
    /** Get the permission which all players with this rank have. */
    public Permission getPermission() {
        return permission;
    }
    
    /** Check whether a player has this rank via permissions. */
    public boolean hasRank(Player player) {
        return player.hasPermission(getPermission());
    }
    
    /** Get the chat formatting codes for this rank, i.e. the color and decoration combined. */
    public String getFormat() {
        String color = this.color.map(ChatColor::toString).orElse("");
        String decoration = this.decoration.map(ChatColor::toString).orElse("");
        return color + decoration;
    }
    
    /** The rank abbreviation with chat formatting codes. */
    public String formatAbbreviated() {
        return getFormat() + getAbbreviation();
    }
    
    /** The rank name with chat formatting codes. */
    public String formatFull() {
        return getFormat() + getFullName();
    }
    
    /** `Rank Name (RankAbbr)` with chat formatting codes. */
    public String formatExtended() {
        return formatFull() + ChatColor.RESET + " (" + formatAbbreviated() + ChatColor.RESET + ")";
    }
    
    @Override
    public String toString() {
        return this.getAbbreviation();
    }
}
