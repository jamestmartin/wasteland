package me.jamestmartin.wasteland.chat;

import org.bukkit.entity.Player;

import me.jamestmartin.wasteland.ranks.PlayerRanks;
import me.jamestmartin.wasteland.ranks.Rank;

/** Creates chat format strings for players. */
interface ChatFormatter {
    /**
     * Calculates the chat format string for a player.
     * It will contain two `%s` variables: the player's name, and the actual message.
     */
    String formatPlayerChat(Player player, PlayerRanks ranks);
    
    /**
     * Calculates the officer format string for a player.
     * It will contain two `%s` variables: the player's name, and the actual message.
     */
    String formatOfficerChat(Player player, PlayerRanks ranks);
    
    /**
     * Calculates the `/official` format string for a player.
     * It will contain two `%s` variables: the player's name, and the actual message.
     */
    String formatOfficial(Player player, PlayerRanks ranks);
    
    /**
     * Calculates the `/official` format string for the console.
     * It will contain just one `%s` variable: the actual message.
     */
    String formatConsole(Rank consoleRank);
}
