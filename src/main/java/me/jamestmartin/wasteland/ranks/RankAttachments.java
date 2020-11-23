package me.jamestmartin.wasteland.ranks;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import me.jamestmartin.wasteland.Wasteland;
import me.jamestmartin.wasteland.kills.KillsStore;

public class RankAttachments {
    private final EnlistedRanks ranks;
    private final KillsStore killsStore;
    
    private final Map<UUID, PermissionAttachment> attachments = new ConcurrentHashMap<>();
    
    public RankAttachments(EnlistedRanks ranks, KillsStore killsStore) {
        this.ranks = ranks;
        this.killsStore = killsStore;
    }
    
    public void removeAttachment(Player player) {
        PermissionAttachment attachment = attachments.remove(player.getUniqueId());
        player.removeAttachment(attachment);
    }
    
    private void createAttachment(Player player, int kills) throws Exception {
        PermissionAttachment attachment = player.addAttachment(Wasteland.getInstance());
        attachments.put(player.getUniqueId(), attachment);
        EnlistedRank rank = ranks.getRankAt(player, kills);
        for (EnlistedRank child : ranks.getTransitiveReflexivePredecessors(rank)) {
            attachment.setPermission(child.getPermission(), true);
        }
    }
    
    void createAttachment(Player player) throws Exception {
        int kills = killsStore.getPlayerKills(player);
        createAttachment(player, kills);
    }
    
    public void updatePlayerRank(Player player) throws Exception {
        removeAttachment(player);
        createAttachment(player);
    }
    
    private void updatePlayerRank(Player player, int kills) throws Exception {
        removeAttachment(player);
        createAttachment(player, kills);
    }
    
    public void register() {
        for (Player player : Wasteland.getInstance().getServer().getOnlinePlayers()) {
            try {
                createAttachment(player);
            } catch (Exception e) {
                Wasteland.getInstance().getLogger().log(Level.SEVERE, "Failed to get player's kills.", e);
            }
        }
    }
    
    public void unregister() {
        for(Map.Entry<UUID, PermissionAttachment> attachment : attachments.entrySet()) {
            Wasteland.getInstance().getServer().getPlayer(attachment.getKey())
                .removeAttachment(attachment.getValue());
            attachments.remove(attachment.getKey());
        }
        attachments.clear();
    }
    
    public class AttachmentUpdatingPlayerKillsStore implements KillsStore {
        @Override
        public int getPlayerKills(Player player) throws Exception {
            return killsStore.getPlayerKills(player);
        }

        @Override
        public void setPlayerKills(Player player, int kills) throws Exception {
            killsStore.setPlayerKills(player, kills);
            RankAttachments.this.updatePlayerRank(player, kills);
        }

        @Override
        public void addPlayerKills(Player player, int kills) throws Exception {
            killsStore.addPlayerKills(player, kills);
            RankAttachments.this.updatePlayerRank(player);
        }
    }
}
