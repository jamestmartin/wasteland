package me.jamestmartin.wasteland.modtools.infraction;

/** The way in which which an infraction was removed. */
public enum RepealType {
    /** The player was cleared of wrongdoing, and this infraction will be removed from their record. */
    CLEARED,
    /** The player's sentence was reduced, and replaced with another infraction. */
    COMMUTED,
    /** The player's sentence was removed based on appeal, but the infraction will remain on their record. */
    APPEALED;
}
