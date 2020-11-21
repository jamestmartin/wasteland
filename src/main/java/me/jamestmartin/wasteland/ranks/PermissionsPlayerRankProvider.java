package me.jamestmartin.wasteland.ranks;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.bukkit.entity.Player;

public class PermissionsPlayerRankProvider implements PlayerRankProvider {
    private final AllRanks ranks;
    
    public PermissionsPlayerRankProvider(AllRanks ranks) {
        this.ranks = ranks;
    }
    
    @Override
    public AllRanks getRanks() {
        return ranks;
    }
    
    private<T extends Rank> Optional<T> getRank(Player player, Ranks<T> ranks) {
        Set<T> playerRanks = new HashSet<>();
        for (T rank : ranks.getRanks()) {
            if (player.hasPermission(rank.getPermission())) {
                playerRanks.add(rank);
            }
        }
        
        return ranks.getHighestRank(playerRanks);
    }

    @Override
    public Optional<EnlistedRank> getEnlistedRank(Player player) {
        return getRank(player, ranks.getEnlistedRanks());
    }

    @Override
    public Optional<Rank> getOfficerRank(Player player) {
        return getRank(player, ranks.getOfficerRanks());
    }
}
