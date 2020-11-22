package me.jamestmartin.wasteland.kills;

import org.bukkit.entity.Player;

/** A data store which stores how many monsters a player has killed. */
public interface KillsStore extends KillsProvider {
    /** Set how many monsters a player has killed. */
    public void setPlayerKills(Player player, int kills) throws Exception;
    /** Add to the number of monsters a player has killed. */
    public void addPlayerKills(Player player, int kills) throws Exception;
    /** Add one to the number of monsters a player has killed. */
    public default void incrementPlayerKills(Player player) throws Exception {
        addPlayerKills(player, 1);
    }
}
