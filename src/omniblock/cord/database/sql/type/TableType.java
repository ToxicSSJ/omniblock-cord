/*
 *  Omniblock Developers Team - Copyright (C) 2016
 *
 *  This program is not a free software; you cannot redistribute it and/or modify it.
 *
 *  Only this enabled the editing and writing by the members of the team. 
 *  No third party is allowed to modification of the code.
 *
 */

package omniblock.cord.database.sql.type;

import java.sql.SQLException;
import java.sql.Statement;

import omniblock.cord.database.Database;
import omniblock.cord.database.sql.util.VariableUtils;

public enum TableType {
	
	RESOLVER("uuid_resolver", "CREATE TABLE IF NOT EXISTS uuid_resolver (id INTEGER PRIMARY KEY AUTO_INCREMENT, p_offline_uuid TINYTEXT NOT NULL, p_online_uuid TINYTEXT NOT NULL, p_resolver TINYTEXT NOT NULL, p_last_name TINYTEXT NOT NULL, register_date TIMESTAMP NOT NULL)", null),
	PLAYER_SETTINGS("player_settings", "CREATE TABLE IF NOT EXISTS player_settings (id INT PRIMARY KEY AUTO_INCREMENT, p_id TEXT(20) NOT NULL, p_pass TEXT(20) NOT NULL, p_email TEXT(20) NOT NULL, p_settings TEXT(20) NOT NULL )", "INSERT INTO player_settings (p_id, p_pass, p_email, p_settings) SELECT * FROM (SELECT VAR_P_ID a, VAR_P_PASS b, VAR_P_EMAIL c, VAR_P_SETTINGS d) AS tmp WHERE NOT EXISTS (SELECT 1 FROM player_settings WHERE p_id = VAR_P_ID );"),
	BOOSTERS_DATA("boosters_data", "CREATE TABLE IF NOT EXISTS boosters_data (id INT PRIMARY KEY AUTO_INCREMENT, p_id TEXT(20) NOT NULL, p_network_booster TEXT(20) NOT NULL, p_network_booster_gametype TEXT(20) NOT NULL, p_network_booster_expire TEXT(20) NOT NULL, p_personal_booster TEXT(20) NOT NULL, p_personal_booster_expire TEXT(20) NOT NULL )", "INSERT INTO boosters_data (p_id, p_network_booster, p_network_booster_gametype, p_network_booster_expire, p_personal_booster, p_personal_booster_expire) SELECT * FROM (SELECT VAR_P_ID a, VAR_P_COMMONBOOSTER b, VAR_P_COMMONBOOSTER c, VAR_P_COMMONBOOSTER d, VAR_P_COMMONBOOSTER e, VAR_P_COMMONBOOSTER f) AS tmp WHERE NOT EXISTS (SELECT 1 FROM boosters_data WHERE p_id = VAR_P_ID );"),
	RANK_DATA("rank_data", "CREATE TABLE IF NOT EXISTS rank_data (id INT PRIMARY KEY AUTO_INCREMENT, p_id TEXT(20) NOT NULL, p_rank TEXT(20) NOT NULL, p_temp_rank TEXT(20) NOT NULL, p_temp_rank_expire TEXT(20) NOT NULL, p_loot TEXT(20) NOT NULL )", "INSERT INTO rank_data (p_id, p_rank, p_temp_rank, p_temp_rank_expire, p_loot) SELECT * FROM (SELECT VAR_P_ID a, VAR_P_RANK b, VAR_P_TEMP_RANK c, VAR_P_TEMP_EXPIRE d, VAR_P_LOOT e) AS tmp WHERE NOT EXISTS (SELECT 1 FROM rank_data WHERE p_id = VAR_P_ID );"),
	BAN_DATA("ban_data", "CREATE TABLE IF NOT EXISTS ban_data (id INTEGER PRIMARY KEY AUTO_INCREMENT, p_id TEXT(20) NOT NULL, banned BOOLEAN NOT NULL )", "INSERT INTO ban_data (p_id, banned) SELECT * FROM (SELECT VAR_P_ID a, VAR_P_BAN b) AS tmp WHERE NOT EXISTS (SELECT 1 FROM ban_data WHERE p_id = VAR_P_ID );"),
	BETAKEYS("betakeys", "CREATE TABLE IF NOT EXISTS betakeys (id INTEGER PRIMARY KEY AUTO_INCREMENT, p_id TEXT(20) NOT NULL, p_key TEXT(10) NOT NULL )", null),
	BANK_DATA("bank_data", "CREATE TABLE IF NOT EXISTS bank_data (id INT PRIMARY KEY AUTO_INCREMENT, p_id TEXT(20) NOT NULL, p_money INT NOT NULL, p_exp INT NOT NULL, p_vip_points INT NOT NULL, p_items TEXT(20) NOT NULL )", "INSERT INTO bank_data (p_id, p_money, p_exp, p_vip_points, p_items) SELECT * FROM (SELECT VAR_P_ID a, VAR_P_MONEY b, VAR_P_EXP c, VAR_P_VIP_POINTS d, VAR_P_BANK_ITEMS e) AS tmp WHERE NOT EXISTS (SELECT 1 FROM bank_data WHERE p_id = VAR_P_ID );"),
	BAN_REGISTRY("ban_registry", "CREATE TABLE IF NOT EXISTS ban_registry (id INTEGER PRIMARY KEY AUTO_INCREMENT, ban_hash TEXT(20) NOT NULL, p_mod TEXT(20) NOT NULL, p_banned TEXT(20) NOT NULL, ban_reason TEXT(20) NOT NULL, ban_time_from TEXT(20) NOT NULL, ban_time_to TEXT(20) NOT NULL )", "INSERT INTO ban_registry (ban_hash, p_mod, p_banned, ban_reason, ban_time_from, ban_time_to ) VALUES (VAR_BAN_HASH, VAR_MOD, VAR_BANNED, VAR_REASON, VAR_BAN_TIME_FROM, VAR_BAN_TIME_TO);"),
	SKYWARS_DATA("skywars_data", "CREATE TABLE IF NOT EXISTS skywars_data (id INT PRIMARY KEY AUTO_INCREMENT,  p_id TEXT(20) NOT NULL, p_items TEXT(20) NOT NULL, p_extra_points TEXT(20) NOT NULL, p_selected TEXT(20) NOT NULL, p_last_games_average TEXT(20) NOT NULL )", "INSERT INTO skywars_data (p_id, p_items, p_extra_points, p_selected, p_last_games_average) SELECT * FROM (SELECT VAR_P_ID a, VAR_P_SKYWARS_ITEMS b, VAR_P_SKYWARS_EXTRA_POINTS d, VAR_P_SKYWARS_SELECTED e, VAR_P_SKYWARS_LAST_GAMES_AVERAGE f) AS tmp WHERE NOT EXISTS (SELECT 1 FROM skywars_data WHERE p_id = VAR_P_ID );"),
	TOP_STATS_SKYWARS("top_stats_skywars", "CREATE TABLE IF NOT EXISTS top_stats_skywars (id INTEGER PRIMARY KEY AUTO_INCREMENT, p_id TEXT(20) NOT NULL, kills INTEGER NOT NULL, deaths INTEGER NOT NULL, assists INTEGER NOT NULL, games INTEGER NOT NULL, wins INTEGER NOT NULL, average DOUBLE NOT NULL) ", "INSERT INTO top_stats_skywars (p_id, kills, deaths, assists, games, wins, average)  SELECT * FROM (SELECT VAR_P_ID a, VAR_P_SKYWARS_STATS_KILLS b, VAR_P_SKYWARS_STATS_DEATHS c, VAR_P_SKYWARS_STATS_ASSISTS d, VAR_P_SKYWARS_STATS_GAMES e, VAR_P_SKYWARS_STATS_WINS f, VAR_P_SKYWARS_STATS_AVERAGE g) AS tmp WHERE NOT EXISTS (SELECT 1 FROM top_stats_skywars WHERE p_id = VAR_P_ID );"),
	
