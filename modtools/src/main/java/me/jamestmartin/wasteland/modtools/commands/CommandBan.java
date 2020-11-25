package me.jamestmartin.wasteland.modtools.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jamestmartin.wasteland.modtools.config.DurationsConfig;
import me.jamestmartin.wasteland.modtools.infraction.InfractionStore;
import me.jamestmartin.wasteland.modtools.infraction.NewInfraction;
import me.jamestmartin.wasteland.modtools.infraction.SentenceType;

public class CommandBan extends CommandIssueInfraction {
    public CommandBan(InfractionStore store, DurationsConfig durations) {
        super(store, durations);
    }

    @Override
    protected SentenceType getType() {
        return SentenceType.BAN;
    }

    @Override
    protected void applyInfraction(CommandSender sender, NewInfraction infraction) {
        Player recipient = infraction.getRecipient().getPlayer();
        if (recipient != null) {
            recipient.kickPlayer(infraction.getMessage());
        }
        sender.sendMessage(infraction.getRecipient().getName() + " has been banned for " + infraction.getDuration().toStringLong() + ".");
    }
}
