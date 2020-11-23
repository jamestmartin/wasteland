package me.jamestmartin.wasteland.modtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.jamestmartin.wasteland.modtools.WastelandModTools;

public class CommandWMT implements CommandExecutor {
    private final WastelandModTools instance;
    
    public CommandWMT(WastelandModTools instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Not enough arguments.");
            return false;
        }
        
        if (args.length > 1) {
            sender.sendMessage("Too many arguments.");
            return false;
        }
        
        if (!args[0].equals("reload")) {
            sender.sendMessage("Unknown subcommand: " + args[0]);
            return false;
        }
        
        if (!sender.hasPermission("wasteland.modtools.reload")) {
            sender.sendMessage("You do not have permission to reload Wasteland ModTools.");
        }
        
        sender.sendMessage("Reloading...");
        instance.reload();
        sender.sendMessage("Done.");
        
        return true;
    }

}