	;
	
	private String table;

	private TableCreator creator;
	private TableInserter inserter;

	TableType(String table, String creator, String inserter) {

		this.table = table;

		this.creator = new TableCreator(table, creator);
		this.inserter = new TableInserter(this, table, inserter);

	}

	public String getTableName() {
		return table;
	}

	public TableCreator getCreator() {
		return creator;
	}

	public TableInserter getInserter() {
		return inserter;
	}
	
	public class TableInserter {

		private String table;
		private String inserter_sql;
		private TableType type;

		public TableInserter(TableType type, String table, String inserter) {
			this.type = type;
			this.table = table;
			this.inserter_sql = inserter;
		}

		public void insert(String player_name) throws SQLException {
			Statement stm = null;
			try {
				stm = Database.getConnection().createStatement();
				stm.executeUpdate(VariableUtils.formatVariables(inserter_sql, player_name, true));
			} finally {
				if (stm != null) {
					stm.close();
				}
			}
		}

		public boolean exists() {

			try {
				if (Database.getConnection().getMetaData().getTables(null, null, table, null).next()) {
					return true;
				}
			} catch(SQLException e) {
				return false;
			}

			return false;

		}

		public String getInserterSQL() {
			return inserter_sql;
		}

		public String getTable() {
			return table;
		}

		public TableType getType() {
			return type;
		}

	}
	
	public class TableCreator {

		private String table;
		private String creator_sql;

		public TableCreator(String table, String creator) {
			this.table = table;
			this.creator_sql = creator;
		}

		public void make(Statement stm) {

			try {
				stm.executeUpdate(creator_sql);
			} catch(Exception e) {
				e.printStackTrace();
			}

		}

		public boolean exists() {

			try {
				if (Database.getConnection().getMetaData().getTables(null, null, table, null).next()) {
					return true;
				}
			} catch(SQLException e) {
				return false;
			}

			return false;

		}

		public String getCreatorSQL() {
			return creator_sql;
		}

		public String getTable() {
			return table;
		}

	}
	
}
