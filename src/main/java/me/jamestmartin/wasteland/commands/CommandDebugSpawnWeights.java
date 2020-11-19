package me.jamestmartin.wasteland.commands;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jamestmartin.wasteland.Wasteland;
import me.jamestmartin.wasteland.spawns.MonsterType;
import me.jamestmartin.wasteland.spawns.WastelandSpawner;
import me.jamestmartin.wasteland.world.MoonPhase;

public class CommandDebugSpawnWeights implements CommandExecutor {
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
        
        WastelandSpawner spawner = Wasteland.getInstance().getSpawner();
        
        Player player = (Player) sender;
        Block target = player.getTargetBlockExact(25).getRelative(BlockFace.UP);
        
        MoonPhase phase = MoonPhase.fromWorld(target.getWorld());
        int monsters = WastelandSpawner.countNearbyMonsters(target.getLocation());
        int players =  WastelandSpawner.countNearbyPlayers(target.getLocation());
        int moonlight = MoonPhase.getMoonlight(target);
        int sunlight = MoonPhase.getSunlight(target);
        int blocklight = target.getLightFromBlocks();
        HashMap<MonsterType, Float> weights = spawner.calculateSpawnProbabilities(target);
        
        sender.sendMessage("Moon phase: " + phase);
        sender.sendMessage("Moonlight: " + moonlight + ", Sunlight: " + sunlight + ", Blocklight: " + blocklight);
        sender.sendMessage("Monsters: " + monsters + ", Players: " + players);
        sender.sendMessage("Monster weights at " + target.getX() + ", " + target.getY() + ", " + target.getZ() + ":");
        for (Entry<MonsterType, Float> entry : weights.entrySet()) {
            MonsterType type = entry.getKey();
            float weight = entry.getValue();
            float lightProb = spawner.calculateLightLevelProbability(type, target);
            float phaseMult = spawner.getMoonPhaseMultiplier(type, target);
            float entitiesMult = WastelandSpawner.calculateNearbyEntitiesMultiplier(target);
            sender.sendMessage(String.format("* %s: %.2f (light: %.2f, phase: %.2f, entities: %.2f)", type, weight, lightProb, phaseMult, entitiesMult));
        }
        
        return true;
    }
}
