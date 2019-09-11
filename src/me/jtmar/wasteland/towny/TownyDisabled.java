package me.jtmar.wasteland.towny;

import java.util.Optional;

import org.bukkit.entity.Player;

public class TownyDisabled implements TownyPrefix {
	@Override
	public Optional<String> getPrefix(Player player) {
		return Optional.empty();
	}
}
