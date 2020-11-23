package me.jamestmartin.wasteland.manual;

import org.bukkit.plugin.java.JavaPlugin;

import me.jamestmartin.wasteland.Substate;

public class ManualState implements Substate {
    private final CommandManual commandRules;
    private final CommandManual commandFaq;
    
    public ManualState(ManualConfig config) {
        this.commandRules = new CommandManual("rules", config.getRules());
        this.commandFaq = new CommandManual("faq", config.getFaq());
    }

    @Override
    public void register(JavaPlugin plugin) {
        plugin.getCommand("rules").setExecutor(commandRules);
        plugin.getCommand("faq").setExecutor(commandFaq);
    }

    @Override
    public void unregister(JavaPlugin plugin) {
        plugin.getCommand("rules").setExecutor(null);
        plugin.getCommand("faq").setExecutor(null);
    }
}
