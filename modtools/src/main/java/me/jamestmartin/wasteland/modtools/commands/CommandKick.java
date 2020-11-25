package me.jamestmartin.wasteland.modtools.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jamestmartin.wasteland.modtools.infraction.NewInfraction;
import me.jamestmartin.wasteland.modtools.infraction.InfractionStore;
import me.jamestmartin.wasteland.modtools.infraction.SentenceType;

public class CommandKick extends CommandIssueInfraction {
    public CommandKick(InfractionStore store) {
        super(store);
    }

    @Override
    protected SentenceType getType() {
        return SentenceType.KICK;
    }

    @Override
    protected void applyInfraction(CommandSender sender, NewInfraction infraction) {
        Player player = infraction.getRecipient().getPlayer();
        if (player == null) {
            sender.sendMessage("That player is not online!");
            sender.sendMessage("Infraction logged anyway.");
            return;
        }
        
        player.kickPlayer(infraction.getMessage());
    }
}
