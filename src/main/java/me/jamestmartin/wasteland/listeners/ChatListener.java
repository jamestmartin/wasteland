package me.jamestmartin.wasteland.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import me.jamestmartin.wasteland.config.ChatConfig;

public class ChatListener implements Listener {
    private final ChatConfig config;
    
    public ChatListener(final ChatConfig config) {
        this.config = config;
    }
    
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
		final Player player = event.getPlayer();
		if (player.hasPermission("wasteland.chat.officer")) {
		    event.setFormat(config.getOfficerChatFormat(player));
		} else {
		    event.setFormat(config.getPlayerChatFormat(player));
		}
	}
}
