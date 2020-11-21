package me.jamestmartin.wasteland.towny;

import java.util.Optional;

import org.bukkit.entity.Player;

public class TownyDisabled implements TownyDependency {
	@Override
	public Optional<String> getTownAbbreviation(Player player) {
		return Optional.empty();
	}
}
