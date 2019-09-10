package me.jtmar.wasteland;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;

public class TownyDependency {
	public static String getTownTagPrefix(Player player) {
		try {
			Resident resident = TownyUniverse.getDataSource().getResident(player.getName());
			String tag = resident.getTown().getTag();
			if (tag != null && !tag.equals("")) {
				return "[" + ChatColor.BLUE + tag + ChatColor.RESET + "] ";
			}
		} catch (NotRegisteredException e) {
		}
		return "";
	}
}
