package me.jamestmartin.wasteland.modtools;

import org.bukkit.plugin.java.JavaPlugin;

import me.jamestmartin.wasteland.modtools.commands.CommandWMT;
import me.jamestmartin.wasteland.modtools.config.ConfigParser;

public class WastelandModTools extends JavaPlugin {
    private static WastelandModTools instance;
    
    private ModToolsState state;
    
    public static WastelandModTools getInstance() {
        return instance;
    }
    
    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        load();
        
        // This command doesn't depend on any config,
        // so we don't need to register/unregister it when we reload.
        getCommand("wmt").setExecutor(new CommandWMT(this));
    }
    
    @Override
    public void onDisable() {
        state.disable(this);
        instance = null;
    }
    
    private void load() {
        state = new ModToolsState(ConfigParser.parse(getConfig()));
        state.register(this);
    }
    
    private void unload() {
        state.unregister(this);
        state = null;
    }
    
    public void reload() {
        getLogger().info("Reloading...");
        saveDefaultConfig();
        reloadConfig();
        unload();
        load();
        getLogger().info("Done.");
    }

}
