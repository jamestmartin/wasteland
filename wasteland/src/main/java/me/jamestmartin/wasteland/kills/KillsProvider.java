package me.jamestmartin.wasteland.kills;

import org.bukkit.entity.Player;

public interface KillsProvider {
    /** Get how many monsters a player has killed. */
    public int getPlayerKills(Player player) throws Exception;
}
