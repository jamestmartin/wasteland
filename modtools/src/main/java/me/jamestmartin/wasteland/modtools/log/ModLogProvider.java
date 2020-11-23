package me.jamestmartin.wasteland.modtools.log;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import me.jamestmartin.wasteland.modtools.infraction.Duration;
import me.jamestmartin.wasteland.modtools.infraction.Infraction;
import me.jamestmartin.wasteland.modtools.infraction.InfractionType;

public interface ModLogProvider {
    Set<Infraction> getInfractions(
            Duration duration,
            Optional<UUID> issuer,
            Optional<UUID> recipient,
            Optional<InfractionType> type
    );
    
    default Set<Infraction> getInfractions() {
        return getInfractions(Duration.INFINITY);
    }
    
    default Set<Infraction> getInfractions(InfractionType type) {
        return getInfractions(Duration.INFINITY, type);
    }
    
    default Set<Infraction> getInfractions(Duration duration) {
        return getInfractions(duration, Optional.empty(), Optional.empty(), Optional.empty());
    }
    
    default Set<Infraction> getInfractions(Duration duration, InfractionType type) {
        return getInfractions(duration, Optional.empty(), Optional.empty(), Optional.of(type));
    }
    
    default Set<Infraction> getInfractionsIssuedTo(UUID player) {
        return getInfractionsIssuedTo(Duration.INFINITY, player);
    }
    
    default Set<Infraction> getInfractionsIssuedTo(UUID player, InfractionType type) {
        return getInfractionsIssuedTo(Duration.INFINITY, player, type);
    }
    
    default Set<Infraction> getInfractionsIssuedTo(Duration duration, UUID player) {
        return getInfractions(duration, Optional.empty(), Optional.of(player), Optional.empty());
    }
    
    default Set<Infraction> getInfractionsIssuedTo(Duration duration, UUID player, InfractionType type) {
        return getInfractions(duration, Optional.empty(), Optional.of(player), Optional.of(type));
    }
    
    default Set<Infraction> getInfractionsIssuedBy(UUID player) {
        return getInfractionsIssuedBy(Duration.INFINITY, player);
    }
    
    default Set<Infraction> getInfractionsIssuedBy(UUID player, InfractionType type) {
        return getInfractionsIssuedBy(Duration.INFINITY, player, type);
    }
    
    default Set<Infraction> getInfractionsIssuedBy(Duration duration, UUID player) {
        return getInfractions(duration, Optional.of(player), Optional.empty(), Optional.empty());
    }
    
    default Set<Infraction> getInfractionsIssuedBy(Duration duration, UUID player, InfractionType type) {
        return getInfractions(duration, Optional.of(player), Optional.empty(), Optional.of(type));
    }
    
}
