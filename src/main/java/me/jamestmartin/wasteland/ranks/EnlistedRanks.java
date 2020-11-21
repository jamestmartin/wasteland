package me.jamestmartin.wasteland.ranks;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import com.google.common.graph.ImmutableValueGraph;

/** A collection of enlisted ranks and their successor/predecessor relationships. */
public class EnlistedRanks extends Ranks<EnlistedRank> {
    /**
     * @param ranks
     *   The edges represent predecessor/successor relationships,
     *   and the edge value represents whether the successor is the preferred successor.
     *   This graph *must* be a tree.
     * @param originalOrder
     *   The order in which the ranks are listed in the configuration file.
     */
    public EnlistedRanks(ImmutableValueGraph<EnlistedRank, Boolean> ranks, Optional<List<EnlistedRank>> originalOrder) {
        super(ranks, originalOrder);
    }
    
    /**
     * @param ranks
     *   The edges represent predecessor/successor relationships,
     *   and the edge value represents whether the successor is the preferred successor.
     *   This graph *must* be a tree.
     * @param originalOrder
     *   The order in which the ranks are listed in the configuration file.
     */
    public EnlistedRanks(ImmutableValueGraph<EnlistedRank, Boolean> ranks, List<EnlistedRank> originalOrder) {
        super(ranks, originalOrder);
    }
    
    /**
     * @param ranks
     *   The edges represent predecessor/successor relationships,
     *   and the edge value represents whether the successor is the preferred successor.
     *   This graph *must* be a tree.
     */
    public EnlistedRanks(ImmutableValueGraph<EnlistedRank, Boolean> ranks) {
        super(ranks);
    }
    
    /** Get the direct successors to the rank which can be automatically rewarded for killing monsters. */
    public Set<EnlistedRank> getPromotableSuccessors(EnlistedRank rank) {
        return getSuccessors(rank).stream().filter(EnlistedRank::hasKills).collect(Collectors.toUnmodifiableSet());
    }
    
    /** Get the preferred direct successor to the rank if it can be automatically rewarded for killing monsters. */
    public Optional<EnlistedRank> getPreferredPromotableSuccessor(EnlistedRank rank) {
        return getPromotableSuccessors(rank).stream().filter(super::isPreferredSuccessor).findAny();
    }
    
    /** Get the next rank that a player would get promoted to after the given rank, if any exists. */
    public Optional<EnlistedRank> getNextRank(Player player, EnlistedRank rank) {
        for (EnlistedRank successor : getPromotableSuccessors(rank)) {
            if (player.hasPermission(successor.getPreferencePermission())) {
                return Optional.of(successor);
            }
        }
        
        return getPreferredPromotableSuccessor(rank);
    }
    
    /** The rank which is automatically assigned to all players. */
    public EnlistedRank getDefaultRank() {
        return getRanks().stream().filter(rank -> rank.getKills().map(x -> x == 0).orElse(false)).findAny().get();
    }
    
    /** The rank a player would have if they killed some number of monsters. */
    public EnlistedRank getRankAt(Player player, int kills) {
        EnlistedRank rank = getDefaultRank();
        while (true) {
            Optional<EnlistedRank> maybe = getNextRank(player, rank);
            if (!maybe.isPresent()) {
                break;
            }
            
            EnlistedRank nextRank = maybe.get();
            
            if (!nextRank.getKills().map(k -> kills >= k).orElseGet(() -> player.hasPermission(nextRank.getPermission()))) {
                break;
            }
            
            rank = nextRank;
        }
        
        return rank;
    }
}
