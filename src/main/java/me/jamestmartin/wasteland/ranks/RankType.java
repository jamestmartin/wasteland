package me.jamestmartin.wasteland.ranks;

public enum RankType {
    ENLISTED,
    OFFICER,
    CONSOLE,
    /** Either enlisted, or officer if the player is one. */
    HIGHEST;
}
