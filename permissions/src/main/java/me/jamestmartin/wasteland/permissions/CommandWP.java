package me.jamestmartin.wasteland.permissions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

class CommandWP implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 0) {
            sender.sendMessage("Please specify an operation.");
            return false;
        }
        
        if (args.length > 1) {
            sender.sendMessage("Too many arguments.");
            return false;
        }
        
        if (!args[0].equals("reload")) {
            sender.sendMessage("Unknown operation: " + args[0] + ".");
            return false;
        }
        
        if (!sender.hasPermission("wasteland.permissions.reload")) {
            sender.sendMessage("You do not have permission to reload Wasteland Permissions' config.");
        }
        
        WastelandPermissions.getInstance().reload();
        sender.sendMessage("Reloaded Wasteland Permissions' configuration.");
        
        return true;
    }
}
