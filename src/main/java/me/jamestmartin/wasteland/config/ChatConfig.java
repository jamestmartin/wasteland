package me.jamestmartin.wasteland.config;

import java.util.Optional;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.jamestmartin.wasteland.Wasteland;
import me.jamestmartin.wasteland.ranks.Rank;
import me.jamestmartin.wasteland.ranks.RankType;

public class ChatConfig {
    private final String chatFormat;
    private final String officerChatFormat;
    private final String officialFormat;
    private final String consoleFormat;
    
    private final String rankPrefixFormat;
    private final String townyPrefixFormat;
    
    public ChatConfig(ConfigurationSection c) {
        ConfigurationSection formats = c.getConfigurationSection("formats");
        this.chatFormat = formats.getString("chat");
        this.officerChatFormat = formats.getString("officer");
        this.officialFormat = formats.getString("official");
        this.consoleFormat = formats.getString("console");
        
        ConfigurationSection prefixes = c.getConfigurationSection("prefixes");
        this.rankPrefixFormat = prefixes.getString("rank");
        this.townyPrefixFormat = prefixes.getString("towny");
    }
    
    public String formatRankPrefix(Player player, Rank rank) {
        return rankPrefixFormat.replace("{abbr}", rank.formatAbbreviated());
    }
    
    public String formatPlayerRankPrefix(Player player, RankType rankType) {
        Optional<Rank> rank = Rank.getRank(rankType, player);
        if (rank.isEmpty()) {
            return "";
        }
        return formatRankPrefix(player, rank.get());
    }
    
    public String formatPlayerTownPrefix(Player player) {
        Optional<String> townTag = Wasteland.getInstance().getTownyAbbreviationProvider().getTownAbbreviation(player);
        if (townTag.isEmpty()) {
            return "";
        }
        return townyPrefixFormat.replace("{tag}", townTag.get());
    }
    
    private String substituteRankPrefixes(String format, Player player) {
        // This is an inefficient way to do it, but chat doesn't need optimization.
        String enlistedRankPrefix = formatPlayerRankPrefix(player, RankType.ENLISTED);
        String officerRankPrefix = formatPlayerRankPrefix(player, RankType.OFFICER);
        String highestRankPrefix = formatPlayerRankPrefix(player, RankType.HIGHEST);
        return format
            .replace("{enlisted}", enlistedRankPrefix)
            .replace("{officer}", officerRankPrefix)
            .replace("{rank}", highestRankPrefix);
    }
    
    private String substitutePlayerTownPrefix(String format, Player player) {
        return format.replace("{towny}", formatPlayerTownPrefix(player));
    }
    
    private String substitutePlayerPrefixes(String format, Player player) {
        return substitutePlayerTownPrefix(substituteRankPrefixes(format, player), player);
    }
    
    public String getPlayerChatFormat(Player player) {
        return substitutePlayerPrefixes(chatFormat, player);
    }
    
    public String getOfficerChatFormat(Player player) {
        return substitutePlayerPrefixes(officerChatFormat, player);
    }
    
    public String getOfficialFormat(Player player) {
        return substitutePlayerPrefixes(officialFormat, player);
    }
    
    public String getConsoleFormat() {
        return consoleFormat
                .replace("{console}", Rank.getConsoleRank().get().formatAbbreviated())
                .replace("{console_full}", Rank.getConsoleRank().get().formatFull());
    }
}
