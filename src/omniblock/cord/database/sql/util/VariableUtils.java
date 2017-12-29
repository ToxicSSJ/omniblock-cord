package omniblock.cord.database.sql.util;

import org.apache.commons.lang3.StringUtils;

import omniblock.cord.database.sql.type.ItemType;
import omniblock.cord.database.sql.type.RankType;

import omniblock.cord.database.base.AuthBase;

public class VariableUtils {

	public static final String COMMON_SEPARATOR = ";";

	public static RankType RANK_INITIAL_RANK = RankType.USER;

	public static int BANK_INITIAL_EXP = 0;
	public static int BANK_INITIAL_MONEY = 100;
	public static int BANK_INITIAL_VIP_POINTS = 0;
	public static String BANK_INITIAL_ITEMS = ItemType.GIF_MINI_COW_PET.getHashid();

	public static String SKYWARS_INITIAL_ITEMS = "{}";
	public static int SKYWARS_INITIAL_EXTRA_POINTS = 0;
	public static int SKYWARS_WEEKPRIZE_POINTS = 0;
	public static String SKYWARS_INITIAL_SELECTED = "J0;K0;B0;D0";
	public static String SKYWARS_INITIAL_LAST_GAMES_AVERAGES = "{}";

	public static String GADGETS_INITIAL_GADGETS = "{}";

	public static int BAN_STATUS = 0;

	public static String formatVariables(String SQL, String name, boolean player, boolean resolver) {

		if (player) {

			if (resolver) {

				if (SQL.contains("VAR_P_RESOLVER")) {
					SQL = SQL.replaceAll("VAR_P_RESOLVER", "'" + Resolver.getNetworkIDByName(name) + "'");
				}
				if (SQL.contains("VAR_P_LAST_NAME")) {
					SQL = SQL.replaceAll("VAR_P_LAST_NAME", "'" + name + "'");
				}
				if (SQL.contains("VAR_P_OFFLINE_UUID")) {
					SQL = SQL.replaceAll("VAR_P_OFFLINE_UUID", "'" + Resolver.getOfflineUUIDByName(name) + "'");
				}
				if (SQL.contains("VAR_P_ONLINE_UUID")) {
					SQL = SQL.replaceAll("VAR_P_ONLINE_UUID", "'" + Resolver.getOnlineUUIDByName(name) + "'");
				}

			} else {

				if (SQL.contains("VAR_P_ID")) {
					SQL = SQL.replaceAll("VAR_P_ID", "'" + Resolver.getNetworkIDByName(name) + "'");
				}
				if (SQL.contains("VAR_P_LAST_NAME")) {
					SQL = SQL.replaceAll("VAR_P_LAST_NAME", "'" + name + "'");
				}
				if (SQL.contains("VAR_P_OFFLINE_UUID")) {
					SQL = SQL.replaceAll("VAR_P_OFFLINE_UUID", "'" + Resolver.getOfflineUUIDByName(name) + "'");
				}
				if (SQL.contains("VAR_P_ONLINE_UUID")) {
					SQL = SQL.replaceAll("VAR_P_ONLINE_UUID", "'" + Resolver.getOnlineUUIDByName(name) + "'");
				}

			}

		}

		for (StartVariables sv: StartVariables.values()) {
			if (SQL.contains(sv.getKey())) {
				SQL = StringUtils.replace(SQL, sv.getKey(), "'" + sv.getInitial() + "'");
			}
		}

		return SQL;

	}

	public static String formatVariables(String SQL, String name, boolean player) {
		return formatVariables(SQL, name, player, false);
	}

	public enum StartVariables {

		P_RANK("VAR_P_RANK", RANK_INITIAL_RANK),
		P_TEMPORAL_RANK("VAR_P_TEMP_RANK", ""),
		P_TEMPORAL_RANK_EXPIRE("VAR_P_TEMP_EXPIRE", ""),

		P_LOOT("VAR_P_LOOT", ""),

		P_EXP("VAR_P_EXP", BANK_INITIAL_EXP),
		P_MONEY("VAR_P_MONEY", BANK_INITIAL_MONEY),
		P_VIP_POINTS("VAR_P_VIP_POINTS", BANK_INITIAL_VIP_POINTS),
		P_BANK_ITEMS("VAR_P_BANK_ITEMS", BANK_INITIAL_ITEMS),

		P_PASS("VAR_P_PASS", AuthBase.DEFAULT_PASS),
		P_EMAIL("VAR_P_EMAIL", "omniblockmc@gmail.com"),
		P_COMMON_BOOSTER("VAR_P_COMMONBOOSTER", "NONE"),
		P_SETTINGS("VAR_P_SETTINGS", "spanish,iplogin,privatemsg,texturesound,friendrequest,seeplayers"),

		P_SKYWARS_ITEMS("VAR_P_SKYWARS_ITEMS", SKYWARS_INITIAL_ITEMS),
		P_SKYWARS_EXTRA_POINTS("VAR_P_SKYWARS_EXTRA_POINTS", SKYWARS_INITIAL_EXTRA_POINTS),
		P_SKYWARS_SELECTED("VAR_P_SKYWARS_SELECTED", SKYWARS_INITIAL_SELECTED),
		P_SKYWARS_LAST_GAME_AVERAGE("VAR_P_SKYWARS_LAST_GAMES_AVERAGE", SKYWARS_INITIAL_LAST_GAMES_AVERAGES),
		P_SKYWARS_WEEKPRIZE("VAR_P_SKYWARS_WEEKPRIZE", SKYWARS_WEEKPRIZE_POINTS),

		P_SKYWARS_STATS_KILLS("VAR_P_SKYWARS_STATS_KILLS", 0),
		P_SKYWARS_STATS_DEATHS("VAR_P_SKYWARS_STATS_DEATHS", 0),
		P_SKYWARS_STATS_ASSISTS("VAR_P_SKYWARS_STATS_ASSISTS", 0),
		P_SKYWARS_STATS_GAMES("VAR_P_SKYWARS_STATS_GAMES", 0),
		P_SKYWARS_STATS_WINS("VAR_P_SKYWARS_STATS_WINS", 0),
		P_SKYWARS_STATS_AVERAGE("VAR_P_SKYWARS_STATS_AVERAGE", 0.0),

		P_GADGETS("VAR_P_GADGETS", GADGETS_INITIAL_GADGETS),

		P_BAN_STATUS("VAR_P_BAN", BAN_STATUS),

		;

		private String
		var;
		private String initial;

		StartVariables(String
		var, Object initial) {
			this.
			var =
			var;
			this.initial = initial.toString();
		}

		public String getKey() {
			return var;
		}

		public String getInitial() {
			return initial;
		}

	}

}