package me.jamestmartin.wasteland.modtools.infraction;

import java.util.Optional;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface InfractionStore extends InfractionProvider {
    /**
     * @param infraction The new infraction to add to the database.
     * @see NewInfraction#NewInfraction(SentenceType, java.util.Optional, OfflinePlayer, Duration, String, String) Create a new infraction.
     * @see NewInfraction#NewInfraction(java.util.Optional, Duration, Infraction) Commute an old infraction.
     * @see NewInfraction#NewInfraction(SentenceType, java.util.Optional, OfflinePlayer, java.util.Optional, Duration, java.util.Optional, String, String) Update an old infraction.
     */
    void issueInfraction(NewInfraction infraction);
    
    /**
     * <p>Clear a previously-issued infraction.
     * <p>This will clear the infraction from the player's record, but not from the audit log.
     * @param id The identifier of the infraction to clear.
     * @param issuer The moderator who cleared the infraction.
     * @see RepealType#CLEARED
     * @see #appealInfraction(OfflinePlayer, SentenceType)
     */
    void clearInfraction(int id, Optional<Player> issuer);
    
    /**
     * <p>Accept an appeal for a previously-issued infraction.
     * <p>This will remove the sentence from the player, but not remove it from their record.
     * @param id The identifier of the infraction which was appealed.
     * @param issuer The moderator who accepted the appeal.
     * @see RepealType#APPEALED
     * @see #clearInfraction(int, Optional)
     */
    void appealInfraction(int id, Optional<Player> issuer);
}
