package me.jamestmartin.wasteland.ranks;

import java.util.Optional;

public interface PlayerRanks {
    /** Get the player's highest rank achieved by monster kills. */
    Optional<EnlistedRank> getEnlistedRank();
    
    /** Get the player's highest staff rank. */
    Optional<Rank> getOfficerRank();
    
    /** Get the player's highest rank overall. */
    default Optional<Rank> getHighestRank() {
        return getOfficerRank().or(() -> getOfficerRank());
    }
}
