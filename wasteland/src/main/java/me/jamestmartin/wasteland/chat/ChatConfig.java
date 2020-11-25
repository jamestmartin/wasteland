package me.jamestmartin.wasteland.chat;

import java.util.Optional;

import org.bukkit.entity.Player;

import me.jamestmartin.wasteland.Wasteland;
import me.jamestmartin.wasteland.ranks.PlayerRanks;
import me.jamestmartin.wasteland.ranks.Rank;

public class ChatConfig implements ChatFormatter {
    private final String chatFormat;
    private final String officerChatFormat;
    private final String officialFormat;
    private final String consoleFormat;
    
    private final String rankPrefixFormat;
    private final String townyPrefixFormat;
    
    public ChatConfig(
            final String chatFormat,
            final String officerChatFormat,
            final String officialFormat,
            final String consoleFormat,
            final String rankPrefixFormat,
            final String townyPrefixFormat
            ) {
        this.chatFormat = chatFormat;
        this.officerChatFormat = officerChatFormat;
        this.officialFormat = officialFormat;
        this.consoleFormat = consoleFormat;
        this.rankPrefixFormat = rankPrefixFormat;
        this.townyPrefixFormat = townyPrefixFormat;
    }
    
    public String getChatFormat() {
        return chatFormat;
    }
    
    public String getOfficerChatFormat() {
        return officerChatFormat;
    }
    
    public String getOfficialFormat() {
        return officialFormat;
    }
    
    public String getConsoleFormat() {
        return consoleFormat;
    }
    
    public String getRankPrefixFormat() {
        return rankPrefixFormat;
    }
    
    public String getTownyPrefixFormat() {
        return townyPrefixFormat;
    }
    
    private String formatRankPrefix(Rank rank) {
        return getRankPrefixFormat().replace("{abbr}", rank.formatAbbreviated());
    }
    
    private String formatPlayerRankPrefix(Optional<Rank> rank) {
        if (rank.isEmpty()) {
            return "";
        }
        return formatRankPrefix(rank.get());
    }
    
    private String formatPlayerTownPrefix(Player player) {
        Optional<String> townTag = Wasteland.getTowny().getTownTag(player);
        if (townTag.isEmpty()) {
            return "";
        }
        return getTownyPrefixFormat().replace("{tag}", townTag.get());
    }
    
    private String substituteRankPrefixes(PlayerRanks ranks, String format) {
        // This is an inefficient way to do it, but chat doesn't need optimization for now.
        String enlistedRankPrefix = formatPlayerRankPrefix(ranks.getEnlistedRank().map(x -> x));
        String officerRankPrefix = formatPlayerRankPrefix(ranks.getOfficerRank());
        String highestRankPrefix = formatPlayerRankPrefix(ranks.getHighestRank());
        return format
            .replace("{enlisted}", enlistedRankPrefix)
            .replace("{officer}", officerRankPrefix)
            .replace("{rank}", highestRankPrefix);
    }
    
    private String substitutePlayerTownPrefix(Player player, String format) {
        return format.replace("{towny}", formatPlayerTownPrefix(player));
    }
    
    private String substitutePlayerPrefixes(Player player, PlayerRanks ranks, String format) {
        return substitutePlayerTownPrefix(player, substituteRankPrefixes(ranks, format));
    }
    
    @Override
    public String formatPlayerChat(Player player, PlayerRanks ranks) {
        return substitutePlayerPrefixes(player, ranks, getChatFormat());
    }
    
    @Override
    public String formatOfficerChat(Player player, PlayerRanks ranks) {
        return substitutePlayerPrefixes(player, ranks, getOfficerChatFormat());
    }
    
    @Override
    public String formatOfficial(Player player, PlayerRanks ranks) {
        return substitutePlayerPrefixes(player, ranks, getOfficialFormat());
    }
    
    @Override
    public String formatConsole(Rank consoleRank) {
        return getConsoleFormat()
                .replace("{console}", consoleRank.formatAbbreviated())
                .replace("{console_full}", consoleRank.formatFull());
    }
}
