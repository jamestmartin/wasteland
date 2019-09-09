package me.jtmar.wasteland.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jtmar.wasteland.ranks.OfficerRank;
import net.md_5.bungee.api.ChatColor;

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
			OfficerRank rank = OfficerRank.getRank(player);
			if (rank == null) {
				sender.sendMessage("You are not a staff member.");
				return true;
			}
			senderFormat = "[" + rank.format() + ChatColor.RESET + "] " + player.getDisplayName();
		} else {
			senderFormat = ChatColor.GOLD + ChatColor.BOLD.toString() + "The General of the Armies";
		}
		
		sender.getServer().broadcastMessage(senderFormat + ChatColor.RESET + ": " + ChatColor.ITALIC + String.join(" ", args));
		return true;
	}
}
