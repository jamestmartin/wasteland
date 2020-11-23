package me.jamestmartin.wasteland.ranks;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.graph.ImmutableValueGraph;

/** A collection of ranks and their successor/predecessor relationships. */
public class Ranks<T extends Rank> {
    protected final ImmutableValueGraph<T, Boolean> ranks;
    protected final Optional<List<T>> originalOrder;
    
    /**
     * @param ranks
     *   The edges represent predecessor/successor relationships,
     *   and the edge value represents whether the successor is the preferred successor.
     *   This graph *must* be a tree.
     * @param originalOrder
     *   The order in which the ranks are listed in the configuration file.
     */
    public Ranks(ImmutableValueGraph<T, Boolean> ranks, Optional<List<T>> originalOrder) {
        this.ranks = ranks;
        this.originalOrder = originalOrder;
    }
    
    /**
     * @param ranks
     *   The edges represent predecessor/successor relationships,
     *   and the edge value represents whether the successor is the preferred successor.
     *   This graph *must* be a tree.
     * @param originalOrder
     *   The order in which the ranks are listed in the configuration file.
     */
    public Ranks(ImmutableValueGraph<T, Boolean> ranks, List<T> originalOrder) {
        this(ranks, Optional.of(originalOrder));
    }
    
    /**
     * @param ranks
     *   The edges represent predecessor/successor relationships,
     *   and the edge value represents whether the successor is the preferred successor.
     *   This graph *must* be a tree.
     */
    public Ranks(ImmutableValueGraph<T, Boolean> ranks) {
        this(ranks, Optional.empty());
    }
    
    /** Get all of the ranks in this collection. */
    public Set<T> getRanks() {
        return ranks.nodes();
    }
    
    /** Get a list of the ranks in their original order, or, if that is not specified, ordered least to greatest. */
    public List<T> getRanksList() {
        return originalOrder.orElseGet(() -> Lists.reverse(getRanks().stream().sorted(getComparator()).collect(Collectors.toUnmodifiableList())));
    }
    
    /** Get a rank by its id. */
    public Optional<T> getRank(String id) {
        return getRanks().stream().filter(rank -> rank.getId().equals(id)).findAny();
    }
    
    /** The rank's unique predecessor, if it exists. */
    public Optional<T> getPredecessor(T rank) {
        return ranks.predecessors(rank).stream().findAny();
    }
    
    public boolean hasPredecessor(T rank) {
        return getPredecessor(rank).isPresent();
    }
    
    /** Every rank which is a transitive predecessor to the given rank. */
    public Set<T> getTransitivePredecessors(T rank) {
        Set<T> predecessors = new HashSet<>();
        while (hasPredecessor(rank)) {
            rank = getPredecessor(rank).get();
            predecessors.add(rank);
        }
        return predecessors;
    }
    
    /** Every rank which is a transitive predecessor to the given rank, including the rank itself. */
    public Set<T> getTransitiveReflexivePredecessors(T rank) {
        Set<T> predecessors = getTransitivePredecessors(rank);
        predecessors.add(rank);
        return predecessors;
    }
    
    /** The set of *direct* successors to the rank. */
    public Set<T> getSuccessors(T rank) {
        return ranks.successors(rank);
    }
    
    /** The rank's preferred successor, if it exists. */
    public Optional<T> getPreferredSuccessor(T rank) {
        return getSuccessors(rank).stream().filter(successor -> ranks.edgeValueOrDefault(rank, successor, false)).findAny();
    }
    
    /** Is the rank the preferred successor to its predecessor, if it has one? */
    public boolean isPreferredSuccessor(final T rank) {
        // Future Guava versions return an Optional here in the first place.
        return getPredecessor(rank).flatMap(predecessor -> Optional.ofNullable(ranks.edgeValue(predecessor, rank))).orElse(false);
    }
    
    /** Is the second rank a *direct* successor of the first rank? */
    public boolean isSuccessorOf(T predecessor, T successor) {
        // Replace with this code for Guava 25+.
        //return ranks.hasEdgeConnecting(predecessor, successor);
        return ranks.successors(predecessor).contains(successor);
    }
    
    /** Is the second rank a transitive successor of the first rank? */
    public boolean isTransitiveSuccessorOf(T predecessor, T successor) {
        return isSuccessorOf(predecessor, successor)
                || getPredecessor(successor).map(rank -> isTransitiveSuccessorOf(predecessor, rank)).orElse(false);
    }
    
    /**
     * The highest ranks of a collection of ranks is any rank which has no transitive successor.
     * If there are multiple such ranks in this collection, the first is returned.
     */
    public Optional<T> getHighestRank(Collection<T> ranks) {
        return ranks.stream().filter(rank -> !ranks.stream().anyMatch(other -> isTransitiveSuccessorOf(rank, other))).findFirst();
    }
    
    /** Compare ranks by their predecessor/successor relationship. */
    public class RankComparator implements Comparator<T> {
        @Override
        public int compare(T a, T b) {
            if (a == b) return 0;
            if (a.getId().equals(b.getId())) return 0;
            if (Ranks.this.isSuccessorOf(a, b)) return 1;
            if (Ranks.this.isSuccessorOf(b, a)) return -1;
            
            // Preferred ranks are given precedence over other ranks.
            Optional<T> predA = Ranks.this.getPredecessor(a);
            Optional<T> predB = Ranks.this.getPredecessor(b);
            if (predA.isPresent() && predB.isPresent() && predA.get().getId().equals(predB.get().getId())) {
                if (Ranks.this.isPreferredSuccessor(a)) {
                    return 1;
                }
                if (Ranks.this.isPreferredSuccessor(b)) {
                    return -1;
                }
            }
            
            // incomparable
            return 0;
        }
    }
    
    public RankComparator getComparator() {
        return new RankComparator();
    }
}
