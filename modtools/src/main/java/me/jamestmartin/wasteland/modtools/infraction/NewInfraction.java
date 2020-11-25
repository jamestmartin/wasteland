package me.jamestmartin.wasteland.modtools.infraction;

import java.util.Date;
import java.util.Optional;

import org.bukkit.OfflinePlayer;

public class NewInfraction {
    /** The type of sentence the player received: ban, kick, mute, or warning. */
    private final SentenceType type;
    /** The moderator who issued the sentence. (Or empty, if it was issued by the console.) */
    private final Optional<OfflinePlayer> issuer;
    /** The player who committed the infraction. */
    private final OfflinePlayer recipient;
    /** The time that the sentence takes effect, or empty if it starts immediately. */
    private final Optional<Date> start;
    /** The duration of the sentence. */
    private final Duration duration;
    /** If this sentence was commuted, the identifier for the original infraction. */
    private final Optional<Integer> original;
    /** The section identifier for the rule that was violated. */
    private final String rule;
    /** The reason that an infraction was issued. */
    private final String reason;
    
    public NewInfraction(
            SentenceType type,
            Optional<OfflinePlayer> issuer,
            OfflinePlayer recipient,
            Optional<Date> start,
            Duration duration,
            Optional<Integer> original,
            String rule,
            String reason
    ) {
        this.type = type;
        this.issuer = issuer;
        this.recipient = recipient;
        this.start = start;
        this.duration = duration;
        this.original = original;
        this.rule = rule;
        this.reason = reason;
    }
    
    public NewInfraction(
            SentenceType type,
            Optional<OfflinePlayer> issuer,
            OfflinePlayer recipient,
            Duration duration,
            String rule,
            String reason
    ) {
        this.type = type;
        this.issuer = issuer;
        this.recipient = recipient;
        this.start = Optional.empty();
        this.duration = duration;
        this.original = Optional.empty();
        this.rule = rule;
        this.reason = reason;
    }
    
    /** Update an old infraction with a new expiry. */
    public NewInfraction(
            Optional<OfflinePlayer> issuer,
            Duration duration,
            Infraction original
    ) {
        this.type = original.getType();
        this.issuer = issuer;
        this.recipient = original.getRecipient();
        this.start = Optional.of(original.getStart());
        this.duration = duration;
        this.original = Optional.of(original.getId());
        this.rule = original.getRule();
        this.reason = original.getReason();
    }
    
    /** Update an old infraction with a new expiry and reason. */
    public NewInfraction(
            Optional<OfflinePlayer> issuer,
            Duration duration,
            Infraction original,
            String rule,
            String reason
    ) {
        this.type = original.getType();
        this.issuer = issuer;
        this.recipient = original.getRecipient();
        this.start = Optional.of(original.getStart());
        this.duration = duration;
        this.original = Optional.of(original.getId());
        this.rule = rule;
        this.reason = reason;
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
    
    /**
     * @return The time that the sentence first takes effect, or empty if it starts immediately.
     */
    public Optional<Date> getStart() {
        return start;
    }
    
    /** @return The time the infraction naturally expires if not repealed, if ever. */
    public Duration getDuration() {
        return duration;
    }
    
    /** @return If this sentence was commuted, the identifier for the original infraction. */
    public Optional<Integer> getOriginal() {
        return original;
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
