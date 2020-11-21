package me.jamestmartin.wasteland;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

import me.jamestmartin.wasteland.config.ConfigParser;
import me.jamestmartin.wasteland.store.SqliteDatabase;
import me.jamestmartin.wasteland.towny.TownyEnabled;
import me.jamestmartin.wasteland.towny.TownyDisabled;
import me.jamestmartin.wasteland.towny.TownyDependency;

import org.bukkit.plugin.java.JavaPlugin;

public class Wasteland extends JavaPlugin {
	private static Wasteland instance;
	private static TownyDependency towny;
    
    private WastelandConfig config;
    private SqliteDatabase database;
    private WastelandState state;
	
	public static Wasteland getInstance() {
		return instance;
	}
	
	public static TownyDependency getTowny() {
	    return towny;
	}
	
	@Override
	public void onEnable() {
		instance = this;
		
		if (Wasteland.getInstance().getServer().getPluginManager().isPluginEnabled("Towny")) {
            towny = new TownyEnabled();
        } else {
            towny = new TownyDisabled();
        }
		
		initialize();
		register();
	}

	@Override
	public void onDisable() {
	    // No need to unregister stuff; Bukkit will do that for us.
	    state = null;
	    
		deinitialize();
		
		towny = null;
		instance = null;
	}
	
	public void reload() {
	    reloadConfig();
        reinitialize();
        
        unregister();
        register();
	}
    
    private void reinitialize() {
        deinitialize();
        initialize();
    }
    
    private void initialize() {
        initializeConfig();
        initializeDatabase();
    }
    
    private void initializeConfig() {
        saveDefaultConfig();
        config = ConfigParser.parseConfig(getConfig());
    }
    
    private void initializeDatabase() {
        String databaseFilename = config.getDatabaseFilename();
        try {
            database = new SqliteDatabase(new File(this.getDataFolder(), databaseFilename));
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
    
    private void deinitialize() {
        config = null;
        
        try {
            if (database != null)
                database.close();
        } catch (SQLException e) {
            this.getLogger().log(Level.SEVERE, "Failed to close database.", e);
        }
        database = null;
    }
	
	private void register() {
        state = new WastelandState(config, database);
        state.register(this);
	}
	
	private void unregister() {
	    state.unregister(this);
	    state = null;
	}
}
