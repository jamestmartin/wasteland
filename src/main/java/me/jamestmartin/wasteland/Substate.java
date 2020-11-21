package me.jamestmartin.wasteland;

import org.bukkit.plugin.java.JavaPlugin;

public interface Substate {
    void register(JavaPlugin plugin);
    void unregister(JavaPlugin plugin);
}
