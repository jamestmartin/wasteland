package me.jamestmartin.wasteland.towny;

import java.util.Optional;

import org.bukkit.entity.Player;

public interface TownyPrefix {
	public Optional<String> getPrefix(Player player);
}
