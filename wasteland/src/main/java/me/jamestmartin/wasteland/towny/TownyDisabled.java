package me.jamestmartin.wasteland.towny;

import java.util.Optional;

import org.bukkit.entity.Player;

/**
 * <p>
 * A set of dummy implementations of Towny-related functionality
 * which we will substitute for the actual functionality if Towny is not installed.
 * <p>
 * These methods do nothing and return nothing.
 * 
 * @see TownyDependency
 * @see TownyEnabled
 */
public class TownyDisabled implements TownyDependency {
	@Override
	public Optional<String> getTownTag(Player player) {
		return Optional.empty();
	}
	
	@Override
	public Optional<String> getNationTag(Player player) {
	    return Optional.empty();
	}
}
