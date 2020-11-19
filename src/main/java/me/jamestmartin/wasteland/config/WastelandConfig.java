package me.jamestmartin.wasteland.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import me.jamestmartin.wasteland.ranks.EnlistedRank;
import me.jamestmartin.wasteland.ranks.Rank;
import me.jamestmartin.wasteland.spawns.MonsterType;

public class WastelandConfig {
	private final String databaseFile;
	private final boolean prefixTownTag;
	private final Optional<ChatColor> prefixTownTagColor;
	
	private final boolean preferOfficerRank, bracketChatRank, nameUsesRankColor;
	
	private final Collection<EnlistedRank> enlistedRanks;
	private final Collection<Rank> officerRanks;
	private final Optional<Rank> consoleRank;
	
	private final Set<EntityType> eligibleMobs;
	private final String eligibleMobsName;

	private final Map<MonsterType, MonsterSpawnConfig> spawns;

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
		
		List<String> eligibleMobTypes = c.getStringList("eligibleMobs");
		this.eligibleMobs = new HashSet<>();
		for (String mobType : eligibleMobTypes) {
		    this.eligibleMobs.addAll(Arrays.asList(EntityTypes.lookupEntityType(mobType)));
		}

		this.eligibleMobsName = c.getString("eligibleMobsName");

		ConfigurationSection mss = c.getConfigurationSection("spawns");
		this.spawns = new HashMap<>();
		if (mss != null) {
		    for (String typeName : mss.getKeys(false)) {
		        MonsterType type = MonsterType.valueOf(typeName);
		        MonsterSpawnConfig config = new MonsterSpawnConfig(mss.getConfigurationSection(typeName));
		        this.spawns.put(type, config);
		    }
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
	
	/** The entity types which, if killed, will count towards your enlisted rank. */
	public Set<EntityType> eligibleMobs() { return this.eligibleMobs; }
	/** The term for the eligible mobs, e.g. "zombies" or "hostile mobs". */
	public String eligibleMobsName() { return this.eligibleMobsName; }
	
	public Map<MonsterType, MonsterSpawnConfig> spawns() { return this.spawns; }
}
