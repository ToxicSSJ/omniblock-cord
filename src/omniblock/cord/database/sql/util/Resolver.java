/*
 *  Omniblock Developers Team - Copyright (C) 2016
 *
 *  This program is not a free software; you cannot redistribute it and/or modify it.
 *
 *  Only this enabled the editing and writing by the members of the team. 
 *  No third party is allowed to modification of the code.
 *
 */
package omniblock.cord.database.sql.util;

import java.sql.SQLException;
import java.util.UUID;

import com.google.common.base.Charsets;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import omniblock.cord.database.sql.make.MakeSQLQuery;

public class Resolver {

	public static UUID getOfflineUUID(ProxiedPlayer p) {
		return getOfflineUUIDByName(p.getName());
	}

	public static UUID getOfflineUUIDByNetworkID(String networkID) {
		MakeSQLQuery sql = new MakeSQLQuery("uuid_resolver").select("p_offline_uuid").where("p_resolver", networkID);

		try {
			SQLResultSet sqr = sql.execute();
			if (sqr.next()) {
				return UUID.fromString(sqr.get("p_offline_uuid"));
			}
		} catch (SQLException | IllegalArgumentException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static UUID getOfflineUUIDByName(String name) {
		return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8));
	}

	public static boolean isPremium(ProxiedPlayer p) {
		return getOnlineUUID(p) != null;
	}

	public static boolean isPremium(String networkID) {
		return getOnlineUUIDByNetworkID(networkID) != null;
	}

	public static UUID getOnlineUUID(ProxiedPlayer p) {
		return getOnlineUUIDByName(p.getName());
	}

	public static UUID getOnlineUUIDByNetworkID(String networkID) {
		MakeSQLQuery sql = new MakeSQLQuery("uuid_resolver").select("p_online_uuid").where("p_resolver", networkID);

		try {
			SQLResultSet sqr = sql.execute();
			if (sqr.next()) {
				String onlineUUID = sqr.get("p_online_uuid");
				if (onlineUUID == null || onlineUUID.equalsIgnoreCase("NONE")) {
					return null;
				}

				return UUID.fromString(sqr.get("p_online_uuid"));
			}
		} catch (SQLException | IllegalArgumentException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static UUID getOnlineUUIDByName(String name) {
		MakeSQLQuery sql = new MakeSQLQuery("uuid_resolver").select("p_online_uuid").where("p_offline_uuid",
				getOfflineUUIDByName(name).toString());

		try {
			SQLResultSet sqr = sql.execute();
			if (sqr.next()) {
				String onlineUUID = sqr.get("p_online_uuid");
				if (onlineUUID == null || onlineUUID.equalsIgnoreCase("NONE")) {
					return null;
				}

				return UUID.fromString(sqr.get("p_online_uuid"));
			}
		} catch (SQLException | IllegalArgumentException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getNetworkID(ProxiedPlayer p) {
		return getNetworkIDByName(p.getName());
	}

	public static String getNetworkIDByName(String name) {
		MakeSQLQuery sql = new MakeSQLQuery("uuid_resolver").select("p_resolver").where("p_offline_uuid",
				getOfflineUUIDByName(name).toString());

		try {
			SQLResultSet sqr = sql.execute();
			if (sqr.next()) {
				return sqr.get("p_resolver");
			}
		} catch (SQLException | IllegalArgumentException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getLastName(ProxiedPlayer p) {
		MakeSQLQuery sql = new MakeSQLQuery("uuid_resolver").select("p_last_name").where("p_offline_uuid",
				getOfflineUUIDByName(p.getName()).toString());

		try {
			SQLResultSet sqr = sql.execute();
			if (sqr.next()) {
				return sqr.get("p_last_name");
			}
		} catch (SQLException | IllegalArgumentException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getLastNameByNetworkID(String networkID) {
		MakeSQLQuery sql = new MakeSQLQuery("uuid_resolver").select("p_last_name").where("p_resolver", networkID);

		try {
			SQLResultSet sqr = sql.execute();
			if (sqr.next()) {
				return sqr.get("p_last_name");
			}
		} catch (SQLException | IllegalArgumentException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static boolean hasLastName(String name) {
		MakeSQLQuery sql = new MakeSQLQuery("uuid_resolver").select("p_last_name").where("p_last_name", name);

		try {
			SQLResultSet sqr = sql.execute();
			return sqr.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static boolean hasNetworkID(String networkID) {
		MakeSQLQuery sql = new MakeSQLQuery("uuid_resolver").select("p_resolver").where("p_resolver", networkID);

		try {
			SQLResultSet sqr = sql.execute();
			return sqr.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static boolean hasOfflineUUID(UUID offlineUUID) {
		MakeSQLQuery sql = new MakeSQLQuery("uuid_resolver").select("p_offline_uuid").where("p_offline_uuid",
				offlineUUID.toString());

		try {
			SQLResultSet sqr = sql.execute();
			return sqr.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static boolean hasOnlineUUID(UUID onlineUUID) {
		MakeSQLQuery sql = new MakeSQLQuery("uuid_resolver").select("p_online_uuid").where("p_online_uuid",
				onlineUUID.toString());

		try {
			SQLResultSet sqr = sql.execute();
			return sqr.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

}
