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
	
    /**
     * The active, enabled instance of this plugin.
     * 
     * @return
     *   May be null if the plugin:
     *   <ul>
     *     <li>has been loaded but not yet enabled,
     *     <li>has been disabled,
     *     <li>is in the process of reloading.
     *   </ul>
     */
	public static Wasteland getInstance() {
		return instance;
	}
	
	/**
	 * @return
	 *   Either the proxy for accessing Towny if the plugin is loaded,
	 *   or a dummy implementation.
	 * @see TownyDependency
	 */
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

        saveDefaultConfig();
		initializeConfig();
		register();
	}

	@Override
	public void onDisable() {
	    // We do not need to disable 
	    state.disable(this);
	    state = null;
		towny = null;
		instance = null;
	}
	
	/**
	 * Completely disable and re-enable the plugin,
	 * which has the effect of reloading the plugin's configuration
	 * and database.
	 */
	public void reload() {
	    getLogger().info("Reloading wasteland...");
        saveDefaultConfig();
	    reloadConfig();
	    initializeConfig();
	    
        unregister();
        register();
        getLogger().info("Done.");
	}
    
    private void initializeConfig() {
        config = ConfigParser.parseConfig(getConfig());
    }
	
    /** Register all commands, listeners, permission attachments, etc. */
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
	
	/** Disable all commands, unregister all listeners and permission attachments, etc. */
	private void unregister() {
	    state.unregister(this);
	    state = null;
	}
}
