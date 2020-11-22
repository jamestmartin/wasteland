package me.jamestmartin.wasteland;

import org.bukkit.plugin.java.JavaPlugin;

public interface Substate {
    void register(JavaPlugin plugin);
    void unregister(JavaPlugin plugin);
    
    /**
     * Close everything that needs to be closed without unregistering anything.
     * This is called when the plugin is being disabled.
     */
    default void disable(JavaPlugin plugin) { }
}
