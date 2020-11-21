package me.jamestmartin.wasteland.kills;

import org.bukkit.entity.Player;

public interface PlayerKillsProvider {
    /** Get how many monsters a player has killed. */
    public int getPlayerKills(Player player) throws Exception;
}
