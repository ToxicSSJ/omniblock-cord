package omniblock.cord.database.base;

import java.sql.SQLException;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import omniblock.cord.database.sql.make.MakeSQLQuery;
import omniblock.cord.database.sql.make.MakeSQLUpdate;
import omniblock.cord.database.sql.make.MakeSQLUpdate.TableOperation;
import omniblock.cord.database.sql.type.TableType;
import omniblock.cord.database.sql.util.Resolver;
import omniblock.cord.database.sql.util.SQLResultSet;
import omniblock.cord.database.sql.util.VariableUtils;

public class BankBase {

	public static int getMoney(ProxiedPlayer player) {
		return getMoney(player.getName());
	}

	public static int getMoney(String name) {

		MakeSQLQuery msq = new MakeSQLQuery(TableType.BANK_DATA).select("p_money").where("p_id",
				Resolver.getNetworkIDByName(name));

		try {
			
			SQLResultSet sqr = msq.execute();
			if (sqr.next()) {
				return sqr.get("p_money");
			}
			
		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return VariableUtils.BANK_INITIAL_MONEY;
	}
	
	public static void setMoney(ProxiedPlayer player, int quantity) {

		setMoney(player.getName(), quantity);
		return;

	}

	public static void setMoney(String player, int quantity) {

		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.BANK_DATA, TableOperation.UPDATE);

		msu.rowOperation("p_money", quantity);
		msu.whereOperation("p_id", Resolver.getNetworkIDByName(player));

		try {

			msu.execute();
			return;

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return;

	}
	
	public static void addMoney(ProxiedPlayer player, int quantity) {

		addMoney(player.getName(), quantity);
		return;

	}

	public static void addMoney(String player, int quantity) {

		int money = getMoney(player);
		setMoney(player, quantity + money);
		return;

	}
	
	public static int getExp(ProxiedPlayer player) {

		return getExp(player.getName());
	}
	
	public static int getExp(String player) {

		MakeSQLQuery msq = new MakeSQLQuery(TableType.BANK_DATA).select("p_exp").where("p_id",
				Resolver.getNetworkIDByName(player));

		try {
			SQLResultSet sqr = msq.execute();
			if (sqr.next()) {
				return sqr.get("p_exp");
			}

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return VariableUtils.BANK_INITIAL_EXP;
	}

	public static void setExp(ProxiedPlayer player, int quantity) {

		setExp(player.getName(), quantity);
		return;

	}
	
	public static void setExp(String player, int quantity) {

		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.BANK_DATA, TableOperation.UPDATE);

		msu.rowOperation("p_exp", quantity);
		msu.whereOperation("p_id", Resolver.getNetworkIDByName(player));

		try {

			msu.execute();
			return;

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return;

	}

	public static void addExp(ProxiedPlayer player, int quantity) {

		addExp(player.getName(), quantity);
		return;

	}

	public static void addExp(String player, int quantity) {

		int money = getExp(player);
		setExp(player, quantity + money);
		return;

	}
	
	public static int getPoints(ProxiedPlayer player) {

		return getPoints(player.getName());
	}
	
	public static int getPoints(String player) {

		MakeSQLQuery msq = new MakeSQLQuery(TableType.BANK_DATA).select("p_vip_points").where("p_id",
				Resolver.getNetworkIDByName(player));

		try {
			SQLResultSet sqr = msq.execute();
			if (sqr.next()) {
				return sqr.get("p_vip_points");
			}

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return VariableUtils.BANK_INITIAL_VIP_POINTS;
	}

	public static void setPoints(ProxiedPlayer player, int quantity) {

		setPoints(player.getName(), quantity);
		return;

	}

	public static void setPoints(String player, int quantity) {

		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.BANK_DATA, TableOperation.UPDATE);

		msu.rowOperation("p_vip_points", quantity);
		msu.whereOperation("p_id", Resolver.getNetworkIDByName(player));

		try {

			msu.execute();
			return;

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return;

	}
	
	public static void addPoints(ProxiedPlayer player, int quantity) {

		addPoints(player.getName(), quantity);
		return;

	}

	public static void addPoints(String player, int quantity) {

		int money = getPoints(player);
		setPoints(player, quantity + money);
		return;

	}
	
}
