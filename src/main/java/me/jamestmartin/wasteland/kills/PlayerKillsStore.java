package me.jamestmartin.wasteland.kills;

import org.bukkit.entity.Player;

/** A data store which stores how many monsters a player has killed. */
public interface PlayerKillsStore extends PlayerKillsProvider {
    /** Set how many monsters a player has killed. */
    public void setPlayerKills(Player player, int kills) throws Exception;
    /** Add to the number of monsters a player has killed. */
    public void addPlayerKills(Player player, int kills) throws Exception;
    /** Add one to the number of monsters a player has killed. */
    public default void incrementPlayerKills(Player player) throws Exception {
        addPlayerKills(player, 1);
    }
    
    /** Add a player to the store if they are not already present, if necessary. */
    public default void initPlayer(Player player) throws Exception { }
}
