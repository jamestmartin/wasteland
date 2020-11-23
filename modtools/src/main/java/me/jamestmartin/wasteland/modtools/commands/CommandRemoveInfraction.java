package me.jamestmartin.wasteland.modtools.commands;

import java.util.Optional;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jamestmartin.wasteland.modtools.infraction.Infraction;
import me.jamestmartin.wasteland.modtools.infraction.InfractionStore;
import me.jamestmartin.wasteland.modtools.infraction.InfractionType;

public abstract class CommandRemoveInfraction implements CommandExecutor {
    protected final InfractionStore store;
    
    public CommandRemoveInfraction(InfractionStore store) {
        this.store = store;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player issuer = sender instanceof Player ? (Player) sender : null;
        Optional<Infraction> maybe = parseArgs(getType(), issuer, args);
        if (maybe.isEmpty()) {
            // TOOD: better error messages
            sender.sendMessage("Invalid syntax.");
            return false;
        }
        Infraction infraction = maybe.get();
        
        applyInfraction(sender, infraction);
        store.addInfraction(infraction);
        return false;
    }
    
    protected abstract InfractionType getType();
    
    protected abstract void applyInfraction(CommandSender sender, Infraction infraction);
    
    private static Optional<Infraction> parseArgs(InfractionType type, OfflinePlayer issuer, String[] args) {
        // TODO
        return null;
    }
}
