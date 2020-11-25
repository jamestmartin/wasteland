package me.jamestmartin.wasteland.modtools.log;

import me.jamestmartin.wasteland.modtools.infraction.Infraction;
import me.jamestmartin.wasteland.modtools.infraction.InfractionStore;

public interface AuditLogStore extends AuditLogProvider, InfractionStore {
    void addInfraction(Infraction infraction);
}
