package me.jamestmartin.wasteland.manual;

import org.bukkit.plugin.java.JavaPlugin;

import me.jamestmartin.wasteland.manual.config.ManualConfig;

public class ManualState {
    private final CommandManual commandRules;
    private final CommandManual commandFaq;
    
    public ManualState(ManualConfig config) {
        this.commandRules = new CommandManual("rules", config.getRules());
        this.commandFaq = new CommandManual("faq", config.getFaq());
    }

    public void register(JavaPlugin plugin) {
        plugin.getCommand("rules").setExecutor(commandRules);
        plugin.getCommand("faq").setExecutor(commandFaq);
    }

    public void unregister(JavaPlugin plugin) {
        plugin.getCommand("rules").setExecutor(null);
        plugin.getCommand("faq").setExecutor(null);
    }
}
