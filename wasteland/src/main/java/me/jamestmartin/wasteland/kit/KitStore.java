package me.jamestmartin.wasteland.kit;

import org.bukkit.entity.Player;

public interface KitStore {
    /** Get the last time the player received a kit. */
    long getLastKitTime(Player player) throws Exception;
    /** Get the total number of kits a player has received. */
    int getTotalKitsRecieved(Player player) throws Exception;
    /** Increments the total kits received and updates the last kit time. */
    void useKit(Player player) throws Exception;
}
