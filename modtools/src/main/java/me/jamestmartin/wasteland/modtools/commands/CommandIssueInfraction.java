package me.jamestmartin.wasteland.modtools.commands;

import java.util.Optional;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jamestmartin.wasteland.modtools.config.DurationsConfig;
import me.jamestmartin.wasteland.modtools.infraction.Duration;
import me.jamestmartin.wasteland.modtools.infraction.InfractionStore;
import me.jamestmartin.wasteland.modtools.infraction.NewInfraction;
import me.jamestmartin.wasteland.modtools.infraction.SentenceType;

abstract class CommandIssueInfraction implements CommandExecutor {
    protected final InfractionStore store;
    protected final DurationsConfig durations;
    
    public CommandIssueInfraction(InfractionStore store, DurationsConfig durations) {
        this.store = store;
        this.durations = durations;
    }
    
    public CommandIssueInfraction(InfractionStore store) {
        if (getType().hasDuration()) {
            throw new IllegalArgumentException("Must pass durations for infraction type which has duration");
        }
        this.store = store;
        this.durations = null;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Duration maxDuration = durations != null ? durations.getMaximumBanDuration(sender) : Duration.INFINITY;
        if (maxDuration.getSeconds().map(x -> x == 0).orElse(false)) {
            sender.sendMessage("You have permission to issue infractions, but can only issue infractions of duration 0!");
            sender.sendMessage("No infraction will be issued.");
            return true;
        }

        Player issuer = sender instanceof Player ? (Player) sender : null;
        Optional<NewInfraction> maybe = parseArgs(getType(), issuer, args);
        if (maybe.isEmpty()) {
            // TOOD: better error messages
            sender.sendMessage("Invalid syntax.");
            return false;
        }
        NewInfraction infraction = maybe.get();
        
        if (infraction.getDuration().compareTo(maxDuration) == 1) {
            sender.sendMessage("You are not allowed you issue an infraction of duration " + infraction.getDuration() + ".");
            sender.sendMessage("The maximum infraction duration you can issue is " + maxDuration + ".");
            sender.sendMessage("No infraction will be issued.");
            return true;
        }
        
        applyInfraction(sender, infraction);
        store.issueInfraction(infraction);
        return false;
    }
    
    protected abstract SentenceType getType();
    
    protected abstract void applyInfraction(CommandSender sender, NewInfraction infraction);
    
    private static Optional<NewInfraction> parseArgs(SentenceType type, OfflinePlayer issuer, String[] args) {
        // TODO
        return null;
    }
}
