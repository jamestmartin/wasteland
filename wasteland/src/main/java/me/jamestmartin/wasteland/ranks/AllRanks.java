package me.jamestmartin.wasteland.ranks;

public class AllRanks {
    private final EnlistedRanks enlistedRanks;
    private final Ranks<Rank> officerRanks;
    private final Rank consoleRank;
    
    public AllRanks(EnlistedRanks enlistedRanks, Ranks<Rank> officerRanks, Rank consoleRank) {
        this.enlistedRanks = enlistedRanks;
        this.officerRanks = officerRanks;
        this.consoleRank = consoleRank;
    }
    
    public EnlistedRanks getEnlistedRanks() {
        return enlistedRanks;
    }
    
    public Ranks<Rank> getOfficerRanks() {
        return officerRanks;
    }
    
    public Rank getConsoleRank() {
        return consoleRank;
    }
}
