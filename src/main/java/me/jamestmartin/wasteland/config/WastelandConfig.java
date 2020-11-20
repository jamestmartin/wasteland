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
	
	private final ChatConfig chat;
	
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
		
		this.chat = new ChatConfig(c.getConfigurationSection("chat"));
		
		ConfigurationSection enlistedSection = c.getConfigurationSection("enlisted");
		ArrayList<EnlistedRank> enlistedRanks = new ArrayList<>();
		this.enlistedRanks = enlistedRanks;
        this.eligibleMobs = new HashSet<>();
		if (enlistedSection != null) {
			Optional<ChatColor> defaultDecoration = readColor(enlistedSection, "decoration");
			Optional<String> defaultDescription =
					Optional.ofNullable(enlistedSection.getString("description"));
			
			ConfigurationSection enlistedRanksSection = enlistedSection.getConfigurationSection("ranks");
			Set<String> rankIDs = enlistedRanksSection.getKeys(false);
			enlistedRanks.ensureCapacity(rankIDs.size());
			for (String id : rankIDs) {
				EnlistedRank result = new EnlistedRank(defaultDescription, defaultDecoration,
				        enlistedRanksSection.getConfigurationSection(id));
				enlistedRanks.add(result);
			}
			enlistedRanks.sort(new EnlistedRank.EnlistedRankComparator(enlistedRanks));
			

	        ConfigurationSection promotionSection = enlistedSection.getConfigurationSection("promotions");
	        ConfigurationSection eligibleSection = promotionSection.getConfigurationSection("eligible");
	        
	        List<String> eligibleMobTypes = eligibleSection.getStringList("entities");
	        for (String mobType : eligibleMobTypes) {
	            this.eligibleMobs.addAll(Arrays.asList(EntityTypes.lookupEntityType(mobType)));
	        }

	        this.eligibleMobsName = eligibleSection.getString("name");
		} else {
		    this.eligibleMobsName = "nothing";
		}
		
		ConfigurationSection officerSection = c.getConfigurationSection("officer");
		ArrayList<Rank> officerRanks = new ArrayList<>();
		this.officerRanks = officerRanks;
		if (officerSection != null) {
			Optional<ChatColor> defaultDecoration = readColor(officerSection, "decoration");
			
			ConfigurationSection officerRanksSection = officerSection.getConfigurationSection("ranks");
			Set<String> rankIDs = officerRanksSection.getKeys(false);
			officerRanks.ensureCapacity(rankIDs.size());
			for (String id : rankIDs) {
				ConfigurationSection rank = officerRanksSection.getConfigurationSection(id);
				Rank result = new Rank(Optional.empty(), defaultDecoration, rank);
				officerRanks.add(result);
			}
			officerRanks.sort(new Rank.RankComparator(officerRanks));
			
			String consoleRankID = officerSection.getString("console", null);
			if (consoleRankID == null) {
			    this.consoleRank = Optional.of(officerRanks.get(officerRanks.size() - 1));
			} else {
			    this.consoleRank = Optional.of(officerRanks.stream().filter(rank -> rank.getId().equals(consoleRankID)).findFirst().get());
			}
		} else {
		    this.consoleRank = Optional.empty();
		}

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
	
	public ChatConfig chat() { return this.chat; }
	
	public Collection<EnlistedRank> enlistedRanks() { return this.enlistedRanks; }
	public Collection<Rank> officerRanks() { return this.officerRanks; }
	public Optional<Rank> consoleRank() { return this.consoleRank; }
	
	/** The entity types which, if killed, will count towards your enlisted rank. */
	public Set<EntityType> eligibleMobs() { return this.eligibleMobs; }
	/** The term for the eligible mobs, e.g. "zombies" or "hostile mobs". */
	public String eligibleMobsName() { return this.eligibleMobsName; }
	
	public Map<MonsterType, MonsterSpawnConfig> spawns() { return this.spawns; }
}
