package me.jamestmartin.wasteland.modtools.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jamestmartin.wasteland.modtools.infraction.Infraction;
import me.jamestmartin.wasteland.modtools.infraction.InfractionStore;
import me.jamestmartin.wasteland.modtools.infraction.InfractionType;

public class CommandKick extends CommandIssueInfraction {
    public CommandKick(InfractionStore store) {
        super(store);
    }

    @Override
    protected InfractionType getType() {
        return InfractionType.KICK;
    }

    @Override
    protected void applyInfraction(CommandSender sender, Infraction infraction) {
        Player player = infraction.getRecipient().getPlayer();
        if (player == null) {
            sender.sendMessage("That player is not online!");
            sender.sendMessage("Infraction logged anyway.");
            return;
        }
        
        player.kickPlayer(infraction.getMessage());
    }
}
