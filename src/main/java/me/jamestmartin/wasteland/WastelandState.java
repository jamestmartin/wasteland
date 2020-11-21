package me.jamestmartin.wasteland;

import org.bukkit.plugin.java.JavaPlugin;

import me.jamestmartin.wasteland.chat.ChatState;
import me.jamestmartin.wasteland.kills.KillsState;
import me.jamestmartin.wasteland.ranks.PermissionsPlayerRankProvider;
import me.jamestmartin.wasteland.ranks.PlayerRankProvider;
import me.jamestmartin.wasteland.ranks.RanksState;
import me.jamestmartin.wasteland.spawns.SpawnsState;
import me.jamestmartin.wasteland.store.SqliteDatabase;

public class WastelandState implements Substate {
    private final CommandWasteland commandWasteland;
    
    private final ChatState chatState;
    private final KillsState killsState;
    private final RanksState ranksState;
    private final SpawnsState spawnsState;

    public WastelandState(WastelandConfig config, SqliteDatabase database) {
        this.commandWasteland = new CommandWasteland();
        
        PlayerRankProvider rankProvider = new PermissionsPlayerRankProvider(config.getRanks());
        this.ranksState = new RanksState(config.getKillsConfig(), database, rankProvider);
        this.chatState = new ChatState(config.getChatConfig(), rankProvider);
        this.killsState = new KillsState(config.getKillsConfig(), ranksState.getPlayerKillsStore(), rankProvider);
        this.spawnsState = new SpawnsState(config.getSpawnsConfig());
    }
    
    private Substate[] getSubstates() {
        Substate[] substates = { chatState, killsState, ranksState, spawnsState };
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
}
