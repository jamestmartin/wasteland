package me.jamestmartin.wasteland.commands;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jamestmartin.wasteland.Wasteland;
import me.jamestmartin.wasteland.ranks.Rank;

public class CommandOfficial implements CommandExecutor {
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1) {
			sender.sendMessage("Don't you actually have something to say?");
			return false;
		}
		
		String senderFormat;
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Optional<Rank> rank = Rank.getOfficerRank(player);
			if (!rank.isPresent()) {
				sender.sendMessage("You are not a staff member.");
				return true;
			}
			String rankFormat = rank.get().formatAbbreviated() + ChatColor.RESET;
			if (Wasteland.getInstance().getSettings().bracketChatRank()) {
				rankFormat = ChatColor.RESET + "[" + rankFormat + "]";
			}
			senderFormat = rankFormat + " " + player.getDisplayName();
		} else {
			Optional<Rank> consoleRank = Wasteland.getInstance().getSettings().consoleRank();
			if (!consoleRank.isPresent()) {
				sender.sendMessage("No console rank is configured to send messages with!");
				return true;
			}
			senderFormat = consoleRank.get().formatFull();
		}
		
		sender.getServer().broadcastMessage(senderFormat + ChatColor.RESET + ": " + String.join(" ", args));
		return true;
	}
}
