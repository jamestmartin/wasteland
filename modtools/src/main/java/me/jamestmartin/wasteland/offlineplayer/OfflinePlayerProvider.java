package me.jamestmartin.wasteland.offlineplayer;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public interface OfflinePlayerProvider {
    Map<UUID, Date> getNameUUIDs(String playerName);
    Date getFirstSeen(UUID player);
    Date getLastSeen(UUID player);
}
