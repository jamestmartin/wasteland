package me.jamestmartin.wasteland.modtools;

import org.bukkit.plugin.java.JavaPlugin;

import me.jamestmartin.wasteland.manual.ManualState;
import me.jamestmartin.wasteland.modtools.commands.CommandBan;
import me.jamestmartin.wasteland.modtools.commands.CommandInfractions;
import me.jamestmartin.wasteland.modtools.commands.CommandKick;
import me.jamestmartin.wasteland.modtools.commands.CommandMute;
import me.jamestmartin.wasteland.modtools.commands.CommandWarn;
import me.jamestmartin.wasteland.modtools.config.ModToolsConfig;
import me.jamestmartin.wasteland.modtools.infraction.InfractionStore;
import me.jamestmartin.wasteland.modtools.log.CommandAuditLog;
import me.jamestmartin.wasteland.offlineplayer.CommandLastSeen;
import me.jamestmartin.wasteland.offlineplayer.CommandUUID;

class ModToolsState {
    private final ManualState manualState;
    
    private final CommandBan commandBan;
    private final CommandInfractions commandInfractions;
    private final CommandKick commandKick;
    private final CommandLastSeen commandLastSeen;
    private final CommandAuditLog commandAuditLog;
    private final CommandMute commandMute;
    private final CommandUUID commandUUID;
    private final CommandWarn commandWarn;
    
    public ModToolsState(ModToolsConfig config) {
        this.manualState = new ManualState(config.getManuals());
        
        // TODO
        InfractionStore store = null;
        
        this.commandBan = new CommandBan(store, config.getDurations().getBansConfig());
        this.commandInfractions = new CommandInfractions();
        this.commandKick = new CommandKick(store);
        this.commandLastSeen = new CommandLastSeen();
        this.commandAuditLog = new CommandAuditLog();
        this.commandMute = new CommandMute(store, config.getDurations().getMutesConfig());
        this.commandUUID = new CommandUUID();
        this.commandWarn = new CommandWarn(store);
    }
    
    public void register(JavaPlugin plugin) {
        manualState.register(plugin);
        
        plugin.getCommand("ban").setExecutor(commandBan);
        plugin.getCommand("infractions").setExecutor(commandInfractions);
        plugin.getCommand("kick").setExecutor(commandKick);
        plugin.getCommand("lastseen").setExecutor(commandLastSeen);
        plugin.getCommand("auditlog").setExecutor(commandAuditLog);
        plugin.getCommand("mute").setExecutor(commandMute);
        plugin.getCommand("uuid").setExecutor(commandUUID);
        plugin.getCommand("warn").setExecutor(commandWarn);
    }
    
    public void unregister(JavaPlugin plugin) {
        manualState.unregister(plugin);
        
        plugin.getCommand("ban").setExecutor(null);
        plugin.getCommand("bans").setExecutor(null);
        plugin.getCommand("infractions").setExecutor(null);
        plugin.getCommand("kick").setExecutor(null);
        plugin.getCommand("lastseen").setExecutor(null);
        plugin.getCommand("auditlog").setExecutor(null);
        plugin.getCommand("mute").setExecutor(null);
        plugin.getCommand("mutes").setExecutor(null);
        plugin.getCommand("unban").setExecutor(null);
        plugin.getCommand("unmute").setExecutor(null);
        plugin.getCommand("uuid").setExecutor(null);
        plugin.getCommand("warn").setExecutor(null);
        
        disable(plugin);
    }
    
    public void disable(JavaPlugin plugin) {
        
    }
}
