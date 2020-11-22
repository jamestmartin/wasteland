package me.jamestmartin.wasteland;

import me.jamestmartin.wasteland.config.ConfigParser;
import me.jamestmartin.wasteland.towny.TownyEnabled;
import me.jamestmartin.wasteland.towny.TownyDisabled;
import me.jamestmartin.wasteland.towny.TownyDependency;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

public class Wasteland extends JavaPlugin {
	private static Wasteland instance;
	private static TownyDependency towny;
    
    private WastelandConfig config;
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
		
		if (this.getServer().getPluginManager().isPluginEnabled("Towny")) {
            towny = new TownyEnabled();
        } else {
            towny = new TownyDisabled();
        }
		
		initializeConfig();
		register();
	}

	@Override
	public void onDisable() {
	    state.disable(this);
	    state = null;
		towny = null;
		instance = null;
	}
	
	public void reload() {
	    getLogger().info("Reloading wasteland...");
	    reloadConfig();
	    initializeConfig();
	    
        unregister();
        register();
        getLogger().info("Done.");
	}
    
    private void initializeConfig() {
        saveDefaultConfig();
        config = ConfigParser.parseConfig(getConfig());
    }
	
	private void register() {
	    try {
	        state = new WastelandState(config);
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
        state.register(this);
	}
	
	private void unregister() {
	    state.unregister(this);
	    state = null;
	}
}
