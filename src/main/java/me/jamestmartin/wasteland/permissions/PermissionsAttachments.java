package me.jamestmartin.wasteland.permissions;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import me.jamestmartin.wasteland.Wasteland;

class PermissionsAttachments {
    private final PermissionsConfig config;

    private final Map<UUID, PermissionAttachment> attachments = new ConcurrentHashMap<>();
    
    public PermissionsAttachments(PermissionsConfig config) {
        this.config = config;
    }
    
    void removeAttachment(Player player) {
        PermissionAttachment attachment = attachments.remove(player.getUniqueId());
        if (attachment != null) {
            player.removeAttachment(attachment);
        }
    }
    
    void createAttachment(Player player) {
        Map<String, Boolean> permissions = config.getPermissions(player);
        if (permissions.isEmpty()) {
            return;
        }

        PermissionAttachment attachment = player.addAttachment(Wasteland.getInstance());
        attachments.put(player.getUniqueId(), attachment);
        for (Entry<String, Boolean> permission : permissions.entrySet()) {
            attachment.setPermission(permission.getKey(), permission.getValue());
        }
    }
    
    void updateAttachment(Player player) {
        removeAttachment(player);
        createAttachment(player);
    }
    
    void register() {
        for (Player player : Wasteland.getInstance().getServer().getOnlinePlayers()) {
            createAttachment(player);
        }
    }
    
    void unregister() {
        for(Map.Entry<UUID, PermissionAttachment> attachment : attachments.entrySet()) {
            Wasteland.getInstance().getServer().getPlayer(attachment.getKey())
                .removeAttachment(attachment.getValue());
            attachments.remove(attachment.getKey());
        }
        attachments.clear();
    }
}
