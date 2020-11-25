package me.jamestmartin.wasteland.towny;

import java.util.Optional;

import org.bukkit.entity.Player;

/**
 * Provides the tags for the town and nation that a player is a member of,
 * if Towny is installed and the player is a member of a town or nation.
 */
public interface TownyTagProvider {
    /**
     * <p>
     * A town tag is a short identifier for a town which the town may opt to set.
     * It may be used in places where the town's full name would be too long,
     * for example to include what town a player is member of in chat.
     * <p>
     * If Towny is installed, the player is a member of a town,
     * and that town has opted to set a town tag,
     * then this method will get that town tag.
     * 
     * @param player
     * @return
     *   If the player is in a town which has a town tag, that town tag;
     *   otherwise, the return value will be empty.
     * @see #getNationTag(Player)
     */
	public Optional<String> getTownTag(Player player);
	
	/**
     * <p>
     * A nation tag is a short identifier for a nation which the nation may opt to set.
     * It may be used in places where the nation's full name would be too long,
     * for example to include what nation a player is member of in chat.
     * <p>
     * If Towny is installed, the player is a member of a nation,
     * and that nation has opted to set a nation tag,
     * then this method will get that nation tag.
     * 
     * @param player
     * @return
     *   If the player is in a nation which has a nation tag, that town nation;
     *   otherwise, the return value will be empty.
     * @see #getTownTag(Player)
     */
	public Optional<String> getNationTag(Player player);
}
