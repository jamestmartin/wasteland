package me.jamestmartin.wasteland.towny;

import java.util.Optional;

import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;

public class TownyDependency implements TownAbbreviationProvider {
	@Override
	public Optional<String> getTownAbbreviation(Player player) {
		try {
			Resident resident = TownyAPI.getInstance().getDataSource().getResident(player.getName());
			String tag = resident.getTown().getTag();
			if (tag != null && !tag.equals("")) {
				return Optional.of(tag);
			}
		} catch (NotRegisteredException e) {
		}
		return Optional.empty();
	}
}
