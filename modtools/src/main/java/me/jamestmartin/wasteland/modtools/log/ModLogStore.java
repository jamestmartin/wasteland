package me.jamestmartin.wasteland.modtools.log;

import me.jamestmartin.wasteland.modtools.infraction.Infraction;

public interface ModLogStore extends ModLogProvider {
    void addInfraction(Infraction infraction);
}
