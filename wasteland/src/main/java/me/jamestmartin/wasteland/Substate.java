package me.jamestmartin.wasteland;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * <p>
 * This plugin implements a lot of functionality,
 * and although it is all needed together to create the desired player experience,
 * most parts of the plugin do not depend on most other parts.
 * For example, the spawning subsystem does not depend on the chat subsystem.
 * However, splitting the plugin into many tiny independent plugins would be excessive.
 * 
 * <p>
 * Thus, instead, the plugin is split into many submodules which each manages its own
 * state, configuration, et cetera independently.
 * This interface is a common interface to those submodules.
 * 
 * @see WastelandState The plugin's main state class which contains all of the other substates.
 */
public interface Substate {
    /**
     * Register all:
     * <ul>
     *   <li>commands
     *   <li>listeners
     *   <li>permission attachments
     * </ul>
     * and connect to any databases.
     * 
     * @param plugin
     *   Bukkit requires a {@link JavaPlugin} instances
     *   to register commands and listeners *for*.
     */
    void register(JavaPlugin plugin);
    
    /**
     * Disable and unregister all:
     * <ul>
     *   <li>commands
     *   <li>listeners
     *   <li>permission attachments
     * </ul>
     * and disconnect from any databases.
     * 
     * @param plugin
     *   Bukkit requires a {@link JavaPlugin} instances
     *   to unregister commands and listeners *from*.
     * 
     * @see #disable(JavaPlugin)
     */
    void unregister(JavaPlugin plugin);
    
    /**
     * <p>
     * Close everything that needs to be closed without
     * {@link #unregister(JavaPlugin) unregistering} anything.
     * 
     * <p>
     * When a plugin is disabled, Bukkit automatically unregisters most things, like commands, listeners, and attachments,
     * and in fact if we tried to unregister themselves, exceptions would be thrown.
     * However, it may still be necessary to do things like clean up database connections,
     * which is the purpose of this method.
     * 
     * @param plugin The plugin being disabled.
     * 
     * @see #unregister(JavaPlugin)
     */
    default void disable(JavaPlugin plugin) { }
}
