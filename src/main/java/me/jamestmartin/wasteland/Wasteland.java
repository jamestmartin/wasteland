package me.jamestmartin.wasteland;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

import me.jamestmartin.wasteland.commands.CommandDebugSpawn;
import me.jamestmartin.wasteland.commands.CommandDebugSpawnWeights;
import me.jamestmartin.wasteland.commands.CommandOfficial;
import me.jamestmartin.wasteland.commands.CommandRank;
import me.jamestmartin.wasteland.commands.CommandRankEligibleMobs;
import me.jamestmartin.wasteland.commands.CommandRanks;
import me.jamestmartin.wasteland.commands.CommandSetKills;
import me.jamestmartin.wasteland.config.WastelandConfig;
import me.jamestmartin.wasteland.listeners.ChatListener;
import me.jamestmartin.wasteland.listeners.RankListener;
import me.jamestmartin.wasteland.spawns.WastelandSpawner;
import me.jamestmartin.wasteland.towny.TownyDependency;
import me.jamestmartin.wasteland.towny.TownyDisabled;
import me.jamestmartin.wasteland.towny.TownAbbreviationProvider;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Wasteland extends JavaPlugin {
	private static Wasteland instance;
	
	private Database database;
	private WastelandConfig config;
	private RankListener rankListener;
	private TownAbbreviationProvider townyAbbreviationProvider;
	private WastelandSpawner spawner;
	
	public static Wasteland getInstance() {
		return instance;
	}

    public Database getDatabase() {
        return database;
    }
	
	public WastelandConfig getSettings() {
		return config;
	}
	
	public TownAbbreviationProvider getTownyAbbreviationProvider() {
		return townyAbbreviationProvider;
	}
	
	public WastelandSpawner getSpawner() {
	    return spawner;
	}
	
	public void updatePlayerRank(Player player) throws SQLException {
		rankListener.updatePlayerRank(player);
	}
	
	private void initializeConfig() {
		this.saveDefaultConfig();
		this.config = new WastelandConfig(this.getConfig());
	}
	
	private void initializeTowny() {
		if (Wasteland.getInstance().getServer().getPluginManager().isPluginEnabled("Towny")) {
			this.townyAbbreviationProvider = new TownyDependency();
		} else {
			this.townyAbbreviationProvider = new TownyDisabled();
		}
	}
	
	private void initializeDatabase() {
		String databaseFilename = this.getConfig().getString("databaseFile");
		try {
			database = new Database(new File(this.getDataFolder(), databaseFilename));
		} catch (IOException e) {
			this.getLogger().log(Level.SEVERE, "Failed to write database file: " + e.getMessage());
			this.getPluginLoader().disablePlugin(this);
		} catch (ClassNotFoundException e) {
			this.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
			this.getPluginLoader().disablePlugin(this);
		} catch (SQLException e) {
			this.getLogger().log(Level.SEVERE, "SQLite exception on initialize.", e);
			this.getPluginLoader().disablePlugin(this);
		}
	}
	
	private void registerCommands() {
		this.getCommand("rank").setExecutor(new CommandRank(config.eligibleMobsName()));
		this.getCommand("rankeligiblemobs").setExecutor(new CommandRankEligibleMobs(config.eligibleMobsName(), config.eligibleMobs()));
        this.getCommand("ranks").setExecutor(new CommandRanks());
		this.getCommand("setkills").setExecutor(new CommandSetKills());
		this.getCommand("official").setExecutor(new CommandOfficial(config.chat()));

		// debug commands
		this.getCommand("debugspawn").setExecutor(new CommandDebugSpawn());
		this.getCommand("debugspawnweights").setExecutor(new CommandDebugSpawnWeights());
	}
	
	private void registerListeners() {
		PluginManager manager = this.getServer().getPluginManager();
		manager.registerEvents(new RankListener(config.eligibleMobs()), this);
		manager.registerEvents(new ChatListener(config.chat()), this);
	}
	
	@Override
	public void onEnable() {
		instance = this;
		initializeConfig();
		initializeTowny();
		initializeDatabase();
		registerCommands();
		registerListeners();
		this.spawner = new WastelandSpawner(config.spawns());
	}

	@Override
	public void onDisable() {
		if (rankListener != null)
			rankListener.close();
		rankListener = null;
		try {
			if (database != null)
				database.close();
		} catch (SQLException e) {
			this.getLogger().log(Level.SEVERE, "Failed to close database.", e);
		}
		instance = null;
	}
}
