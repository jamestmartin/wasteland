package me.jamestmartin.wasteland.spawns;

import org.bukkit.plugin.java.JavaPlugin;

import me.jamestmartin.wasteland.Substate;

public class SpawnsState implements Substate {
    private final CommandDebugSpawn commandDebugSpawn;
    private final CommandDebugSpawnWeights commandDebugSpawnWeights;
    
    public SpawnsState(SpawnsConfig config) {
        WastelandSpawner spawner = new WastelandSpawner(config);
        
        commandDebugSpawn = new CommandDebugSpawn(spawner);
        commandDebugSpawnWeights = new CommandDebugSpawnWeights(spawner);
    }

    @Override
    public void register(JavaPlugin plugin) {
        plugin.getCommand("debugspawn").setExecutor(commandDebugSpawn);
        plugin.getCommand("debugspawnweights").setExecutor(commandDebugSpawnWeights);
    }

    @Override
    public void unregister(JavaPlugin plugin) {
        plugin.getCommand("debugspawn").setExecutor(commandDebugSpawn);
        plugin.getCommand("debugspawnweights").setExecutor(commandDebugSpawnWeights);
    }
}
