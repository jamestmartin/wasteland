package me.jamestmartin.wasteland.permissions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

class PermissionsAttachmentsListener implements Listener {
    private final PermissionsAttachments attachments;
    
    public PermissionsAttachmentsListener(PermissionsAttachments attachments) {
        this.attachments = attachments;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        attachments.createAttachment(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeave(PlayerQuitEvent event) {
        attachments.removeAttachment(event.getPlayer());
    }
}
