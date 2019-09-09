package me.jtmar.wasteland.commands;

import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jtmar.wasteland.Wasteland;
import me.jtmar.wasteland.ranks.EnlistedRank;

public class CommandRank implements CommandExecutor {
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 1) {
			sender.sendMessage("Too many arguments!");
			sender.sendMessage(command.getUsage());
			return false;
		}
		
		String playerName;
		String has;
		String is;
		String need;
		Player subject;
		if (args.length == 1) {
			subject = sender.getServer().getPlayer(args[0]);
			if (!sender.equals(subject) && !sender.hasPermission("wasteland.kills.other")) {
				sender.sendMessage("You do not have permission to view another player's kills.");
				return false;
			}
			if (subject == null) {
				sender.sendMessage("Unknown player: " + args[0]);
				return false;
			}
			playerName = subject.getDisplayName();
			has = " has ";
			is = " is ";
			need = " needs ";
		} else if (sender instanceof Player) {
			if (!sender.hasPermission("wasteland.kills")) {
				sender.sendMessage("You do not have permission to view your own kills.");
			}
			subject = (Player) sender;
			playerName = "You";
			has = " have ";
			is = " are ";
			need = " need ";
		} else {
			sender.sendMessage("You are not a player! Please specify a player whose kills you want to view.");
			sender.sendMessage(command.getUsage());
			return false;
		}
		
		try {
			int kills = Wasteland.getInstance().getDatabase().getPlayerKills(subject);
			EnlistedRank rank = EnlistedRank.getRankFromKills(kills);
			EnlistedRank nextRank = EnlistedRank.getNextRank(subject);
			sender.sendMessage(playerName + is + "rank " + rank.formatFull() + ".");
			String beginString = playerName + has + "killed " + kills + " zombies";
			if (nextRank == null) {
				sender.sendMessage(beginString + ".");
			} else {
				int moreZombies = nextRank.getMinimumKills() - kills;
				sender.sendMessage(beginString + " and " + need + "to kill " + moreZombies
						+ " more to reach " + nextRank.formatFull() + ".");
			}
		} catch (SQLException e) {
			sender.sendMessage("Command failed due to database exception. Contact the server administrator.");
			Wasteland.getInstance().getLogger().log(Level.SEVERE, "Failed to get player kills.", e);
		}
		
        return true;
    }
}
