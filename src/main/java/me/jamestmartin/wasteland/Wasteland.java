package me.jamestmartin.wasteland;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

import me.jamestmartin.wasteland.commands.CommandOfficial;
import me.jamestmartin.wasteland.commands.CommandRank;
import me.jamestmartin.wasteland.commands.CommandRankEligibleMobs;
import me.jamestmartin.wasteland.commands.CommandRanks;
import me.jamestmartin.wasteland.commands.CommandSetKills;
import me.jamestmartin.wasteland.config.WastelandConfig;
import me.jamestmartin.wasteland.listeners.ChatListener;
import me.jamestmartin.wasteland.listeners.RankListener;
import me.jamestmartin.wasteland.towny.TownyDependency;
import me.jamestmartin.wasteland.towny.TownyDisabled;
import me.jamestmartin.wasteland.towny.TownyPrefix;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Wasteland extends JavaPlugin {
	private static Wasteland instance;
	
	private Database database;
	private WastelandConfig config;
	private RankListener rankListener;
	private TownyPrefix townyPrefix;
	
	public static Wasteland getInstance() {
		return instance;
	}
	
	public WastelandConfig getSettings() {
		return config;
	}
	
	public TownyPrefix getTownyPrefix() {
		return townyPrefix;
	}
	
	public Database getDatabase() {
		return database;
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
			this.townyPrefix = new TownyDependency(config.prefixTownTagColor());
		} else {
			this.townyPrefix = new TownyDisabled();
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
		this.getCommand("official").setExecutor(new CommandOfficial());
	}
	
	private void registerListeners() {
		PluginManager manager = this.getServer().getPluginManager();
		rankListener = new RankListener(config.eligibleMobs());
		manager.registerEvents(rankListener, this);
		manager.registerEvents(new ChatListener(), this);
	}
	
	@Override
	public void onEnable() {
		instance = this;
		initializeConfig();
		initializeTowny();
		initializeDatabase();
		registerCommands();
		registerListeners();
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
