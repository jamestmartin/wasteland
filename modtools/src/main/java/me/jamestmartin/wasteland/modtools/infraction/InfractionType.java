package me.jamestmartin.wasteland.modtools.infraction;

public enum InfractionType {
    BAN(true),
    KICK(false),
    MUTE(true),
    WARN(false);
    
    private final boolean hasduration;
    
    private InfractionType(boolean hasDuration) {
        this.hasduration = hasDuration;
    }
    
    public boolean hasDuration() {
        return hasduration;
    }
}
