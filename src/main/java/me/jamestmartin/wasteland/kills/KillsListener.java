package me.jamestmartin.wasteland.kills;

import java.util.Optional;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import me.jamestmartin.wasteland.Wasteland;
import me.jamestmartin.wasteland.ranks.EnlistedRank;
import me.jamestmartin.wasteland.ranks.PlayerRankProvider;

class KillsListener implements Listener {
    private final KillsConfig config;
    private final PlayerKillsStore store;
    private final PlayerRankProvider provider;
	
	public KillsListener(KillsConfig config, PlayerKillsStore store, PlayerRankProvider provider) {
	    this.config = config;
	    this.store = store;
	    this.provider = provider;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDeath(EntityDeathEvent event) {
		Player player = event.getEntity().getKiller();
		if (player == null) return;
		if (!config.isMobEligible(event.getEntityType())) return;
		
		try {
		    // TODO: Rank-related logic does not belong in this listener.
            Optional<EnlistedRank> oldRank = provider.getEnlistedRank(player);
            store.incrementPlayerKills(player);
            Optional<EnlistedRank> newRank = provider.getEnlistedRank(player);
            
            if (newRank.isPresent()) {
                final String formatString;
                if (oldRank.isPresent()) {
                    if (!newRank.get().equals(oldRank.get())) {
                        formatString = "%s" + ChatColor.RESET + " has been promoted from %s " + ChatColor.RESET + "to %s" + ChatColor.RESET + "!";
                        player.getServer().broadcastMessage(
                                String.format(formatString, player.getDisplayName(),
                                        oldRank.get().formatFull(), newRank.get().formatFull()));
                    }
                } else {
                    formatString = "%s" + ChatColor.RESET + " has been promoted to %s" + ChatColor.RESET + "!";
                    player.getServer().broadcastMessage(
                            String.format(formatString, player.getDisplayName(), newRank.get()));
                }
            }
        } catch (Exception e) {
            Wasteland.getInstance().getLogger().log(Level.SEVERE, "Failed to increment player kills.", e);
        }
	}
}
