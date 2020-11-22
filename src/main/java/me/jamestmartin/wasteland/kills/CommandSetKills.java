package me.jamestmartin.wasteland.kills;

import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jamestmartin.wasteland.Wasteland;

class CommandSetKills implements CommandExecutor {
    private final KillsStore store;
    
    public CommandSetKills(KillsStore store) {
        this.store = store;
    }
    
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("wasteland.kills.set")) {
			sender.sendMessage("You do not have permission to set a player's kills.");
			return true;
		}
		
		if (args.length < 1) {
			sender.sendMessage("You must specify the number of kills to set to.");
			return false;
		}
		if (args.length > 2) {
			sender.sendMessage("Too many arguments.");
			return false;
		}
		
		String playerNowHas;
		Player subject;
		if (args.length == 1) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("You are not a player! Don't forget to specify a player argument.");
				return false;
			}
			subject = (Player) sender;
			playerNowHas = "You now have";
		} else {
			subject = sender.getServer().getPlayer(args[0]);
			if (subject == null) {
				sender.sendMessage("Unknown player: " + args[0]);
				return false;
			}
			playerNowHas = subject.getDisplayName() + " now has";
		}

		String killsArg;
		if (args.length == 1) {
			killsArg = args[0];
		} else {
			killsArg = args[1];
		}
		
		int kills;
		try {
			kills = Integer.valueOf(killsArg);
		} catch (NumberFormatException e) {
			sender.sendMessage("Not a valid number of kills: " + killsArg);
			return false;
		}
		
		if (kills < 0) {
			sender.sendMessage("The number of kills must not be negative!");
			return false;
		}
		
		try {
		    int previousKills = store.getPlayerKills(subject);
			store.setPlayerKills(subject, kills);
			sender.sendMessage(playerNowHas + " " + kills + " kills.");
			Wasteland.getInstance().getLogger().info(sender.getName() + " has changed the number of kills " + subject.getName() + " has from " + previousKills + " to " + kills);
		} catch (Exception e) {
			Wasteland.getInstance().getLogger().log(Level.SEVERE, "Failed to set player kills.", e);
			sender.sendMessage("ERROR: Failed to update player kills. Please notify a server administrator.");
		}
		
		return true;
	}
}
