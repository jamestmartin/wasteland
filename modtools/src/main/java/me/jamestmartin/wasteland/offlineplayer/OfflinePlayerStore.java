package me.jamestmartin.wasteland.offlineplayer;

import org.bukkit.entity.Player;

public interface OfflinePlayerStore extends OfflinePlayerProvider {
    void logPlayer(Player player);
}
