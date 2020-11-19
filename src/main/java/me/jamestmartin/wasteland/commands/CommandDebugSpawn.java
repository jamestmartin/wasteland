package me.jamestmartin.wasteland.commands;

import java.util.Optional;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.jamestmartin.wasteland.Wasteland;

public class CommandDebugSpawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command.");
            return false;
        }
        Player player = (Player) sender;

        if (args.length > 1) {
            sender.sendMessage("Too many arguments.");
            return false;
        }
        
        final int attempts;
        if (args.length == 0) {
            attempts = 1;
        } else {
            attempts = Integer.parseUnsignedInt(args[0]);
        }
        
        Random rand = new Random();
        
        long time = System.currentTimeMillis();
        
        int successfulSpawns = 0;
        for (int attempt = 0; attempt < attempts; attempt++) {
            Optional<LivingEntity> tryMonster = Wasteland.getInstance().getSpawner().trySpawn(rand, player.getLocation());
            if (tryMonster.isEmpty()) {
                continue;
            }
            
            LivingEntity monster = tryMonster.get();
            Location loc = monster.getLocation();
            sender.sendMessage("Spawned a " + monster.getType() + " at location " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ".");
            successfulSpawns++;
        }

        sender.sendMessage("Spawned " + successfulSpawns + " monsters.");
        
        sender.sendMessage("Took " + (System.currentTimeMillis() - time) + " milliseconds.");
        
        return true;
    }
}
