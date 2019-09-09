package me.jtmar.wasteland;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.entity.Player;

public class Database implements AutoCloseable {
	private static final String CREATE_KILLS_TABLE =
			"CREATE TABLE IF NOT EXISTS player_kills"
			+ "( `player` VARCHAR(36) PRIMARY KEY"
			+ ", `kills`  UNSIGNED INT NOT NULL"
			+ ")";
	private static final String INIT_PLAYER_KILLS =
			"INSERT OR IGNORE INTO `player_kills`(`player`, `kills`) VALUES (?, 0)";
	private static final String GET_PLAYER_KILLS =
			"SELECT `kills` FROM `player_kills` WHERE `player` = ?";
	private static final String INCREMENT_PLAYER_KILLS =
			"UPDATE `player_kills` SET `kills`=`kills` + 1 WHERE `player` = ?";
	private static final String SET_PLAYER_KILLS =
			"UPDATE `player_kills` SET `kills` = ? WHERE `player` = ?";
	
	private final Connection connection;
	private final PreparedStatement psInitPlayerKills;
	private final PreparedStatement psGetPlayerKills;
	private final PreparedStatement psIncrementPlayerKills;
	private final PreparedStatement psSetPlayerKills;
	
	public Database(File file)
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
		s.close();
		
		psInitPlayerKills = connection.prepareStatement(INIT_PLAYER_KILLS);
		psGetPlayerKills = connection.prepareStatement(GET_PLAYER_KILLS);
		psIncrementPlayerKills = connection.prepareStatement(INCREMENT_PLAYER_KILLS);
		psSetPlayerKills = connection.prepareStatement(SET_PLAYER_KILLS);
	}
	
	public void initPlayerKills(Player player) throws SQLException {
		String playerUUID = player.getUniqueId().toString();
		psInitPlayerKills.setString(1, playerUUID);
		psInitPlayerKills.executeUpdate();
		connection.commit();
	}
	
	public int getPlayerKills(Player player) throws SQLException
	{
		String playerUUID = player.getUniqueId().toString();
		psGetPlayerKills.setString(1, playerUUID);
		try (ResultSet result = psGetPlayerKills.executeQuery()) {
			if (!result.next()) return 0;
			return result.getInt("kills");
		}
	}
	
	public void incrementPlayerKills(Player player) throws SQLException {
		String playerUUID = player.getUniqueId().toString();
		psIncrementPlayerKills.setString(1, playerUUID);
		psIncrementPlayerKills.executeUpdate();;
		connection.commit();
	}
	
	public void setPlayerKills(Player player, int kills) throws SQLException {
		if (kills < 0)
			throw new IllegalArgumentException("Number of kills must not be negative: " + kills);
		String playerUUID = player.getUniqueId().toString();
		psSetPlayerKills.setInt(1, kills);
		psSetPlayerKills.setString(2, playerUUID);
		psSetPlayerKills.executeUpdate();;
		connection.commit();
	}
	
	@Override
	public void close() throws SQLException {
		psInitPlayerKills.close();
		psGetPlayerKills.close();
		psIncrementPlayerKills.close();
		psSetPlayerKills.close();
		connection.commit();
		connection.close();
	}
}
