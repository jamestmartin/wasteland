package me.jamestmartin.wasteland.kit;

import org.bukkit.plugin.java.JavaPlugin;

import me.jamestmartin.wasteland.Substate;

public class KitState implements Substate {
    private final CommandKit commandKit;
    
    public KitState(KitConfig config, KitStore store) {
        this.commandKit = new CommandKit(config, store);
    }

    @Override
    public void register(JavaPlugin plugin) {
        plugin.getCommand("kit").setExecutor(commandKit);
    }

    @Override
    public void unregister(JavaPlugin plugin) {
        plugin.getCommand("kit").setExecutor(null);
    }
}
