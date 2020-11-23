package me.jamestmartin.wasteland.modtools.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jamestmartin.wasteland.modtools.config.DurationsConfig;
import me.jamestmartin.wasteland.modtools.infraction.Infraction;
import me.jamestmartin.wasteland.modtools.infraction.InfractionStore;
import me.jamestmartin.wasteland.modtools.infraction.InfractionType;

public class CommandBan extends CommandIssueInfraction {
    public CommandBan(InfractionStore store, DurationsConfig durations) {
        super(store, durations);
    }

    @Override
    protected InfractionType getType() {
        return InfractionType.BAN;
    }

    @Override
    protected void applyInfraction(CommandSender sender, Infraction infraction) {
        Player recipient = infraction.getRecipient().getPlayer();
        if (recipient != null) {
            recipient.kickPlayer(infraction.getMessage());
        }
        sender.sendMessage(infraction.getRecipient().getName() + " has been banned for " + infraction.getDuration().toStringLong() + ".");
    }
}
