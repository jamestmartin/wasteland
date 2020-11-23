package me.jamestmartin.wasteland.chat;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.jamestmartin.wasteland.Substate;
import me.jamestmartin.wasteland.ranks.PlayerRankProvider;

public class ChatState implements Substate {
    private final ChatListener chatListener;
    private final CommandOfficial commandOfficial;
    
    public ChatState(ChatConfig config, PlayerRankProvider rankProvider) {
        this.chatListener = new ChatListener(config, rankProvider);
        this.commandOfficial = new CommandOfficial(config, rankProvider);
    }
    
    @Override
    public void register(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(chatListener, plugin);
        plugin.getCommand("official").setExecutor(commandOfficial);
    }
    
    @Override
    public void unregister(JavaPlugin plugin) {
        AsyncPlayerChatEvent.getHandlerList().unregister(chatListener);
        plugin.getCommand("official").setExecutor(null);
    }
}
