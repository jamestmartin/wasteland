package me.jamestmartin.wasteland.kills;

import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

class CommandRankEligibleMobs implements CommandExecutor {
    private final KillsConfig config;
    
    public CommandRankEligibleMobs(KillsConfig config) {
        this.config = config;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            sender.sendMessage("Too many arguments!");
            sender.sendMessage(command.getUsage());
            return false;
        }
        
        sender.sendMessage("Killing " + config.getEligibleMobsName() + " will count towards your next promotion.");
        sender.sendMessage("Specifically, any of these mobs will work: " +
                config.getEligibleMobs().stream().map(Object::toString).sorted().collect(Collectors.joining(", ")));
        
        return true;
    }
}
