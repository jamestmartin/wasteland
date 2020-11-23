package me.jamestmartin.wasteland.store.sqlite;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.jamestmartin.wasteland.Wasteland;
import me.jamestmartin.wasteland.kills.KillsStore;
import me.jamestmartin.wasteland.kit.KitStore;
import me.jamestmartin.wasteland.store.StoreState;

public class SqliteState implements StoreState {
    private final SqliteDatabase database;
    private final SqliteInitializerListener initializerListener;
    
    public SqliteState(String databaseFilename) throws IOException, ClassNotFoundException, SQLException {
        database = new SqliteDatabase(new File(Wasteland.getInstance().getDataFolder(), databaseFilename));
        initializerListener = new SqliteInitializerListener(database);
    }
    
    @Override
    public void disable(JavaPlugin plugin) {
        try {
            database.close();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "SQLite exception while closing database.", e);
            plugin.getPluginLoader().disablePlugin(plugin);
        }
    }

    @Override
    public void register(JavaPlugin plugin) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            try {
                database.initPlayer(player);
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to initialize player", e);
            }
        }
    }

    @Override
    public void unregister(JavaPlugin plugin) {
        PlayerJoinEvent.getHandlerList().unregister(initializerListener);
        
        disable(plugin);
    }

    @Override
    public KillsStore getKillsStore() {
        return database;
    }
    
    @Override
    public KitStore getKitStore() {
        return database;
    }
}
