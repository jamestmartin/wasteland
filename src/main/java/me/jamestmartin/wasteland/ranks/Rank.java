package me.jamestmartin.wasteland.ranks;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import me.jamestmartin.wasteland.Wasteland;
import me.jamestmartin.wasteland.config.WastelandConfig;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class Rank {
	private final String id, abbreviation, name;
	private final Optional<String> description;
	private final Optional<ChatColor> color, decoration;
	private final Permission permission;
	private final Optional<String> predecessor;
	private final boolean preferred;
	
	public Rank(Optional<String> defaultDescription, Optional<ChatColor> defaultDecoration,
			ConfigurationSection c) {
		this.id = c.getName();
		this.name = c.getString("name", id);
		this.abbreviation = c.getString("abbreviation", name);
		Optional<String> description = Optional.ofNullable(c.getString("description"));
		if (!description.isPresent()) this.description = defaultDescription;
		else this.description = description;
		this.color = WastelandConfig.readColor(c, "color");
		Optional<ChatColor> decoration = WastelandConfig.readColor(c, "decoration");
		if (!decoration.isPresent()) this.decoration = defaultDecoration;
		else this.decoration = decoration;
		this.predecessor = Optional.ofNullable(c.getString("succeeds"));
		this.preferred = c.getBoolean("preferred", true);
		Map<String, Boolean> children = new HashMap<>();
		if (predecessor.isPresent()) {
			children.put(predecessor.get(), true);
		}
		this.permission =
				new Permission("wasteland.rank." + id, description.orElse(null), PermissionDefault.FALSE, children);
	}
	
	public String getId() { return this.id; }
	public Permission getPermission() { return this.permission; }
	public String getAbbreviation() { return this.abbreviation; }
	public String getFullName() { return this.name; }
	public Optional<String> getDescription() { return description; }
	public Optional<ChatColor> getColor() { return this.color; }
	public Optional<ChatColor> getDecoration() { return this.decoration; }
	public Optional<String> getPredecessor() { return this.predecessor; }
	public boolean isPreferred() { return this.preferred; }
	
	public boolean isSuccessorOf(Collection<Rank> ranks, Rank other) {
		if (this == other) return false;
		if (this.getId().equals(other.getId())) return false;
		if (!this.getPredecessor().isPresent()) return false;
		if (this.getPredecessor().get().equals(other.getId())) return true;
		String predecessorId = this.getPredecessor().get();
		for (Rank predecessor : ranks) {
			if (predecessor.getId().equals(predecessorId))
				return predecessor.isSuccessorOf(ranks, other);
		}
		throw new IllegalArgumentException("Predecessor rank " + predecessorId + " not found.");
	}
	
	public final String getFormat() {
		String color = this.color.map(ChatColor::toString).orElse("");
		String decoration = this.decoration.map(ChatColor::toString).orElse("");
		return color + decoration;
	}
	
	/** The rank abbreviation with chat formatting codes. */
	public final String formatAbbreviated() {
		return getFormat() + getAbbreviation();
	}
	
	/** The rank name with chat formatting codes. */
	public final String formatFull() {
		return getFormat() + getFullName();
	}
	
	/** `Rank Name (RankAbbr)` with chat formatting codes. */
	public final String formatExtended() {
		return formatFull() + ChatColor.RESET + " (" + formatAbbreviated() + ChatColor.RESET + ")";
	}
	
	/**
	 * @param player
	 * @return Does the player have this rank?
	 */
	public final boolean hasRank(Player player) {
		return player.hasPermission(getPermission());
	}
	
	@Override
	public String toString() {
		return this.abbreviation;
	}
	
	public static Optional<Rank> getOfficerRank(Player player) {
		Rank result = null;
		for (Rank rank : Wasteland.getInstance().getSettings().officerRanks()) {
			if (player.hasPermission(rank.getPermission())) result = rank;
		}
		return Optional.ofNullable(result);
	}
	
	public static Optional<Rank> getHighestRank(Player player) {
		Optional<Rank> officerRank = getOfficerRank(player);
		if (officerRank.isPresent()) return officerRank;
		return EnlistedRank.getEnlistedRank(player).map(x -> (Rank) x);
	}
	
	public static class RankComparator implements Comparator<Rank> {
		private final Collection<Rank> ranks;
		
		public RankComparator(Collection<Rank> ranks) { this.ranks = ranks; }

		@Override
		public int compare(Rank a, Rank b) {
			if (a == b) return 0;
			if (a.getId().equals(b.getId())) return 0;
			if (a.isSuccessorOf(ranks, b)) return 1;
			if (b.isSuccessorOf(ranks, a)) return -1;
			// incomparable
			return 0;
		}
	}
}
