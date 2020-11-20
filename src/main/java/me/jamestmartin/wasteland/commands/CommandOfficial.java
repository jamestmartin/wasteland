package me.jamestmartin.wasteland.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jamestmartin.wasteland.config.ChatConfig;
import me.jamestmartin.wasteland.ranks.Rank;

public class CommandOfficial implements CommandExecutor {
    private final ChatConfig config;
    
    public CommandOfficial(ChatConfig config) {
        this.config = config;
    }
    
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1) {
			sender.sendMessage("Don't you actually have something to say?");
			return false;
		}
		
		final String message = String.join(" ", args);
		
		final String format;
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			if (!Rank.getOfficerRank(player).isPresent()) {
				sender.sendMessage("You are not a staff member.");
				return true;
			}
			
			format = config.getOfficialFormat(player).replaceFirst("%s", player.getDisplayName());
		} else {
			if (!Rank.getConsoleRank().isPresent()) {
				sender.sendMessage("No console rank is configured to send messages with!");
				return true;
			}
			
			format = config.getConsoleFormat();
		}
		
		sender.getServer().broadcastMessage(format.replaceFirst("%s", message));
		return true;
	}
}
