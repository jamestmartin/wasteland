package me.jamestmartin.wasteland.modtools.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jamestmartin.wasteland.modtools.infraction.Infraction;
import me.jamestmartin.wasteland.modtools.infraction.InfractionStore;
import me.jamestmartin.wasteland.modtools.infraction.InfractionType;

public class CommandWarn extends CommandIssueInfraction {
    public CommandWarn(InfractionStore store) {
        super(store);
    }

    @Override
    protected InfractionType getType() {
        return InfractionType.WARN;
    }

    @Override
    protected void applyInfraction(CommandSender sender, Infraction infraction) {
        Player player = infraction.getRecipient().getPlayer();
        if (player != null) {
            player.sendMessage(infraction.getMessage());
        }
        
        sender.sendMessage(infraction.getRecipient().getName() + " has been warned.");
    }
}
