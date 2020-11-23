package me.jamestmartin.wasteland.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import me.jamestmartin.wasteland.WastelandConfig;
import me.jamestmartin.wasteland.chat.ChatConfig;
import me.jamestmartin.wasteland.kills.KillsConfig;
import me.jamestmartin.wasteland.kit.KitConfig;
import me.jamestmartin.wasteland.ranks.AllRanks;
import me.jamestmartin.wasteland.ranks.EnlistedRank;
import me.jamestmartin.wasteland.ranks.EnlistedRanks;
import me.jamestmartin.wasteland.ranks.Rank;
import me.jamestmartin.wasteland.ranks.Ranks;
import me.jamestmartin.wasteland.spawns.MonsterSpawnConfig;
import me.jamestmartin.wasteland.spawns.MonsterType;
import me.jamestmartin.wasteland.spawns.SpawnsConfig;
import me.jamestmartin.wasteland.util.Pair;
import me.jamestmartin.wasteland.world.MoonPhase;

public class ConfigParser {
    public static WastelandConfig parseConfig(ConfigurationSection c) {
        String databaseFilename = c.getString("databaseFile", "wasteland.sqlite3");
        ChatConfig chatConfig = parseChatConfig(c.getConfigurationSection("chat"));
        KillsConfig killsConfig = parseKillsConfig(c.getConfigurationSection("kills"));
        SpawnsConfig spawnsConfig = parseSpawnsConfig(c.getConfigurationSection("spawns"));
        KitConfig kitConfig = parseKitConfig(c.getConfigurationSection("kit"));
        
        ConfigurationSection enlistedSection = c.getConfigurationSection("enlisted");
        ConfigurationSection officerSection = c.getConfigurationSection("officer");
        AllRanks ranks = parseRanks(enlistedSection, officerSection);
        
        return new WastelandConfig(
                databaseFilename,
                chatConfig,
                killsConfig,
                ranks,
                spawnsConfig,
                kitConfig);
    }
    
    private static ChatConfig parseChatConfig(ConfigurationSection c) {
        final ConfigurationSection formats = c.getConfigurationSection("formats");
        final String chatFormat = formats.getString("chat");
        final String officerChatFormat = formats.getString("officer");
        final String officialFormat = formats.getString("official");
        final String consoleFormat = formats.getString("console");
        
        final ConfigurationSection prefixes = c.getConfigurationSection("prefixes");
        final String rankPrefixFormat = prefixes.getString("rank");
        final String townyPrefixFormat = prefixes.getString("towny");
        
        return new ChatConfig(chatFormat, officerChatFormat, officialFormat, consoleFormat, rankPrefixFormat, townyPrefixFormat);
    }
    
    private static KillsConfig parseKillsConfig(ConfigurationSection c) {
        final ConfigurationSection eligibleSection = c.getConfigurationSection("eligible");
        final String eligibleMobsName = eligibleSection.getString("name");
        
        final Set<EntityType> eligibleMobs = new HashSet<>();
        final List<String> eligibleMobTypes = eligibleSection.getStringList("entities");
        for (final String mobType : eligibleMobTypes) {
            eligibleMobs.addAll(Arrays.asList(EntityTypes.lookupEntityType(mobType)));
        }
        
        return new KillsConfig(eligibleMobsName, eligibleMobs);
    }
    
    private static AllRanks parseRanks(ConfigurationSection enlisted, ConfigurationSection officer) {
        EnlistedRanks enlistedRanks = parseEnlistedRanks(enlisted);
        Ranks<Rank> officerRanks = parseOfficerRanks(officer);
        Rank consoleRank = officerRanks.getRank(officer.getString("console")).get();
        
        return new AllRanks(enlistedRanks, officerRanks, consoleRank);
    }
    
