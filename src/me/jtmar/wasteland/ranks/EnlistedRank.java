package me.jtmar.wasteland.ranks;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum EnlistedRank implements Rank {
	FODDER(     0, "Fodder", "Zombie Fodder"),
	PVT   (   100, "Pvt",    "Private"),
	PFC   (   250, "Pfc",    "Private First Class"),
	LCPL  (   500, "LCpl",   "Lance Corporal"),
	CPL   ( 1_000, "Cpl",    "Corporal"),
	SGT   ( 2_500, "Sgt",    "Sergeant"),
	SSGT  ( 5_000, "SSgt",   "Staff Sergeant"),
	GYSGT (10_000, "GySgt",  "Gunnery Sergeant"),
	MSGT  (25_000, "MSgt",   "Master Sergeant"),
	MGYSGT(50_000, "MGySgt", "Master Gunnery Sergeant");
	
	private final int minKills;
	private final String abbreviation;
	private final String fullName;
	
	private EnlistedRank(int minKills, String abbreviation, String fullName) {
		if(minKills < 0)
			throw new IllegalArgumentException("Number of kills must not be negative: " + minKills);
		this.minKills = minKills;
		this.abbreviation = abbreviation;
		this.fullName = fullName;
	}
	
	public static EnlistedRank getRankFromKills(int kills) {
		if (kills < 0)
			throw new IllegalArgumentException("Number of kills must not be negative: " + kills);
		EnlistedRank result = null;
		for(EnlistedRank rank : EnlistedRank.values()) {
			if (kills < rank.getMinimumKills()) break;
			result = rank;
		}
		if (result == null) throw new IllegalStateException("Null KillsRank.");
		return result;
	}
	
	public static EnlistedRank getRank(Player player) {
		EnlistedRank result = FODDER;
		for (EnlistedRank rank : EnlistedRank.values()) {
			if (player.hasPermission(rank.getPermission())) {
				result = rank;
			}
		}
		return result;
	}
	
	/**
	 * @param player
	 * @return Returns null if the player is at the maximum rank.
	 */
	public static EnlistedRank getNextRank(Player player) {
		EnlistedRank nextRank = null;
		for (EnlistedRank rank : EnlistedRank.values()) {
			if (!player.hasPermission(rank.getPermission())) {
				nextRank = rank;
				break;
			}
		}
		return nextRank;
	}
	
	public int getMinimumKills() {
		return this.minKills;
	}
	
	@Override
	public String getAbbreviation() {
		return this.abbreviation;
	}
	
	@Override
	public String getFullName() {
		return this.fullName;
	}
	
	@Override
	public String toString() {
		return this.getAbbreviation();
	}

	@Override
	public ChatColor getColor() {
		switch (this) {
		case FODDER:
			return ChatColor.GRAY;
		case PVT:
			return ChatColor.WHITE;
		case PFC:
			return ChatColor.BLUE;
		case LCPL:
			return ChatColor.GREEN;
		case CPL:
			return ChatColor.RED;
		case SGT:
			return ChatColor.LIGHT_PURPLE;
		case SSGT:
			return ChatColor.YELLOW;
		case GYSGT:
			return ChatColor.AQUA;
		case MSGT:
			return ChatColor.DARK_RED;
		case MGYSGT:
			return ChatColor.DARK_PURPLE;
		}
		throw new IllegalStateException();
	}
}
