package me.jamestmartin.wasteland.kills;

import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.jamestmartin.wasteland.Substate;
import me.jamestmartin.wasteland.ranks.PlayerRankProvider;

public class KillsState implements Substate {
    private final KillsListener killsListener;
    private final CommandRankEligibleMobs commandRankEligibleMobs;
    private final CommandSetKills commandSetKills;
    
    public KillsState(KillsConfig config, PlayerKillsStore store, PlayerRankProvider rankProvider) {
        this.killsListener = new KillsListener(config, store, rankProvider);
        this.commandRankEligibleMobs = new CommandRankEligibleMobs(config);
        this.commandSetKills = new CommandSetKills(store);
    }
    
    @Override
    public void register(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(killsListener, plugin);
        plugin.getCommand("setkills").setExecutor(commandSetKills);
        plugin.getCommand("rankeligiblemobs").setExecutor(commandRankEligibleMobs);
    }
    
    @Override
    public void unregister(JavaPlugin plugin) {
        EntityDeathEvent.getHandlerList().unregister(killsListener);
        plugin.getCommand("setkills").setExecutor(null);
    }
}
