package me.jamestmartin.wasteland.modtools.infraction;

import java.util.Date;

import org.bukkit.OfflinePlayer;

public class Infraction {
    private final InfractionType type;
    private final OfflinePlayer issuer;
    private final OfflinePlayer recipient;
    private final Date issued;
    private final Duration duration;
    private final String rule;
    private final String reason;
    
    public Infraction(InfractionType type, OfflinePlayer issuer, OfflinePlayer recipient, Date issued, Duration duration, String rule, String reason) {
        this.type = type;
        this.issuer = issuer;
        this.recipient = recipient;
        this.issued = issued;
        this.duration = duration;
        this.rule = rule;
        this.reason = reason;
    }
    
    public InfractionType getType() {
        return type;
    }
    
    public OfflinePlayer getIssuer() {
        return issuer;
    }
    
    public OfflinePlayer getRecipient() {
        return recipient;
    }
    
    public Date getIssued() {
        return issued;
    }
    
    public Duration getDuration() {
        return duration;
    }
    
    public String getRule() {
        return rule;
    }
    
    public String getReason() {
        return reason;
    }
    
    public String getMessage() {
        // TODO
        return null;
    }
}
