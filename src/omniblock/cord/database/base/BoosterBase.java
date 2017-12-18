package omniblock.cord.database.base;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import omniblock.cord.database.base.helpers.AccountHelper.AccountBoosterType;
import omniblock.cord.database.base.items.BoosterType;
import omniblock.cord.database.base.items.NetworkBoosterType;
import omniblock.cord.database.sql.make.MakeSQLQuery;
import omniblock.cord.database.sql.make.MakeSQLUpdate;
import omniblock.cord.database.sql.make.MakeSQLUpdate.TableOperation;
import omniblock.cord.database.sql.type.TableType;
import omniblock.cord.database.sql.util.Resolver;
import omniblock.cord.database.sql.util.SQLResultSet;
import omniblock.cord.database.sql.util.VariableUtils.StartVariables;

public class BoosterBase {

	public static void startBooster(ProxiedPlayer player, String key, AccountBoosterType type, String... args) {

		startBooster(player.getName(), key, type, args);
		return;

	}

	public static void startBooster(String player, String key, AccountBoosterType type, String... args) {

		String general_row = type == AccountBoosterType.PERSONAL_BOOSTER ? "p_personal_booster" : "p_network_booster";
		String general_time_row = type == AccountBoosterType.PERSONAL_BOOSTER ? "p_personal_booster_expire"
				: "p_network_booster_expire";

		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.BOOSTERS_DATA, TableOperation.UPDATE);

		msu.rowOperation(general_row, key);
		msu.rowOperation(general_time_row, parseExpireDate(type == AccountBoosterType.PERSONAL_BOOSTER
				? BoosterType.fromKey(key).getEndDate() : NetworkBoosterType.fromKey(key).getEndDate()));
		if (type == AccountBoosterType.NETWORK_BOOSTER) {
			msu.rowOperation("p_network_booster_gametype", args[0]);
		}
		msu.whereOperation("p_id", Resolver.getNetworkIDByName(player));

		try {

			msu.execute();
			return;

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return;

	}

	public static void removeEnabledBooster(ProxiedPlayer player, AccountBoosterType type) {

		removeEnabledBooster(player.getName(), type);
		return;
	}

	public static void removeEnabledBooster(String player, AccountBoosterType type) {

		String general_row = type == AccountBoosterType.PERSONAL_BOOSTER ? "p_personal_booster" : "p_network_booster";
		String general_time_row = type == AccountBoosterType.PERSONAL_BOOSTER ? "p_personal_booster_expire"
				: "p_network_booster_expire";

		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.BOOSTERS_DATA, TableOperation.UPDATE);

		msu.rowOperation(general_row, StartVariables.P_COMMON_BOOSTER.getInitial());
		msu.rowOperation(general_time_row, StartVariables.P_COMMON_BOOSTER.getInitial());
		if (type == AccountBoosterType.NETWORK_BOOSTER) {
			msu.rowOperation("p_network_booster_gametype", StartVariables.P_COMMON_BOOSTER.getInitial());
		}
		msu.whereOperation("p_id", Resolver.getNetworkIDByName(player));

		try {

			msu.execute();
			return;

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return;
	}

	public static String getEnabledBooster(ProxiedPlayer player, AccountBoosterType type) {

		String select = type == AccountBoosterType.PERSONAL_BOOSTER ? "p_personal_booster" : "p_network_booster";

		MakeSQLQuery msq = new MakeSQLQuery(TableType.BOOSTERS_DATA).select(select).where("p_id",
				Resolver.getNetworkID(player));

		try {

			SQLResultSet sqr = msq.execute();

			if (sqr.next()) {
				return sqr.get(select);
			}

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return StartVariables.P_COMMON_BOOSTER.getInitial();
	}

	public static String getNetworkBoosterGameType(ProxiedPlayer player) {

		String select = "p_network_booster_gametype";

		MakeSQLQuery msq = new MakeSQLQuery(TableType.BOOSTERS_DATA).select(select).where("p_id",
				Resolver.getNetworkID(player));

		try {
			SQLResultSet sqr = msq.execute();
			if (sqr.next()) {
				return sqr.get(select);
			}
		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return StartVariables.P_COMMON_BOOSTER.getInitial();
	}

	public static String getExpireDate(ProxiedPlayer player, AccountBoosterType type) {

		String select = type == AccountBoosterType.PERSONAL_BOOSTER ? "p_personal_booster_expire"
				: "p_network_booster_expire";

		MakeSQLQuery msq = new MakeSQLQuery(TableType.BOOSTERS_DATA).select(select).where("p_id",
				Resolver.getNetworkID(player));

		try {
			SQLResultSet sqr = msq.execute();
			if (sqr.next()) {
				return sqr.get(select);
			}
		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return StartVariables.P_COMMON_BOOSTER.getInitial();
	}

	public static String parseExpireDate(Date expiredate) {

		SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return curFormater.format(expiredate);

	}

	public static Date parseExpireDate(String expiredate) {

		SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateObj = new Date();

		try {
			dateObj = curFormater.parse(expiredate);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dateObj;
	}

}
