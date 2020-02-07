package me.jamestmartin.wasteland.ranks;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import me.jamestmartin.wasteland.Wasteland;

public class EnlistedRank extends Rank {
	private final Optional<Integer> kills;
	
	public EnlistedRank(Optional<String> defaultDescription, Optional<ChatColor> defaultDecoration,
			ConfigurationSection c) {
		super(defaultDescription, defaultDecoration, c);
		if (c.getKeys(false).contains("kills")) this.kills = Optional.of(c.getInt("kills"));
		else this.kills = Optional.empty();
		PermissionDefault def;
		if (kills.isPresent() && kills.get() == 0) def = PermissionDefault.TRUE;
		else def = PermissionDefault.FALSE;
		super.getPermission().setDefault(def);
	}
	
	@Override
	public Optional<String> getDescription() {
		Optional<String> format = super.getDescription();
		String descriptionFormat;
		if (format.isPresent()) descriptionFormat = format.get();
		else return Optional.empty();
		
		String result;
		if (kills.isPresent()) result = descriptionFormat.replace("{kills}", kills.get().toString());
		else result = descriptionFormat;
		return Optional.of(result);
	}
	
	public final Optional<Integer> getKills() {
		return kills;
	}
	
	public static Optional<EnlistedRank> getEnlistedRank(Player player) {
		EnlistedRank result = null;
		for (EnlistedRank rank : Wasteland.getInstance().getSettings().enlistedRanks()) {
			if (player.hasPermission(rank.getPermission())) result = rank;
		}
		return Optional.ofNullable(result);
	}
	
	public static Optional<EnlistedRank> getRankFromKills(int kills) {
		if (kills < 0) throw new IllegalArgumentException("Number of kills must not be negative.");
		return Wasteland.getInstance().getSettings().enlistedRanks()
		  .stream().filter(rank -> rank.getKills().map(k -> k <= kills).orElse(false))
		  .reduce((acc, rank) -> rank);
	}
	
	public static Optional<EnlistedRank> getNextRank(Player player) {
		Optional<EnlistedRank> currentRank = getEnlistedRank(player);
		Stream<EnlistedRank> ranks = Wasteland.getInstance().getSettings().enlistedRanks().stream();
		if (!currentRank.isPresent()) {
			return ranks.filter(rank -> rank.kills.isPresent()).findFirst();
		}
		return ranks.filter(rank -> rank.kills.isPresent() && rank.isSuccessorOf(
				Wasteland.getInstance().getSettings().enlistedRanks().stream().map(x -> (Rank) x).collect(Collectors.toList())
					, currentRank.get())).findFirst();
	}
	
	public static class EnlistedRankComparator implements Comparator<EnlistedRank> {
		private final Comparator<Rank> delegate;
		
		public EnlistedRankComparator(Collection<EnlistedRank> ranks) {
			this.delegate = new RankComparator(
					ranks.stream().map((EnlistedRank x) -> (Rank) x).collect(Collectors.toList()));
		}

		@Override
		public int compare(EnlistedRank a, EnlistedRank b) {
			if (a.getKills().isPresent() && b.getKills().isPresent()) {
				if (a.getKills().get() > b.getKills().get()) return 1;
				if (b.getKills().get() > a.getKills().get()) return -1;
			}
			return delegate.compare(a, b);
		}
	}
}
