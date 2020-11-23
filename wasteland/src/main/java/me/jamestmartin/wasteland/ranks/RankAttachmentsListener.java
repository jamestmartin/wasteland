package me.jamestmartin.wasteland.ranks;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.jamestmartin.wasteland.Wasteland;

public class RankAttachmentsListener implements Listener {
    private final RankAttachments attachments;
    
    public RankAttachmentsListener(RankAttachments attachments) {
        this.attachments = attachments;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        try {
            attachments.createAttachment(player);
        } catch (Exception e) {
            Wasteland.getInstance().getLogger().log(Level.SEVERE, "Failed to get player's kills.", e);
            return;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeave(PlayerQuitEvent event) {
        attachments.removeAttachment(event.getPlayer());
    }
}
