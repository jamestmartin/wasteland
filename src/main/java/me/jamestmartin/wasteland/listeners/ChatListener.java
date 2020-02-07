package me.jamestmartin.wasteland.listeners;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import me.jamestmartin.wasteland.Wasteland;
import me.jamestmartin.wasteland.ranks.EnlistedRank;
import me.jamestmartin.wasteland.ranks.Rank;

public class ChatListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		
		Optional<String> townyPrefixQ = Wasteland.getInstance().getTownyPrefix().getPrefix(player);
		String townyPrefix = townyPrefixQ.map(x -> x + " ").orElse("");
		Optional<Rank> rank;
		if (Wasteland.getInstance().getSettings().preferOfficerRank()
				|| player.hasPermission("wasteland.chat.officer")) {
			rank = Rank.getHighestRank(player);
		} else {
			rank = EnlistedRank.getEnlistedRank(player).map(x -> (Rank) x);
		}
		
		String rankPrefix;
		if (rank.isPresent()) {
			rankPrefix = rank.get().formatAbbreviated();
			if (Wasteland.getInstance().getSettings().bracketChatRank())
				rankPrefix = ChatColor.RESET + "[" + rankPrefix + ChatColor.RESET + "]";
			rankPrefix = rankPrefix + " ";
		} else {
			rankPrefix = "";
		}
		
		event.setFormat(townyPrefix + rankPrefix + "%s" + ChatColor.WHITE + ": %s");
	}
}
