package me.jamestmartin.wasteland.modtools;

import org.bukkit.plugin.java.JavaPlugin;

import me.jamestmartin.wasteland.manual.ManualState;
import me.jamestmartin.wasteland.modtools.commands.CommandBan;
import me.jamestmartin.wasteland.modtools.commands.CommandBans;
import me.jamestmartin.wasteland.modtools.commands.CommandInfractions;
import me.jamestmartin.wasteland.modtools.commands.CommandKick;
import me.jamestmartin.wasteland.modtools.commands.CommandMute;
import me.jamestmartin.wasteland.modtools.commands.CommandMutes;
import me.jamestmartin.wasteland.modtools.commands.CommandUnban;
import me.jamestmartin.wasteland.modtools.commands.CommandUnmute;
import me.jamestmartin.wasteland.modtools.commands.CommandWarn;
import me.jamestmartin.wasteland.modtools.config.ModToolsConfig;
import me.jamestmartin.wasteland.modtools.infraction.InfractionStore;
import me.jamestmartin.wasteland.modtools.log.CommandModLog;
import me.jamestmartin.wasteland.offlineplayer.CommandLastSeen;
import me.jamestmartin.wasteland.offlineplayer.CommandUUID;

class ModToolsState {
    private final ManualState manualState;
    
    private final CommandBan commandBan;
    private final CommandBans commandBans;
    private final CommandInfractions commandInfractions;
    private final CommandKick commandKick;
    private final CommandLastSeen commandLastSeen;
    private final CommandModLog commandModLog;
    private final CommandMute commandMute;
    private final CommandMutes commandMutes;
    private final CommandUnban commandUnban;
    private final CommandUnmute commandUnmute;
    private final CommandUUID commandUUID;
    private final CommandWarn commandWarn;
    
    public ModToolsState(ModToolsConfig config) {
        this.manualState = new ManualState(config.getManuals());
        
        // TODO
        InfractionStore store = null;
        
        this.commandBan = new CommandBan(store, config.getDurations().getBansConfig());
        this.commandBans = new CommandBans();
        this.commandInfractions = new CommandInfractions();
        this.commandKick = new CommandKick(store);
        this.commandLastSeen = new CommandLastSeen();
        this.commandModLog = new CommandModLog();
        this.commandMute = new CommandMute(store, config.getDurations().getMutesConfig());
        this.commandMutes = new CommandMutes();
        this.commandUnban = new CommandUnban();
        this.commandUnmute = new CommandUnmute();
        this.commandUUID = new CommandUUID();
        this.commandWarn = new CommandWarn(store);
    }
    
    public void register(JavaPlugin plugin) {
        manualState.register(plugin);
        
        plugin.getCommand("ban").setExecutor(commandBan);
        plugin.getCommand("bans").setExecutor(commandBans);
        plugin.getCommand("infractions").setExecutor(commandInfractions);
        plugin.getCommand("kick").setExecutor(commandKick);
        plugin.getCommand("lastseen").setExecutor(commandLastSeen);
        plugin.getCommand("modlog").setExecutor(commandModLog);
        plugin.getCommand("mute").setExecutor(commandMute);
        plugin.getCommand("mutes").setExecutor(commandMutes);
        plugin.getCommand("unban").setExecutor(commandUnban);
        plugin.getCommand("unmute").setExecutor(commandUnmute);
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
        plugin.getCommand("modlog").setExecutor(null);
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
