package me.jamestmartin.wasteland.store.sqlite;

import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import me.jamestmartin.wasteland.Wasteland;

public class SqliteInitializerListener implements Listener {
    private final SqliteDatabase db;
    
    public SqliteInitializerListener(SqliteDatabase db) {
        this.db = db;
    }
    
    // I'd prefer for this to be MONITOR, but it needs to
    // make sure the database is initialized before the e.g. RankAttachments.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        try {
            db.initPlayer(player);
        } catch (SQLException e) {
            Wasteland.getInstance().getLogger().log(Level.SEVERE, "Failed to initialize player in database.", e);
            player.sendMessage("ERROR: Failed to initialize you in the Wasteland database. Please contact a server administrator.");
        }
    }
}
