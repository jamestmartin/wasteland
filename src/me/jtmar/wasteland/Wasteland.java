package me.jtmar.wasteland;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.jtmar.wasteland.commands.CommandRank;
import me.jtmar.wasteland.commands.CommandRanks;
import me.jtmar.wasteland.commands.CommandOfficial;
import me.jtmar.wasteland.commands.CommandSetKills;
import me.jtmar.wasteland.listeners.ChatListener;
import me.jtmar.wasteland.listeners.RankListener;

public class Wasteland extends JavaPlugin {
	private static Wasteland instance;
	
	private Database database;
	private RankListener rankListener;
	
	public static Wasteland getInstance() {
		return instance;
	}
	
	public Database getDatabase() {
		return database;
	}
	
	public void updatePlayerRank(Player player) throws SQLException {
		rankListener.updatePlayerRank(player);
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
		this.getCommand("rank").setExecutor(new CommandRank());
		this.getCommand("setkills").setExecutor(new CommandSetKills());
		this.getCommand("official").setExecutor(new CommandOfficial());
		this.getCommand("ranks").setExecutor(new CommandRanks());
	}
	
	private void registerListeners() {
		PluginManager manager = this.getServer().getPluginManager();
		rankListener = new RankListener();
		manager.registerEvents(rankListener, this);
		manager.registerEvents(new ChatListener(), this);
	}
	
	@Override
	public void onEnable() {
		instance = this;
		this.saveDefaultConfig();
		initializeDatabase();
		registerCommands();
		registerListeners();
	}
	
	@Override
	public void onDisable() {
		rankListener.close();
		rankListener = null;
		try {
			database.close();
		} catch (SQLException e) {
			this.getLogger().log(Level.SEVERE, "Failed to close database.", e);
		}
		instance = null;
	}
}
