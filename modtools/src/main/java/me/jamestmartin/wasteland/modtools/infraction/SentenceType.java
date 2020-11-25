package me.jamestmartin.wasteland.modtools.infraction;

public enum SentenceType {
    BAN(true),
    KICK(false),
    MUTE(true),
    WARN(false);
    
    private final boolean hasduration;
    
    private SentenceType(boolean hasDuration) {
        this.hasduration = hasDuration;
    }
    
    public boolean hasDuration() {
        return hasduration;
    }
}
