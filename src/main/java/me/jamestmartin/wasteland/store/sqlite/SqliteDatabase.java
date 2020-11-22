package me.jamestmartin.wasteland.store.sqlite;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.entity.Player;

import me.jamestmartin.wasteland.kills.KillsStore;
import me.jamestmartin.wasteland.kit.KitStore;

public class SqliteDatabase implements AutoCloseable, KillsStore, KitStore {
	private static final String CREATE_KILLS_TABLE =
			"CREATE TABLE IF NOT EXISTS `player_kills`"
			+ "( `player` VARCHAR(36) PRIMARY KEY"
			+ ", `kills`  UNSIGNED INT NOT NULL"
			+ ")";
	private static final String INIT_PLAYER_KILLS =
			"INSERT OR IGNORE INTO `player_kills`(`player`, `kills`) VALUES (?, 0)";
	private static final String GET_PLAYER_KILLS =
			"SELECT `kills` FROM `player_kills` WHERE `player` = ?";
	private static final String ADD_PLAYER_KILLS =
			"UPDATE `player_kills` SET `kills`=`kills` + ? WHERE `player` = ?";
	private static final String SET_PLAYER_KILLS =
			"UPDATE `player_kills` SET `kills` = ? WHERE `player` = ?";

	private final PreparedStatement psInitPlayerKills;
	private final PreparedStatement psGetPlayerKills;
	private final PreparedStatement psAddPlayerKills;
	private final PreparedStatement psSetPlayerKills;
    
    private static final String CREATE_PLAYER_KITS_TABLE =
            "CREATE TABLE IF NOT EXISTS `player_kits`"
            + "( `player`     VARCHAR(36) PRIMARY KEY"
            + ", `total_kits` UNSIGNED INT NOT NULL"
            + ", `last_time`  UNSIGNED INT"
            + ")";
    private static final String INIT_PLAYER_KITS =
            "INSERT OR IGNORE INTO `player_kits`(`player`, `total_kits`, `last_time`) VALUES (?, 0, 0)";
    private static final String GET_PLAYER_LAST_KIT_TIME =
            "SELECT `last_time` FROM `player_kits` WHERE `player` = ?";
    private static final String GET_PLAYER_TOTAL_KITS =
            "SELECT `total_kits` FROM `player_kits` WHERE `player` = ?";
    private static final String USE_PLAYER_KIT =
            "UPDATE `player_kits` SET `total_kits` = `total_kits` + 1, `last_time` = ? WHERE `player` = ?";
    
    private final PreparedStatement psInitPlayerKits;
    private final PreparedStatement psGetPlayerLastKitTime;
    private final PreparedStatement psGetPlayerTotalKits;
    private final PreparedStatement psUsePlayerKit;
    
    private final Connection connection;
	
	public SqliteDatabase(File file)
		throws IOException, ClassNotFoundException, SQLException {
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		// Ensures that JDBC is installed.
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:" + file);
		// I want to let this be true, but it doesn't work if it's true.
		// I can't figure out why.
		connection.setAutoCommit(false);
		
		// Initialize database tables
		Statement s = connection.createStatement();
		s.executeUpdate(CREATE_KILLS_TABLE);
		s.executeUpdate(CREATE_PLAYER_KITS_TABLE);
		s.close();
		
		psInitPlayerKills = connection.prepareStatement(INIT_PLAYER_KILLS);
		psGetPlayerKills = connection.prepareStatement(GET_PLAYER_KILLS);
		psAddPlayerKills = connection.prepareStatement(ADD_PLAYER_KILLS);
		psSetPlayerKills = connection.prepareStatement(SET_PLAYER_KILLS);
		
		psInitPlayerKits = connection.prepareStatement(INIT_PLAYER_KITS);
		psGetPlayerLastKitTime = connection.prepareStatement(GET_PLAYER_LAST_KIT_TIME);
		psGetPlayerTotalKits = connection.prepareStatement(GET_PLAYER_TOTAL_KITS);
		psUsePlayerKit = connection.prepareStatement(USE_PLAYER_KIT);
	}
    
    public void initPlayer(Player player) throws SQLException {
        String playerUUID = player.getUniqueId().toString();
        psInitPlayerKills.setString(1, playerUUID);
        psInitPlayerKills.executeUpdate();
        psInitPlayerKits.setString(1, playerUUID);
        psInitPlayerKits.executeUpdate();
        connection.commit();
    }
	
	@Override
	public int getPlayerKills(Player player) throws SQLException
	{
		String playerUUID = player.getUniqueId().toString();
		psGetPlayerKills.setString(1, playerUUID);
		try (ResultSet result = psGetPlayerKills.executeQuery()) {
			if (!result.next()) throw new SQLException("No result for player kills");
			return result.getInt("kills");
		}
	}
	
	@Override
	public void addPlayerKills(Player player, int kills) throws SQLException {
		String playerUUID = player.getUniqueId().toString();
        psAddPlayerKills.setInt(1, kills);
		psAddPlayerKills.setString(2, playerUUID);
		psAddPlayerKills.executeUpdate();;
		connection.commit();
	}
	
	@Override
	public void setPlayerKills(Player player, int kills) throws SQLException {
		if (kills < 0)
			throw new IllegalArgumentException("Number of kills must not be negative: " + kills);
		String playerUUID = player.getUniqueId().toString();
		psSetPlayerKills.setInt(1, kills);
		psSetPlayerKills.setString(2, playerUUID);
		psSetPlayerKills.executeUpdate();
		connection.commit();
	}

    @Override
    public long getLastKitTime(Player player) throws SQLException {
        String playerUUID = player.getUniqueId().toString();
        psGetPlayerLastKitTime.setString(1, playerUUID);
        try (ResultSet result = psGetPlayerLastKitTime.executeQuery()) {
            if (!result.next()) throw new SQLException("No result for player last kit time");
            // FIXME: This value can be null.
            return result.getLong("last_time");
        }
    }

    @Override
    public int getTotalKitsRecieved(Player player) throws SQLException {
        String playerUUID = player.getUniqueId().toString();
        psGetPlayerTotalKits.setString(1, playerUUID);
        try (ResultSet result = psGetPlayerTotalKits.executeQuery()) {
            if (!result.next()) throw new SQLException("No result for player total kits received");
            return result.getInt("total_kits");
        }
    }

    @Override
    public void useKit(Player player) throws SQLException {
        String playerUUID = player.getUniqueId().toString();
        psUsePlayerKit.setLong(1, player.getWorld().getFullTime());
        psUsePlayerKit.setString(2, playerUUID);
        psUsePlayerKit.executeUpdate();
        connection.commit();
    }
	
	@Override
	public void close() throws SQLException {
		psInitPlayerKills.close();
		psGetPlayerKills.close();
		psAddPlayerKills.close();
		psSetPlayerKills.close();
		
		psInitPlayerKits.close();
		psGetPlayerLastKitTime.close();
		psGetPlayerTotalKits.close();
		psUsePlayerKit.close();
		
		connection.commit();
		connection.close();
	}
}
