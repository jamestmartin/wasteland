package me.jamestmartin.wasteland.permissions;

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
        attachments.createAttachment(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeave(PlayerQuitEvent event) {
        attachments.removeAttachment(event.getPlayer());
    }
}
