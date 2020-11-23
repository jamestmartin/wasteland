package me.jamestmartin.wasteland.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.jamestmartin.wasteland.ranks.PlayerRankProvider;

class ChatListener implements Listener {
    private final ChatFormatter formatter;
    private final PlayerRankProvider ranks;
    
    public ChatListener(ChatFormatter formatter, PlayerRankProvider ranks) {
        this.formatter = formatter;
        this.ranks = ranks;
    }
    
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		final Player player = event.getPlayer();
		if (player.hasPermission("wasteland.chat.officer")) {
		    event.setFormat(formatter.formatOfficerChat(player, ranks.getPlayerRanks(player)));
		} else {
		    event.setFormat(formatter.formatPlayerChat(player, ranks.getPlayerRanks(player)));
		}
	}
}
