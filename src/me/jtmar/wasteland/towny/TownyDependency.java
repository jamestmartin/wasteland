package me.jtmar.wasteland.towny;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;

public class TownyDependency implements TownyPrefix {
	private final String townTagColor;
	
	public TownyDependency(Optional<ChatColor> townTagColor) {
		this.townTagColor = townTagColor.map(ChatColor::toString).orElse("");
	}
	
	public TownyDependency() {
		this(Optional.empty());
	}
	
	public TownyDependency(ChatColor townTagColor) {
		this(Optional.of(townTagColor));
	}
	
	@Override
	public Optional<String> getPrefix(Player player) {
		try {
			Resident resident = TownyUniverse.getDataSource().getResident(player.getName());
			String tag = resident.getTown().getTag();
			if (tag != null && !tag.equals("")) {
				return Optional.of(ChatColor.RESET + "[" + townTagColor + tag + ChatColor.RESET + "]");
			}
		} catch (NotRegisteredException e) {
		}
		return Optional.empty();
	}
}
