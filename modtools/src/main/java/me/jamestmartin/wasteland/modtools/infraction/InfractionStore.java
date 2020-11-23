package me.jamestmartin.wasteland.modtools.infraction;

import org.bukkit.OfflinePlayer;

public interface InfractionStore extends InfractionProvider {
    void addInfraction(Infraction infraction);
    void removeInfractions(OfflinePlayer player, InfractionType type);
}
