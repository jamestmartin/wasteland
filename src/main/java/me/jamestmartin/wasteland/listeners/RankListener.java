package me.jamestmartin.wasteland.listeners;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

import me.jamestmartin.wasteland.Wasteland;
import me.jamestmartin.wasteland.ranks.EnlistedRank;

public class RankListener implements Listener, AutoCloseable {
	private Map<UUID, PermissionAttachment> attachments = new HashMap<>();
	private final Collection<EntityType> eligibleMobs;
	
	public RankListener(Collection<EntityType> eligibleMobs) {
	    this.eligibleMobs = eligibleMobs;
	    
		for (Player player : Wasteland.getInstance().getServer().getOnlinePlayers()) {
			try {
				initializePlayer(player);
			} catch (SQLException e) {
				Wasteland.getInstance().getLogger().log(Level.SEVERE, "Failed to get player's kills.", e);
			}
		}
	}
	
	private void createAttachment(Player player) throws SQLException {
		PermissionAttachment attachment = player.addAttachment(Wasteland.getInstance());
		attachments.put(player.getUniqueId(), attachment);
		int kills = Wasteland.getInstance().getDatabase().getPlayerKills(player);
		Optional<EnlistedRank> rank = EnlistedRank.getRankFromKills(kills);
		if (rank.isPresent()) {
			attachment.setPermission(rank.get().getPermission(), true);
		}
	}
	
	private void removeAttachment(Player player) {
		PermissionAttachment attachment = attachments.remove(player.getUniqueId());
		player.removeAttachment(attachment);
	}
	
	private void initializePlayer(Player player) throws SQLException {
		Wasteland.getInstance().getDatabase().initPlayerKills(player);
		createAttachment(player);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		try {
			initializePlayer(player);
		} catch (SQLException e) {
			Wasteland.getInstance().getLogger().log(Level.SEVERE, "Failed to get player's kills.", e);
			return;
		}
	}
	
	public void updatePlayerRank(Player player) throws SQLException {
		removeAttachment(player);
		createAttachment(player);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDeath(EntityDeathEvent event) {
		Player player = event.getEntity().getKiller();
		if (player == null) return;
		if (!eligibleMobs.contains(event.getEntityType())) return;
		
		try {
            Wasteland.getInstance().getDatabase().incrementPlayerKills(player);
            Optional<EnlistedRank> oldRank = EnlistedRank.getEnlistedRank(player);
            updatePlayerRank(player);
            Optional<EnlistedRank> newRank = EnlistedRank.getEnlistedRank(player);
            
            
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
        } catch (SQLException e) {
            Wasteland.getInstance().getLogger().log(Level.SEVERE, "Failed to increment player kills.", e);
        }
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerLeave(PlayerQuitEvent event) {
		PermissionAttachment attachment = attachments.remove(event.getPlayer().getUniqueId());
		event.getPlayer().removeAttachment(attachment);
	}
	
	@Override
	public void close() {
	    // Trying to remove attachments throws an error. Perhaps it's done automatically?
		/*for(Map.Entry<UUID, PermissionAttachment> attachment : attachments.entrySet()) {
			Wasteland.getInstance().getServer().getPlayer(attachment.getKey())
				.removeAttachment(attachment.getValue());
			attachments.remove(attachment.getKey());
		}*/
		attachments = null;
	}
}
