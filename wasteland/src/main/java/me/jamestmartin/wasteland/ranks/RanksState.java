package me.jamestmartin.wasteland.ranks;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.jamestmartin.wasteland.Substate;
import me.jamestmartin.wasteland.kills.KillsConfig;
import me.jamestmartin.wasteland.kills.KillsStore;

public class RanksState implements Substate {
    private final RankAttachmentsListener rankAttachmentsListener;
    
    private final CommandRank commandRank;
    private final CommandRanks commandRanks;
    
    private final RankAttachments rankAttachments;
    private final KillsStore killsStore;
    
    public RanksState(KillsConfig killsConfig, KillsStore killsStore, PlayerRankProvider rankProvider) {
        this.rankAttachments = new RankAttachments(rankProvider.getRanks().getEnlistedRanks(), killsStore);
        this.rankAttachmentsListener = new RankAttachmentsListener(rankAttachments);
        this.killsStore = rankAttachments.new AttachmentUpdatingPlayerKillsStore();
        
        this.commandRank = new CommandRank(killsConfig, this.killsStore, rankProvider);
        this.commandRanks = new CommandRanks(rankProvider.getRanks());
    }
    
    public KillsStore getPlayerKillsStore() {
        return killsStore;
    }

    @Override
    public void register(JavaPlugin plugin) {
        rankAttachments.register();
        plugin.getServer().getPluginManager().registerEvents(rankAttachmentsListener, plugin);
        
        plugin.getCommand("rank").setExecutor(commandRank);
        plugin.getCommand("ranks").setExecutor(commandRanks);
    }

    @Override
    public void unregister(JavaPlugin plugin) {
        PlayerJoinEvent.getHandlerList().unregister(rankAttachmentsListener);
        PlayerQuitEvent.getHandlerList().unregister(rankAttachmentsListener);
        
        plugin.getCommand("rank").setExecutor(null);
        plugin.getCommand("ranks").setExecutor(null);
        
        rankAttachments.unregister();
    }
}
