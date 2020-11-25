package me.jamestmartin.wasteland.modtools.log;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import me.jamestmartin.wasteland.modtools.infraction.Duration;
import me.jamestmartin.wasteland.modtools.infraction.Infraction;
import me.jamestmartin.wasteland.modtools.infraction.SentenceType;

public interface AuditLogProvider {
    /**
     * @param duration The maximum time ago that an infraction was issued or repealed.
     * @param issuer The moderator who issued an infraction.
     * @param recipient The player who committed the infraction.
     * @param type The type of sentence issued.
     * @return All infractions that were issued or repealed fitting the provided criteria.
     */
    Set<Infraction> getInfractions(
            Duration duration,
            Optional<UUID> issuer,
            Optional<UUID> recipient,
            Optional<SentenceType> type
    );
    
    /** All infractions that have ever been issued. */
    default Set<Infraction> getInfractions() {
        return getInfractions(Duration.INFINITY);
    }
    
    /**
     * @param type The type of sentence issued.
     * @return All sentences of the given type that have ever been issued.
     */
    default Set<Infraction> getInfractions(SentenceType type) {
        return getInfractions(Duration.INFINITY, type);
    }
    
    /**
     * @param duration The maximum time ago that an infraction was issued or repealed.
     * @return All sentences that have ever been issued or repealed within the duration provided.
     */
    default Set<Infraction> getInfractions(Duration duration) {
        return getInfractions(duration, Optional.empty(), Optional.empty(), Optional.empty());
    }
    
    /**
     * @param duration The maximum time ago that an infraction was issued or repealed.
     * @param type The type of sentence issued.
     * @return All sentences of the given type that have ever been issued or repealed within the duration provided.
     */
    default Set<Infraction> getInfractions(Duration duration, SentenceType type) {
        return getInfractions(duration, Optional.empty(), Optional.empty(), Optional.of(type));
    }
    
    // TODO: Javadoc for all these variants.
    
    default Set<Infraction> getInfractionsIssuedTo(UUID player) {
        return getInfractionsIssuedTo(Duration.INFINITY, player);
    }
    
    default Set<Infraction> getInfractionsIssuedTo(UUID player, SentenceType type) {
        return getInfractionsIssuedTo(Duration.INFINITY, player, type);
    }
    
    default Set<Infraction> getInfractionsIssuedTo(Duration duration, UUID player) {
        return getInfractions(duration, Optional.empty(), Optional.of(player), Optional.empty());
    }
    
    default Set<Infraction> getInfractionsIssuedTo(Duration duration, UUID player, SentenceType type) {
        return getInfractions(duration, Optional.empty(), Optional.of(player), Optional.of(type));
    }
    
    default Set<Infraction> getInfractionsIssuedBy(UUID player) {
        return getInfractionsIssuedBy(Duration.INFINITY, player);
    }
    
    default Set<Infraction> getInfractionsIssuedBy(UUID player, SentenceType type) {
        return getInfractionsIssuedBy(Duration.INFINITY, player, type);
    }
    
    default Set<Infraction> getInfractionsIssuedBy(Duration duration, UUID player) {
        return getInfractions(duration, Optional.of(player), Optional.empty(), Optional.empty());
    }
    
    default Set<Infraction> getInfractionsIssuedBy(Duration duration, UUID player, SentenceType type) {
        return getInfractions(duration, Optional.of(player), Optional.empty(), Optional.of(type));
    }
}
