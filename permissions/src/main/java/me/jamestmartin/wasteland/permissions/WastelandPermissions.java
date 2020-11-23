package me.jamestmartin.wasteland.permissions;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import me.jamestmartin.wasteland.permissions.api.Permissions;
import me.jamestmartin.wasteland.permissions.config.ConfigParser;

public class WastelandPermissions extends JavaPlugin {
    private static WastelandPermissions instance;
    
    private Permissions permissions;
    private PermissionsAttachments attachments;
    private PermissionsAttachmentsListener listener;
    
    public static WastelandPermissions getInstance() {
        return instance;
    }
    
    public Permissions getPermissions() {
        return permissions;
    }
    
    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        load();
        
        // This command doesn't depend on any config,
        // so we don't need to register/unregister it when we reload.
        getCommand("wp").setExecutor(new CommandWP());
    }
    
    @Override
    public void onDisable() {
        instance = null;
    }
    
    private void load() {
        permissions = ConfigParser.parse(getConfig());
        
        if (permissions.getPlayerGroups().size() == 0) {
            getLogger().warning("No players have been assigned permissions!");
        }
        
        attachments = new PermissionsAttachments(permissions);
        listener = new PermissionsAttachmentsListener(attachments);
        
        attachments.register();
        getServer().getPluginManager().registerEvents(listener, this);
    }
    
    private void unload() {
        HandlerList.unregisterAll(listener);
        attachments.unregister();
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
