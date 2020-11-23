package me.jamestmartin.wasteland.modtools.infraction;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface InfractionProvider {
    Set<Infraction> getActiveInfractions(Optional<UUID> player, Optional<InfractionType> type);
    
    default Set<Infraction> getActiveInfractions() {
        return getActiveInfractions(Optional.empty(), Optional.empty());
    }
    
    default Set<Infraction> getActiveInfractions(UUID player) {
        return getActiveInfractions(Optional.of(player), Optional.empty());
    }
    
    default Set<Infraction> getActiveInfractions(UUID player, InfractionType type) {
        return getActiveInfractions(Optional.of(player), Optional.of(type));
    }
    
    default Set<Infraction> getActiveInfractions(InfractionType type) {
        return getActiveInfractions(Optional.empty(), Optional.of(type));
    }
}