    private static EnlistedRanks parseEnlistedRanks(ConfigurationSection c) {
        Optional<ChatColor> defaultDecoration = readColor(c, "decoration");
        Optional<String> defaultDescription = Optional.ofNullable(c.getString("description"));
        
        List<EnlistedRank> originalOrder = new ArrayList<>();
        Map<String, EnlistedRank> ranks = new HashMap<>();
        Map<String, String> predecessors = new HashMap<>();
        Map<String, String> preferredSuccessors = new HashMap<>();
        
        ConfigurationSection ranksSection = c.getConfigurationSection("ranks");
        for (String id : ranksSection.getKeys(false)) {
            Pair<EnlistedRank, Pair<Optional<String>, Optional<String>>> parse =
                    parseEnlistedRank(ranksSection.getConfigurationSection(id), id, defaultDecoration, defaultDescription);
            EnlistedRank rank = parse.x;
            Optional<String> predecessor = parse.y.x;
            Optional<String> preferredSuccessor = parse.y.y;
            
            originalOrder.add(rank);
            ranks.put(id, rank);
            predecessor.ifPresent(x -> predecessors.put(id, x));
            preferredSuccessor.ifPresent(x -> preferredSuccessors.put(id, x));
        }
        
        for (Entry<String, String> entry : predecessors.entrySet()) {
            String rank = entry.getKey();
            String predecessor = entry.getValue();
            // Read: There is only one rank whose predecessor is this rank's predecessor,
            // i.e. the predecessor has only this rank as a successor.
            if (predecessors.values().stream().filter(predecessor::equals).count() == 1) {
                // If there is only one successor, that successor will be preferred automatically.
                // TODO: Allow explicitly *not* making the only successor preferred.
                preferredSuccessors.put(predecessor, rank);
            }
        }
        
        // The commented code should be used instead once Guava is upgraded to 28+.
        // ImmutableValueGraph.Builder<EnlistedRank, Boolean> builder = ValueGraphBuilder.directed().immutable();
        MutableValueGraph<EnlistedRank, Boolean> builder = ValueGraphBuilder.directed().build();
        for (EnlistedRank rank : ranks.values()) {
            builder.addNode(rank);
        }
        
        for (Entry<String, String> entry : predecessors.entrySet()) {
            EnlistedRank successor = ranks.get(entry.getKey());
            EnlistedRank predecessor = ranks.get(entry.getValue());
            boolean preferred = successor.getId().equals(preferredSuccessors.get(predecessor.getId()));
            builder.putEdgeValue(predecessor, successor, preferred);
        }
        
        // return new EnlistedRanks(builder.build());
        return new EnlistedRanks(ImmutableValueGraph.copyOf(builder), originalOrder);
    }
    
    private static Pair<EnlistedRank, Pair<Optional<String>, Optional<String>>> parseEnlistedRank(
            ConfigurationSection c,
            String id,
            Optional<ChatColor> defaultDecoration,
            Optional<String> defaultDescription) {
        final String name = c.getString("name");
        final String abbr = c.getString("abbreviation");
        
        final Optional<Integer> kills;
        if (c.contains("kills")) {
            kills = Optional.of(c.getInt("kills"));
        } else {
            kills = Optional.empty();
        }
        
        final Optional<ChatColor> color = readColor(c, "color");
        final Optional<ChatColor> decoration = readColor(c, "decoration").or(() -> defaultDecoration);
        final Optional<String> description =
                Optional.ofNullable(c.getString("description")).or(() -> defaultDescription)
                .map(descr -> kills.map(k -> descr.replace("{kills}", k.toString())).orElse(descr));
        
        final Optional<String> predecessor = Optional.ofNullable(c.getString("succeeds"));
        final Map<String, Boolean> permissionChildren = predecessor.isPresent() ? Map.of("wasteland.rank." + predecessor.get(), true) : Map.of();
        
        final Optional<String> preferredSuccessor = Optional.ofNullable(c.getString("preferred"));
        
        final boolean isDefault = kills.isPresent() && kills.get() == 0;
        final PermissionDefault permissionDefault = isDefault ? PermissionDefault.TRUE : PermissionDefault.FALSE;
        
        final Permission permission = new Permission("wasteland.rank." + id, permissionDefault, permissionChildren);
        
        final Permission preferencePermission = new Permission("wasteland.prefer-rank." + id, PermissionDefault.FALSE);
        
        final Pair<Optional<String>, Optional<String>> relatedRanks = new Pair<>(predecessor, preferredSuccessor);
        
        return new Pair<>(new EnlistedRank(id, name, abbr, description, color, decoration, permission, kills, preferencePermission), relatedRanks);
    }

    private static Ranks<Rank> parseOfficerRanks(ConfigurationSection c) {
        Optional<ChatColor> defaultDecoration = readColor(c, "decoration");
        Optional<String> defaultDescription = Optional.ofNullable(c.getString("description"));

        List<Rank> originalOrder = new ArrayList<>();
        Map<String, Rank> ranks = new HashMap<>();
        Map<String, String> predecessors = new HashMap<>();
        
        ConfigurationSection ranksSection = c.getConfigurationSection("ranks");
        for (String id : ranksSection.getKeys(false)) {
            Pair<Rank,Optional<String>> parse =
                    parseOfficerRank(ranksSection.getConfigurationSection(id), id, defaultDecoration, defaultDescription);
            Rank rank = parse.x;
            Optional<String> predecessor = parse.y;
            
            originalOrder.add(rank);
            ranks.put(id, rank);
            predecessor.ifPresent(x -> predecessors.put(id, x));
        }
        // The commented code should be used instead once Guava is upgraded to 28+.
        // ImmutableValueGraph.Builder<Rank, Boolean> builder = ValueGraphBuilder.directed().immutable();
        MutableValueGraph<Rank, Boolean> builder = ValueGraphBuilder.directed().build();
        for (Rank rank : ranks.values()) {
            builder.addNode(rank);
        }
        
        for (Entry<String, String> entry : predecessors.entrySet()) {
            Rank successor = ranks.get(entry.getKey());
            Rank predecessor = ranks.get(entry.getValue());
            builder.putEdgeValue(predecessor, successor, false);
        }
        
        //return new Ranks<>(builder.build());
        return new Ranks<>(ImmutableValueGraph.copyOf(builder), originalOrder);
    }
    
