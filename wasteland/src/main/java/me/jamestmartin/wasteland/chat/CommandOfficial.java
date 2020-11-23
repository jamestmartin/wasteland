package me.jamestmartin.wasteland.chat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jamestmartin.wasteland.ranks.PlayerRankProvider;
import me.jamestmartin.wasteland.ranks.PlayerRanks;

class CommandOfficial implements CommandExecutor {
    private final PlayerRankProvider ranks;
    private final ChatFormatter formatter;
    
    public CommandOfficial(ChatFormatter formatter, PlayerRankProvider ranks) {
        this.formatter = formatter;
        this.ranks = ranks;
    }
    
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1) {
			sender.sendMessage("Don't you actually have something to say?");
			return false;
		}
		
		String message = String.join(" ", args);
		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			PlayerRanks playerRanks = ranks.getPlayerRanks(player);
			if (!playerRanks.getOfficerRank().isPresent()) {
				sender.sendMessage("You are not an officer.");
				return true;
			}
			
			sender.getServer().broadcastMessage(
			        String.format(formatter.formatOfficial(player, playerRanks), player.getDisplayName(), message));
		} else {
			sender.getServer().broadcastMessage(
			        String.format(formatter.formatConsole(ranks.getRanks().getConsoleRank()), message));
		}
		
		return true;
	}
}
