package me.jamestmartin.wasteland.towny;

import java.util.Optional;

import org.bukkit.entity.Player;

public interface TownAbbreviationProvider {
	public Optional<String> getTownAbbreviation(Player player);
}
