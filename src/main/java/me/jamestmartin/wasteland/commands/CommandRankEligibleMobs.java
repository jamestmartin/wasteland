package me.jamestmartin.wasteland.commands;

import java.util.Collection;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

public class CommandRankEligibleMobs implements CommandExecutor {
    private final String eligibleMobsName;
    private final Collection<EntityType> eligibleMobs;
    
    public CommandRankEligibleMobs(String eligibleMobsName, Collection<EntityType> eligibleMobs) {
        this.eligibleMobsName = eligibleMobsName;
        this.eligibleMobs = eligibleMobs;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            sender.sendMessage("Too many arguments!");
            sender.sendMessage(command.getUsage());
            return false;
        }
        
        sender.sendMessage("Killing " + eligibleMobsName + " will count towards your next promotion.");
        sender.sendMessage("Specifically, any of these mobs will work: " +
                eligibleMobs.stream().map(Object::toString).sorted().collect(Collectors.joining(", ")));
        
        return true;
    }
}
