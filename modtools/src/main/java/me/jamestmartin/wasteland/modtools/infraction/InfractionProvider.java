package me.jamestmartin.wasteland.modtools.infraction;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface InfractionProvider {
    /**
     * @param player The player who committed the infractions, or any player.
     * @param type The type of sentence given, or any sentence type.
     * @return The un-expired, un-repealed sentences matching the provide criteria.
     */
    Set<Infraction> getActiveInfractions(Optional<UUID> player, Optional<SentenceType> type);
    
    /** @return All un-expired, un-repealed sentences. */
    default Set<Infraction> getActiveInfractions() {
        return getActiveInfractions(Optional.empty(), Optional.empty());
    }
    
    /**
     * @param player The player who committed the infractions.
     * @return The player's un-expired, un-repealed sentences.
     */
    default Set<Infraction> getActiveInfractions(UUID player) {
        return getActiveInfractions(Optional.of(player), Optional.empty());
    }
    
    /**
     * @param player The player who committed the infractions.
     * @param type The type of sentence given.
     * @return The player's un-expired, un-repealed sentences of the given type.
     */
    default Set<Infraction> getActiveInfractions(UUID player, SentenceType type) {
        return getActiveInfractions(Optional.of(player), Optional.of(type));
    }
    
    /**
     * @param type The type of sentence given.
     * @return All un-expired, un-repealed sentences of the given type.
     */
    default Set<Infraction> getActiveInfractions(SentenceType type) {
        return getActiveInfractions(Optional.empty(), Optional.of(type));
    }
    
    // TODO: Provide *in*active infractions which were never cleared.
}
