package me.jtmar.wasteland.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import me.jtmar.wasteland.ranks.EnlistedRank;
import me.jtmar.wasteland.ranks.Rank;

public class WastelandConfig {
	private final String databaseFile;
	private final boolean prefixTownTag;
	private final Optional<ChatColor> prefixTownTagColor;
	
	private final boolean preferOfficerRank, bracketChatRank, nameUsesRankColor;
	
	private final Collection<EnlistedRank> enlistedRanks;
	private final Collection<Rank> officerRanks;
	private final Optional<Rank> consoleRank;
	
	/** Orphaned method. */
	public static Optional<ChatColor> readColor(ConfigurationSection c, String path) {
		return (Optional<ChatColor>) Optional.ofNullable(c.getString(path)).map(ChatColor::valueOf);
	}
	
	public WastelandConfig(ConfigurationSection c) {
		this.databaseFile = c.getString("databaseFile", "wasteland.sqlite3");
		this.prefixTownTag = c.getBoolean("prefixTownTag", true);
		this.prefixTownTagColor = readColor(c, "prefixTownTagColor");
		this.preferOfficerRank = c.getBoolean("preferOfficerRank", false);
		this.bracketChatRank = c.getBoolean("bracketChatRank", true);
		this.nameUsesRankColor = c.getBoolean("nameUsesRankColor", false);
		
		
		ConfigurationSection ers = c.getConfigurationSection("enlistedRanks");
		ArrayList<EnlistedRank> enlistedRanks = new ArrayList<>();
		this.enlistedRanks = enlistedRanks;
		if (ers != null) {
			Optional<ChatColor> defaultDecoration = readColor(c, "enlistedRankDefaultDecoration");
			Optional<String> defaultDescription =
					Optional.ofNullable(c.getString("enlistedRankDefaultDescription"));
			
			Set<String> rankIDs = ers.getKeys(false);
			enlistedRanks.ensureCapacity(rankIDs.size());
			for (String id : rankIDs) {
				EnlistedRank result = new EnlistedRank(defaultDescription, defaultDecoration,
						ers.getConfigurationSection(id));
				enlistedRanks.add(result);
			}
			enlistedRanks.sort(new EnlistedRank.EnlistedRankComparator(enlistedRanks));
		}
		
		ConfigurationSection ors = c.getConfigurationSection("officerRanks");
		ArrayList<Rank> officerRanks = new ArrayList<>();
		this.officerRanks = officerRanks;
		if (ors != null) {
			Optional<ChatColor> defaultDecoration = readColor(c, "officerRankDefaultDecoration");
			
			Set<String> rankIDs = ors.getKeys(false);
			officerRanks.ensureCapacity(rankIDs.size());
			for (String id : rankIDs) {
				ConfigurationSection rank = ors.getConfigurationSection(id);
				Rank result = new Rank(Optional.empty(), defaultDecoration, rank);
				officerRanks.add(result);
			}
			officerRanks.sort(new Rank.RankComparator(officerRanks));
		}
		
		ConfigurationSection crs = c.getConfigurationSection("consoleRank");
		if (crs == null) {
			this.consoleRank = Optional.empty();
		} else {
			this.consoleRank = Optional.of(new Rank(Optional.empty(), Optional.empty(), crs));
		}
	}
	
	public String databaseFile() { return this.databaseFile; }
	
	public boolean prefixTownTag() { return this.prefixTownTag; }
	public Optional<ChatColor> prefixTownTagColor() { return this.prefixTownTagColor; }
	
	public boolean preferOfficerRank() { return this.preferOfficerRank; }
	public boolean bracketChatRank() { return this.bracketChatRank; }
	public boolean nameUsesRankColor() { return this.nameUsesRankColor; }
	
	public Collection<EnlistedRank> enlistedRanks() { return this.enlistedRanks; }
	public Collection<Rank> officerRanks() { return this.officerRanks; }
	public Optional<Rank> consoleRank() { return this.consoleRank; }
}
