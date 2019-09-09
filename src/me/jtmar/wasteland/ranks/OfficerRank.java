package me.jtmar.wasteland.ranks;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum OfficerRank implements Rank {
	SNDLT ("2ndLt",  "Second Lieutenant"),
	FSTLT ("1stLt",  "First Lieutenant"),
	CAPT  ("Capt",   "Captain"),
	MAJ   ("Maj",    "Major"),
	LTCOL ("LtCol",  "Lieutenant Colonel"),
	COL   ("Col",    "Colonel"),
	BGEN  ("BGen",   "Brigadier General"),
	MAJGEN("MajGen", "Major General"),
	LTGEN ("LtGen",  "Lieutenant General"),
	GEN   ("Gen",    "General");
	
	private final String abbreviation;
	private final String fullName;
	
	private OfficerRank(String abbreviation, String fullName) {
		this.abbreviation = abbreviation;
		this.fullName = fullName;
	}
	
	/**
	 * @param player
	 * @return Returns `null` if the player is not staff.
	 */
	public static OfficerRank getRank(Player player) {
		OfficerRank result = null;
		for (OfficerRank rank : OfficerRank.values()) {
			if (player.hasPermission(rank.getPermission())) {
				result = rank;
			}
		}
		return result;
	}

	@Override
	public String getAbbreviation() {
		return abbreviation;
	}

	@Override
	public String getFullName() {
		return fullName;
	}
	
	@Override
	public String toString() {
		return this.getAbbreviation();
	}

	@Override
	public ChatColor getColor() {
		switch (this) {
		case SNDLT:
			return ChatColor.WHITE;
		case FSTLT:
			return ChatColor.BLUE;
		case CAPT:
			return ChatColor.GREEN;
		case MAJ:
			return ChatColor.RED;
		case LTCOL:
			return ChatColor.LIGHT_PURPLE;
		case COL:
			return ChatColor.YELLOW;
		case BGEN:
			return ChatColor.AQUA;
		case MAJGEN:
			return ChatColor.DARK_RED;
		case LTGEN:
			return ChatColor.DARK_PURPLE;
		case GEN:
			return ChatColor.GOLD;
		}
		throw new IllegalStateException();
	}
	
	@Override
	public ChatColor getDecoration() {
		return ChatColor.BOLD;
	}
}
