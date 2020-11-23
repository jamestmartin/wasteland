package me.jamestmartin.wasteland.ranks;

import java.util.Optional;

import org.bukkit.entity.Player;

public interface PlayerRankProvider {
    /** Get the ranks this player rank provider can choose from. */
    AllRanks getRanks();
    
    /** Get the player's highest rank achieved by monster kills. */
    Optional<EnlistedRank> getEnlistedRank(Player player);

    /** Get the player's highest staff rank. */
    Optional<Rank> getOfficerRank(Player player);
    
    default PlayerRanks getPlayerRanks(final Player player) {
        return new PlayerRanks() {
            @Override
            public Optional<EnlistedRank> getEnlistedRank() {
                return PlayerRankProvider.this.getEnlistedRank(player);
            }

            @Override
            public Optional<Rank> getOfficerRank() {
                return PlayerRankProvider.this.getOfficerRank(player);
            }
        };
    }
    
    /** Get the player's highest rank overall. */
    default Optional<Rank> getHighestRank(Player player) {
        return getPlayerRanks(player).getHighestRank();
    }
    
    /** The next rank a player will get promoted to from killing monsters. */
    default Optional<EnlistedRank> getNextRank(Player player) {
        return getEnlistedRank(player).flatMap(rank -> getRanks().getEnlistedRanks().getNextRank(player, rank));
    }
}
