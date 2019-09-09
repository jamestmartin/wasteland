package me.jtmar.wasteland.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.jtmar.wasteland.ranks.EnlistedRank;
import me.jtmar.wasteland.ranks.OfficerRank;
import net.md_5.bungee.api.ChatColor;

public class CommandRanks implements CommandExecutor {
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			sender.sendMessage("Too many arguments.");
			return false;
		}
		
		sender.sendMessage("Enlisted ranks:");
		for (EnlistedRank rank : EnlistedRank.values()) {
			sender.sendMessage("* " + rank.formatFull() + ": " + rank.getMinimumKills() + " kills");
		}
		
		sender.sendMessage("Officer (server staff) ranks:");
		for (OfficerRank rank : OfficerRank.values()) {
			sender.sendMessage("* " + rank.formatFull());
		}
		sender.sendMessage("* " + ChatColor.GOLD + ChatColor.BOLD.toString() + "General of the Armies");
		return true;
	}
}
