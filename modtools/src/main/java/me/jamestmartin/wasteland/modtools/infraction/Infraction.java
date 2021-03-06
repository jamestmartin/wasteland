package me.jamestmartin.wasteland.modtools.infraction;

import java.util.Date;
import java.util.Optional;

import org.bukkit.OfflinePlayer;

public class Infraction {
    /** The unique identifier of this infraction. */
    private final int id;
    /** The type of sentence the player received: ban, kick, mute, or warning. */
    private final SentenceType type;
    /** The moderator who issued the sentence. (Or empty, if it was issued by the console.) */
    private final Optional<OfflinePlayer> issuer;
    /** The player who committed the infraction. */
    private final OfflinePlayer recipient;
    /** The time that the sentence was issued. */
    private final Date issued;
    /** The time that the sentence takes effect. */
    private final Date start;
    /** The time that the sentence naturally expires, if ever. */
    private final Optional<Date> expiry;
    /** The information about this infraction's repeal, if it was repealed. */
    private final Optional<Repeal> repeal;
    /** If this sentence was commuted, the identifier for the original infraction. */
    private final Optional<Integer> original;
    /** The section identifier for the rule that was violated. */
    private final String rule;
    /** The reason that an infraction was issued. */
    private final String reason;
    
    public Infraction(
            int id,
            SentenceType type,
            Optional<OfflinePlayer> issuer,
            OfflinePlayer recipient,
            Date issued,
            Date start,
            Optional<Date> expiry,
            Optional<Repeal> repeal,
            Optional<Integer> original,
            String rule,
            String reason
    ) {
        this.id = id;
        this.type = type;
        this.issuer = issuer;
        this.recipient = recipient;
        this.issued = issued;
        this.start = start;
        this.expiry = expiry;
        this.repeal = repeal;
        this.original = original;
        this.rule = rule;
        this.reason = reason;
    }
    
    /** @return The unique identifier for this infraction. */
    public int getId() {
        return id;
    }
    
    /** @return The {@link SentenceType type of sentence} the player received: ban, kick, mute, or warning. */
    public SentenceType getType() {
        return type;
    }
    
    /** @return The moderator who issued the sentence. (Or empty, if it was issued by the console.) */
    public Optional<OfflinePlayer> getIssuer() {
        return issuer;
    }

    /** @return The player who committed the infraction. */
    public OfflinePlayer getRecipient() {
        return recipient;
    }
    
    /** @return The time the sentence was issued. */
    public Date getIssued() {
        return issued;
    }
    
    /**
     * @return
     *   <p>The time that the sentence first takes effect.
     *   <p>
     *      Generally, this will be the same time that it was issued;
     *      however, if a sentence is commuted, the new infraction starts at the same time,
     *      but will actually have a time issued *after* the sentence started!
     */
    public Date getStart() {
        return start;
    }
    
    /** @return The time the infraction naturally expires if not repealed, if ever. */
    public Optional<Date> getExpiry() {
        return expiry;
    }
    
    /**
     * @return The duration that the sentence would last if not repealed.
     * @see #getActualDuration()
     */
    public Duration getOriginalDuration() {
        return Duration.fromDates(getStart(), getExpiry());
    }
    
    /** @return The information about this infraction's repeal, if it was repealed. */
    public Optional<Repeal> getRepeal() {
        return repeal;
    }
    
    /** @return If this sentence was commuted, the identifier for the original infraction. */
    public Optional<Integer> getOriginal() {
        return original;
    }
    
    /**
     * @return The duration the sentence would last if it has been repealed, or otherwise the original duration.
     * @see #getOriginalDuration()
     */
    public Duration getActualDuration() {
        return Duration.fromDates(getStart(), getRepeal().map(Repeal::getIssued).or(() -> getExpiry()));
    }
    
    /** @return The rule that was violated. */
    public String getRule() {
        return rule;
    }
    
    /** @return The reason that the infraction was issued. */
    public String getReason() {
        return reason;
    }
    
    /** @return The message that the player will get sent with information about their sentence. */
    public String getMessage() {
        // TODO
        return null;
    }
}
