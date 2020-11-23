package me.jamestmartin.wasteland.permissions;

import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.jamestmartin.wasteland.Substate;

public class PermissionsState implements Substate {
    private final PermissionsAttachments attachments;
    private final PermissionsAttachmentsListener listener;
    
    public PermissionsState(PermissionsConfig config) {
        this.attachments = new PermissionsAttachments(config);
        this.listener = new PermissionsAttachmentsListener(attachments);
    }

    @Override
    public void register(JavaPlugin plugin) {
        attachments.register();
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    @Override
    public void unregister(JavaPlugin plugin) {
        PlayerQuitEvent.getHandlerList().unregister(listener);
        attachments.unregister();
    }
}
