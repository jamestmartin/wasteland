package me.jamestmartin.wasteland.modtools.infraction;

import java.util.Date;
import java.util.Optional;

import org.bukkit.OfflinePlayer;

public class Repeal {
    private final RepealType type;
    private final Optional<OfflinePlayer> issuer;
    private final Date issued;
    
    public Repeal(RepealType type, Optional<OfflinePlayer> issuer, Date issued) {
        this.type = type;
        this.issuer = issuer;
        this.issued = issued;
    }
    
    /** The way in which the infraction was repealed. */
    public RepealType getType() {
        return type;
    }
    
    /** The player who issued the repeal. */
    public Optional<OfflinePlayer> getIssuer() {
        return issuer;
    }
    
    /** The time the repeal was issued. */
    public Date getIssued() {
        return issued;
    }
}
