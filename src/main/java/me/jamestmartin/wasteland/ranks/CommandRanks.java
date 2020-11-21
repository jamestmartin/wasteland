package me.jamestmartin.wasteland.ranks;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandRanks implements CommandExecutor {
    private final AllRanks ranks;
    
    public CommandRanks(AllRanks ranks) {
        this.ranks = ranks;
    }
    
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
		
		sender.sendMessage("Enlisted ranks:");
		for (EnlistedRank rank : ranks.getEnlistedRanks().getRanksList()) {
			sender.sendMessage("* " + makeElement(rank));
		}
		
		sender.sendMessage("Officer (server staff) ranks:");
		for (Rank rank : ranks.getOfficerRanks().getRanksList()) {
			sender.sendMessage("* " + makeElement(rank));
		}
		
		return true;
	}
}
