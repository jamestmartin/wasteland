package me.jtmar.wasteland.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import me.jtmar.wasteland.TownyDependency;
import me.jtmar.wasteland.Wasteland;
import me.jtmar.wasteland.ranks.EnlistedRank;

public class ChatListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		String townyPrefix = "";
		if (Wasteland.getInstance().getServer().getPluginManager().isPluginEnabled("Towny")) {
			townyPrefix = TownyDependency.getTownTagPrefix(event.getPlayer());
		}
		event.setFormat(
				townyPrefix
					+ "[" + EnlistedRank.getRank(event.getPlayer()).format() + ChatColor.RESET + "] " + " "
					+ "%s" + ChatColor.WHITE + ": %s");
	}
}
