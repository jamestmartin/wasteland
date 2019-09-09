package me.jtmar.wasteland.ranks;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public interface Rank {
	String getAbbreviation();
	String getFullName();
	ChatColor getColor();
	public default ChatColor getDecoration() {
		return null;
	}
	
	public default String format() {
		ChatColor decoration = getDecoration();
		if (decoration != null) {
			return getColor() + decoration.toString() + getAbbreviation();
		}
		return getColor() + getAbbreviation();
	}
	
	public default String formatFull() {
		ChatColor decoration = getDecoration();
		if (decoration != null) {
			return getColor() + decoration.toString() + getFullName();
		}
		return getColor() + getFullName();
	}
	
	public default String formatExtended() {
		return formatFull() + ChatColor.RESET + " (" + format() + ChatColor.RESET + ")";
	}
	
	/**
	 * @param player
	 * @return Does the player have at least this permission?
	 */
	public default boolean hasRank(Player player) {
		return player.hasPermission(getPermission());
	}
	
	public default String getPermission() {
		return "wasteland.rank." + getAbbreviation().toLowerCase();
	}
	
	public static Rank getRank(Player player) {
		OfficerRank rank = OfficerRank.getRank(player);
		if (rank != null) return rank;
		return EnlistedRank.getRank(player);
	}
}
