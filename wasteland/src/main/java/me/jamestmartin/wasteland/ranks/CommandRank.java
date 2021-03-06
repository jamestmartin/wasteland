package me.jamestmartin.wasteland.ranks;

import java.util.Optional;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jamestmartin.wasteland.Wasteland;
import me.jamestmartin.wasteland.kills.KillsConfig;
import me.jamestmartin.wasteland.kills.KillsProvider;

public class CommandRank implements CommandExecutor {
    private final KillsProvider killsProvider;
    private final PlayerRankProvider rankProvider;
    private final String eligibleMobsName;
    
    public CommandRank(KillsConfig killsConfig, KillsProvider killsProvider, PlayerRankProvider rankProvider) {
        this.killsProvider = killsProvider;
        this.rankProvider = rankProvider;
        this.eligibleMobsName = killsConfig.getEligibleMobsName();
    }
    
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
		String playerNameS;
		Player subject;
		
		boolean seeKills;
		if (args.length == 1) {
			subject = sender.getServer().getPlayer(args[0]);
			if (!sender.equals(subject) && !sender.hasPermission("wasteland.view-rank.other")) {
				sender.sendMessage("You do not have permission to view another player's rank.");
				return false;
			}
			if (subject == null) {
				sender.sendMessage("Unknown player: " + args[0]);
				return false;
			}
			seeKills = !sender.equals(subject) && sender.hasPermission("wasteland.view-rank.other.kills");
			playerName = subject.getDisplayName();
			has = " has ";
			is = " is ";
			need = " needs ";
			playerNameS = playerName + "'s";
		} else if (sender instanceof Player) {
			if (!sender.hasPermission("wasteland.view-rank")) {
				sender.sendMessage("You do not have permission to view your own rank.");
			}
			seeKills = sender.hasPermission("wasteland.view-rank.kills");
			subject = (Player) sender;
			playerName = "You";
			has = " have ";
			is = " are ";
			need = " need ";
			playerNameS = "Your";
		} else {
			sender.sendMessage("You are not a player! Please specify a player whose rank you want to view.");
			sender.sendMessage(command.getUsage());
			return false;
		}
		
		try {
			int kills = killsProvider.getPlayerKills(subject);
			Optional<EnlistedRank> rank = rankProvider.getEnlistedRank(subject);
			Optional<EnlistedRank> nextRank = rankProvider.getNextRank(subject);
			if (rank.isPresent()) {
				sender.sendMessage(playerName + is + "rank " + rank.get().formatFull() + ChatColor.RESET + ".");
			}
			if (seeKills) {
				String hasKilled = playerName + has + "killed " + kills + " " + eligibleMobsName;
				String andHasToGo;
				if (nextRank.isPresent()) {
					int moreZombies = nextRank.get().getKills().get() - kills;
					andHasToGo = " and" + need + "to kill " + moreZombies
							+ " more to reach " + nextRank.get().formatFull() + ChatColor.RESET + ".";
				} else {
					andHasToGo = " and" + has + "no further promotions available.";
				}
				sender.sendMessage(hasKilled + andHasToGo);
			} else if (nextRank.isPresent()) {
				sender.sendMessage(playerNameS + " next rank will be " + nextRank.get().formatExtended() + ChatColor.RESET + ".");
			} else {
				sender.sendMessage(playerName + has + "no further promotions available.");
			}
		} catch (Exception e) {
			sender.sendMessage("Command failed due to database exception. Contact the server administrator.");
			Wasteland.getInstance().getLogger().log(Level.SEVERE, "Failed to get player kills.", e);
		}
		
        return true;
    }
}
