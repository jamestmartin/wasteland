package me.jamestmartin.wasteland.store;

import me.jamestmartin.wasteland.Substate;
import me.jamestmartin.wasteland.kills.KillsStore;
import me.jamestmartin.wasteland.kit.KitStore;

public interface StoreState extends Substate {
    KillsStore getKillsStore();
    KitStore getKitStore();
}
