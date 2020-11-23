package me.jamestmartin.wasteland.kit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.Optional;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import me.jamestmartin.wasteland.Wasteland;

public class CommandKit implements CommandExecutor {
    private final KitConfig config;
    private final KitStore store;
    private final Random rand = new Random();
    
    public CommandKit(KitConfig config, KitStore store) {
        this.config = config;
        this.store = store;
    }
    
    private Optional<ItemStack> makeTool(Material material, double quality) {
        int durability = material.getMaxDurability() - (int) (material.getMaxDurability() * rand.nextDouble() / quality);
        if (durability <= material.getMaxDurability() / 15) {
            return Optional.empty();
        }
        
        ItemStack tool = new ItemStack(material);
        ItemMeta meta = tool.getItemMeta();
        ((Damageable) meta).setDamage(durability);
        tool.setItemMeta(meta);
        
        return Optional.of(tool);
    }
    
    private Collection<ItemStack> selectKitItems(Player player) throws Exception {
        int totalKits = store.getTotalKitsRecieved(player);
        double quality = Math.pow(2, ((double) -totalKits) / 2 + 1);
        
        List<ItemStack> items = new ArrayList<>();
        
        for (Material tool : config.getKitTools()) {
            makeTool(tool, quality).ifPresent(items::add);
        }
        
        for (Entry<Material, Integer> item : config.getKitItems().entrySet()) {
            int quantity = (int) (item.getValue() * rand.nextDouble() * quality);
            System.out.println(item.getKey().toString());
            System.out.println(quantity);
            if (quantity > item.getValue() / 15) {
                items.add(new ItemStack(item.getKey(), quantity));
            }
        }
        
        return items;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            sender.sendMessage("Too many arguments.");
            return false;
        }
        
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to receive a kit.");
            return false;
        }
        
        Player player = (Player) sender;
        
        
        try {
            if (store.getLastKitTime(player) + config.getKitPeriod() > player.getWorld().getFullTime()) {
                sender.sendMessage("You've already received a kit recently.");
                return true;
            }
        } catch (Exception e) {
            Wasteland.getInstance().getLogger().log(Level.SEVERE, "Failed to get player's last kit time.", e);
            sender.sendMessage("ERROR: Failed to get last kit time. Please notify a server administrator.");
            return true;
        }
        
        Collection<ItemStack> kitItems;
        try {
            kitItems = selectKitItems(player);
        } catch (Exception e) {
            Wasteland.getInstance().getLogger().log(Level.SEVERE, "Failed to get player's total kits received.", e);
            sender.sendMessage("ERROR: Failed to get total kits received. Please notify a server administrator.");
            return true;
        }
        
        if (kitItems.isEmpty()) {
            sender.sendMessage("Looks like supplies have dried up.");
            return true;
        }
            
        try {
            store.useKit(player);
        } catch (Exception e) {
            Wasteland.getInstance().getLogger().log(Level.SEVERE, "Failed to update player's kit usage.", e);
            sender.sendMessage("ERROR: Failed to update kit usage. Please notify a server administrator.");
            return true;
        }
            
        for (ItemStack stack : kitItems) {
            player.getWorld().dropItemNaturally(player.getLocation(), stack);
        }

        return true;
    }
}
