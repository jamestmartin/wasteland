package me.jamestmartin.wasteland;

import java.io.IOException;
import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import me.jamestmartin.wasteland.chat.ChatState;
import me.jamestmartin.wasteland.kills.KillsState;
import me.jamestmartin.wasteland.kit.KitState;
import me.jamestmartin.wasteland.ranks.PermissionsPlayerRankProvider;
import me.jamestmartin.wasteland.ranks.PlayerRankProvider;
import me.jamestmartin.wasteland.ranks.RanksState;
import me.jamestmartin.wasteland.spawns.SpawnsState;
import me.jamestmartin.wasteland.store.StoreState;
import me.jamestmartin.wasteland.store.sqlite.SqliteState;

public class WastelandState implements Substate {
    private final CommandWasteland commandWasteland;
    
    private final StoreState storeState;
    private final ChatState chatState;
    private final KillsState killsState;
    private final RanksState ranksState;
    private final SpawnsState spawnsState;
    private final KitState kitState;

    public WastelandState(WastelandConfig config) throws IOException, ClassNotFoundException, SQLException {
        this.commandWasteland = new CommandWasteland();
        
        PlayerRankProvider rankProvider = new PermissionsPlayerRankProvider(config.getRanks());
        this.storeState = new SqliteState(config.getDatabaseFilename());
        this.ranksState = new RanksState(config.getKillsConfig(), storeState.getKillsStore(), rankProvider);
        this.chatState = new ChatState(config.getChatConfig(), rankProvider);
        this.killsState = new KillsState(config.getKillsConfig(), ranksState.getPlayerKillsStore(), rankProvider);
        this.spawnsState = new SpawnsState(config.getSpawnsConfig());
        this.kitState = new KitState(config.getKitConfig(), storeState.getKitStore());
    }
    
    private Substate[] getSubstates() {
        Substate[] substates = {
                storeState,
                chatState,
                killsState,
                ranksState,
                spawnsState, 
                kitState,
        };
        return substates;
    }

    @Override
    public void register(JavaPlugin plugin) {
        plugin.getCommand("wasteland").setExecutor(commandWasteland);
        
        for (Substate substate : getSubstates()) {
            substate.register(plugin);
        }
    }

    @Override
    public void unregister(JavaPlugin plugin) {
        plugin.getCommand("wasteland").setExecutor(null);
        
        for (Substate substate : getSubstates()) {
            substate.unregister(plugin);
        }
    }
    
    @Override
    public void disable(JavaPlugin plugin) {
        for (Substate substate : getSubstates()) {
            substate.disable(plugin);
        }
    }
}
