package omniblock.cord.database.base.helpers;

import omniblock.cord.database.sql.type.TableType;

public class BanHelper {

	public static String getInsertSQL(String[] data) {

		if (data.length >= 6) {

			String SQL = TableType.BAN_REGISTRY.getInserter().getInserterSQL();

			if (SQL.contains("VAR_BAN_HASH")) {
				SQL = SQL.replaceAll("VAR_BAN_HASH", "'" + data[0] + "'");
			}
			if (SQL.contains("VAR_MOD")) {
				SQL = SQL.replaceAll("VAR_MOD", "'" + data[1] + "'");
			}
			if (SQL.contains("VAR_BANNED")) {
				SQL = SQL.replaceAll("VAR_BANNED", "'" + data[2] + "'");
			}
			if (SQL.contains("VAR_REASON")) {
				SQL = SQL.replaceAll("VAR_REASON", "'" + data[3] + "'");
			}
			if (SQL.contains("VAR_BAN_TIME_FROM")) {
				SQL = SQL.replaceAll("VAR_BAN_TIME_FROM", "'" + data[4] + "'");
			}
			if (SQL.contains("VAR_BAN_TIME_TO")) {
				SQL = SQL.replaceAll("VAR_BAN_TIME_TO", "'" + data[5] + "'");
			}

			return SQL;

		}
		
		return "";

	}
	
}
