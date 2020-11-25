package me.jamestmartin.wasteland.towny;

import java.util.Optional;

import org.bukkit.entity.Player;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Government;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;

/**
 * The one class in the entire plugin that directly depends on Towny.
 * If Towny is installed, it will be dynamically loaded;
 * if not, a dummy implementation will be loaded instead.
 * 
 * @see TownyDependency
 * @see TownyDisabled
 */
public class TownyEnabled implements TownyDependency {
    private static Resident getResident(Player player) throws NotRegisteredException {
        return TownyAPI.getInstance().getDataSource().getResident(player.getName());
    }
    
    private static Optional<Town> getTown(Player player) {
        try {
            return Optional.of(getResident(player).getTown());
        } catch (NotRegisteredException e) {
            return Optional.empty();
        }
    }
    
    private static Optional<Nation> getNation(Player player) {
        try {
            return Optional.of(getResident(player).getTown().getNation());
        } catch (NotRegisteredException e) {
            return Optional.empty();
        }
    }
    
    private static Optional<String> getTag(Government gov) {
        String tag = gov.getTag();
        if (tag == null || tag.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(tag);
    }
    
	@Override
	public Optional<String> getTownTag(Player player) {
	    return getTown(player).flatMap(TownyEnabled::getTag);
	}

    @Override
    public Optional<String> getNationTag(Player player) {
        return getNation(player).flatMap(TownyEnabled::getTag);
    }
}
