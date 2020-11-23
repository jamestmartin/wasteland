package me.jamestmartin.wasteland;

import me.jamestmartin.wasteland.chat.ChatConfig;
import me.jamestmartin.wasteland.kills.KillsConfig;
import me.jamestmartin.wasteland.kit.KitConfig;
import me.jamestmartin.wasteland.manual.ManualConfig;
import me.jamestmartin.wasteland.permissions.PermissionsConfig;
import me.jamestmartin.wasteland.ranks.AllRanks;
import me.jamestmartin.wasteland.spawns.SpawnsConfig;

public class WastelandConfig {
	private final String databaseFilename;
	private final ChatConfig chatConfig;
	private final KillsConfig killsConfig;
	private final AllRanks ranks;
	private final SpawnsConfig spawnsConfig;
	private final KitConfig kitConfig;
	private final ManualConfig manualConfig;
	private final PermissionsConfig permissionsConfig;
	
	public WastelandConfig(
	        String databaseFilename,
	        ChatConfig chatConfig,
	        KillsConfig killsConfig,
	        AllRanks ranks,
	        SpawnsConfig spawnsConfig,
	        KitConfig kitConfig,
	        ManualConfig manualConfig,
	        PermissionsConfig permissionsConfig) {
	    this.databaseFilename = databaseFilename;
	    this.chatConfig = chatConfig;
	    this.killsConfig = killsConfig;
	    this.ranks = ranks;
	    this.spawnsConfig = spawnsConfig;
	    this.kitConfig = kitConfig;
	    this.manualConfig = manualConfig;
	    this.permissionsConfig = permissionsConfig;
	}
    
    public String getDatabaseFilename() {
        return databaseFilename;
    }
	
	public ChatConfig getChatConfig() {
	    return chatConfig;
	}
	
	public KillsConfig getKillsConfig() {
	    return killsConfig;
	}
	
	public AllRanks getRanks() {
	    return ranks;
	}
	
	public SpawnsConfig getSpawnsConfig() {
	    return spawnsConfig;
	}
	
	public KitConfig getKitConfig() {
	    return kitConfig;
	}
	
	public ManualConfig getManualConfig() {
	    return manualConfig;
	}
	
	public PermissionsConfig getPermissionsConfig() {
	    return permissionsConfig;
	}
}
