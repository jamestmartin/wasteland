package me.jamestmartin.wasteland.modtools.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jamestmartin.wasteland.modtools.infraction.NewInfraction;
import me.jamestmartin.wasteland.modtools.infraction.InfractionStore;
import me.jamestmartin.wasteland.modtools.infraction.SentenceType;

public class CommandWarn extends CommandIssueInfraction {
    public CommandWarn(InfractionStore store) {
        super(store);
    }

    @Override
    protected SentenceType getType() {
        return SentenceType.WARN;
    }

    @Override
    protected void applyInfraction(CommandSender sender, NewInfraction infraction) {
        Player player = infraction.getRecipient().getPlayer();
        if (player != null) {
            player.sendMessage(infraction.getMessage());
        }
        
        sender.sendMessage(infraction.getRecipient().getName() + " has been warned.");
    }
}
