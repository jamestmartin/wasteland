package me.jamestmartin.wasteland.spawns;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jamestmartin.wasteland.world.MoonPhase;

public class CommandDebugSpawnWeights implements CommandExecutor {
    private final WastelandSpawner spawner;
    
    public CommandDebugSpawnWeights(WastelandSpawner spawner) {
        this.spawner = spawner;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            sender.sendMessage("Too many arguments.");
            return false;
        }
        
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command.");
            return false;
        }
        
        Player player = (Player) sender;
        Block target = player.getTargetBlockExact(25).getRelative(BlockFace.UP);
        
        MoonPhase phase = MoonPhase.fromWorld(target.getWorld());
        int monsters = WastelandSpawner.countNearbyMonsters(target.getLocation());
        int players =  WastelandSpawner.countNearbyPlayers(target.getLocation());
        int moonlight = MoonPhase.getMoonlight(target);
        int sunlight = MoonPhase.getSunlight(target);
        int blocklight = target.getLightFromBlocks();
        HashMap<MonsterType, Double> weights = spawner.calculateSpawnProbabilities(target);
        
        sender.sendMessage("Moon phase: " + phase);
        sender.sendMessage("Moonlight: " + moonlight + ", Sunlight: " + sunlight + ", Blocklight: " + blocklight);
        sender.sendMessage("Monsters: " + monsters + ", Players: " + players);
        sender.sendMessage("Monster weights at " + target.getX() + ", " + target.getY() + ", " + target.getZ() + ":");
        for (Entry<MonsterType, Double> entry : weights.entrySet()) {
            MonsterType type = entry.getKey();
            double weight = entry.getValue();
            double lightProb = spawner.calculateLightLevelProbability(type, target);
            double phaseMult = spawner.getMoonPhaseMultiplier(type, target);
            double entitiesMult = WastelandSpawner.calculateNearbyEntitiesMultiplier(target);
            sender.sendMessage(String.format("* %s: %.2f (light: %.2f, phase: %.2f, entities: %.2f)", type, weight, lightProb, phaseMult, entitiesMult));
        }
        
        return true;
    }
}