    private static Pair<Rank, Optional<String>> parseOfficerRank(
            ConfigurationSection c,
            String id,
            Optional<ChatColor> defaultDecoration,
            Optional<String> defaultDescription) {
        final String name = c.getString("name");
        final String abbr = c.getString("abbreviation");
            
        final Optional<ChatColor> color = readColor(c, "color");
        final Optional<ChatColor> decoration = readColor(c, "decoration").or(() -> defaultDecoration);
        final Optional<String> description =
                    Optional.ofNullable(c.getString("description")).or(() -> defaultDescription);
            
        final Optional<String> predecessor = Optional.ofNullable(c.getString("succeeds"));
        final Map<String, Boolean> permissionChildren = predecessor.isPresent() ? Map.of(predecessor.get(), true) : Map.of();
            
        final Permission permission = new Permission("wasteland.rank." + id, PermissionDefault.FALSE, permissionChildren);
            
        return new Pair<>(new Rank(id, name, abbr, description, color, decoration, permission), predecessor);
    }
    
    private static SpawnsConfig parseSpawnsConfig(ConfigurationSection c) {
        final HashMap<MonsterType, MonsterSpawnConfig> spawns = new HashMap<>();
        if (c != null) {
            for (String typeName : c.getKeys(false)) {
                MonsterType type = MonsterType.valueOf(typeName);
                MonsterSpawnConfig config = parseMonsterSpawnConfig(c.getConfigurationSection(typeName));
                spawns.put(type, config);
            }
        }
        
        return new SpawnsConfig(spawns);
    }
    
    private static MonsterSpawnConfig parseMonsterSpawnConfig(ConfigurationSection c) {
        final double maximumLightLevel;
        final double blocklightWeight;
        final double sunlightWeight;
        final double moonlightWeight;
        
        if (!c.isConfigurationSection("light")) {
            maximumLightLevel = c.getInt("light", 9);
            blocklightWeight = 1.0f;
            sunlightWeight = 1.0f;
            moonlightWeight = 0.0f;
        } else {
            final ConfigurationSection cLight = c.getConfigurationSection("light");
            maximumLightLevel = cLight.getInt("maximum", 9);
            blocklightWeight = (float) cLight.getDouble("weights.block", 1.0);
            sunlightWeight = (float) cLight.getDouble("weights.sun", 1.0);
            moonlightWeight = (float) cLight.getDouble("weights.moon", 0.0);
        }
        
        final int minimumYLevel = c.getInt("height.minimum", 0);
        final int maximumYLevel = c.getInt("height.maximum", Integer.MAX_VALUE);
        
        final ConfigurationSection cPhases = c.getConfigurationSection("phases");
        final HashMap<MoonPhase, Double> phaseMultipliers = new HashMap<>();
        if (cPhases != null) {
            for (String phaseKey : cPhases.getKeys(false)) {
                MoonPhase phase = MoonPhase.valueOf(phaseKey);
                double multiplier = cPhases.getDouble(phaseKey);
                phaseMultipliers.put(phase, multiplier);
            }
        }
        
        return new MonsterSpawnConfig(
                maximumLightLevel,
                blocklightWeight, sunlightWeight, moonlightWeight,
                minimumYLevel, maximumYLevel,
                phaseMultipliers);
    }
    
    private static KitConfig parseKitConfig(ConfigurationSection c) {
        long kitPeriod = c.getLong("period");
        
        List<Material> kitTools = new ArrayList<>();
        for (String tool : c.getStringList("tools")) {
            kitTools.add(Material.valueOf(tool));
        }
        
        ConfigurationSection itemsc = c.getConfigurationSection("items");
        Map<Material, Integer> kitItems = new HashMap<>();
        for (String item : itemsc.getKeys(false)) {
            kitItems.put(Material.valueOf(item), itemsc.getInt(item));
        }
        
        return new KitConfig(kitPeriod, kitTools, kitItems);
    }
    
    /** Orphaned method. */
    private static Optional<ChatColor> readColor(ConfigurationSection c, String path) {
        return (Optional<ChatColor>) Optional.ofNullable(c.getString(path)).map(ChatColor::valueOf);
    }
}
