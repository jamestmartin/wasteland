package me.jamestmartin.wasteland;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * The command used to manage the state of the {@link Wasteland plugin} as a whole.
 */
class CommandWasteland implements CommandExecutor {
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
        
        sender.sendMessage("Reloading...");
        Wasteland.getInstance().reload();
        sender.sendMessage("Done.");
        
        return true;
    }
}
