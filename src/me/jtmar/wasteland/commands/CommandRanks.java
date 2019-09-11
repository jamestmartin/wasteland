package me.jtmar.wasteland.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.jtmar.wasteland.Wasteland;
import me.jtmar.wasteland.config.WastelandConfig;
import me.jtmar.wasteland.ranks.EnlistedRank;
import me.jtmar.wasteland.ranks.Rank;

public class CommandRanks implements CommandExecutor {
	private String makeElement(Rank rank) {
		if (rank.getDescription().isPresent())
			return rank.formatExtended() + ": " + rank.getDescription().get();
		return rank.formatExtended();
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			sender.sendMessage("Too many arguments.");
			return false;
		}
		
		WastelandConfig config = Wasteland.getInstance().getSettings();
		
		sender.sendMessage("Enlisted ranks:");
		for (EnlistedRank rank : config.enlistedRanks()) {
			sender.sendMessage("* " + makeElement(rank));
		}
		
		sender.sendMessage("Officer (server staff) ranks:");
		for (Rank rank : config.officerRanks()) {
			sender.sendMessage("* " + makeElement(rank));
		}
		
		if (config.consoleRank().isPresent()) {
			sender.sendMessage("* " + makeElement(config.consoleRank().get()));
		}
		return true;
	}
}
