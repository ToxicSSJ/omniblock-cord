package omniblock.cord.database.base;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import omniblock.cord.database.base.items.MembershipType;
import omniblock.cord.database.sql.make.MakeSQLQuery;
import omniblock.cord.database.sql.make.MakeSQLUpdate;
import omniblock.cord.database.sql.make.MakeSQLUpdate.TableOperation;
import omniblock.cord.database.sql.type.RankType;
import omniblock.cord.database.sql.type.TableType;
import omniblock.cord.database.sql.util.Resolver;
import omniblock.cord.database.sql.util.SQLResultSet;

import omniblock.cord.database.sql.util.VariableUtils;

public class RankBase {

	public static RankType getRank(ProxiedPlayer player) {

		return getRank(player.getName());

	}

	public static void setRank(ProxiedPlayer player, RankType rt) {

		setRank(player.getName(), rt);

	}

	public static RankType getRank(String player) {

		MakeSQLQuery msq = new MakeSQLQuery(TableType.RANK_DATA).select("p_rank").where("p_id",
				Resolver.getNetworkIDByName(player));

		try {
			SQLResultSet sqr = msq.execute();
			if (sqr.next()) {
				return RankType.valueOf(sqr.get("p_rank"));
			}

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return VariableUtils.RANK_INITIAL_RANK;
	}

	public static void setRank(String player, RankType rt) {

		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.RANK_DATA, TableOperation.UPDATE);

		msu.rowOperation("p_rank", rt.toString());
		msu.whereOperation("p_id", Resolver.getNetworkIDByName(player));

		try {

			msu.execute();
			return;

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

	}

	public static void removeTemporalMembership(String player) {

		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.RANK_DATA, TableOperation.UPDATE);

		msu.rowOperation("p_temp_rank", "");
		msu.rowOperation("p_temp_rank_expire", "");
		msu.whereOperation("p_id", Resolver.getNetworkIDByName(player));

		try {

			msu.execute();
			return;

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

	}

	public static void startTemporalMembership(String player, MembershipType type) {

		Date end = type.getEndDate();

		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.RANK_DATA, TableOperation.UPDATE);

		msu.rowOperation("p_temp_rank", type.getKey());
		msu.rowOperation("p_temp_rank_expire", parseExpireDate(end));
		msu.whereOperation("p_id", Resolver.getNetworkIDByName(player));

		try {

			msu.execute();

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		Entry<MembershipType, Date> matchentry = null;

		List<Entry<MembershipType, Date>> newmemberships = new ArrayList<Entry<MembershipType, Date>>();
		List<Entry<MembershipType, Date>> memberships = getMembershipLoot(player);

		List<String> textmemberships = new ArrayList<String>();

		for (Entry<MembershipType, Date> entry : memberships) {

			newmemberships.add(entry);

			if (entry.getKey() == type) {

				matchentry = entry;

			}

		}

		if (matchentry != null) {
			newmemberships.remove(matchentry);
		}

		for (Entry<MembershipType, Date> entry : newmemberships) {

			String trimmed = entry.getKey().getKey() + "#" + parseExpireDate(entry.getValue());
			textmemberships.add(trimmed);

		}

		setMembershipLoot(player, StringUtils.join(textmemberships, ","));

	}

	public static void addTimeToLoot(String player, MembershipType type) {

		List<Entry<MembershipType, Date>> newmemberships = new ArrayList<Entry<MembershipType, Date>>();
		List<Entry<MembershipType, Date>> memberships = getMembershipLoot(player);

		List<String> textmemberships = new ArrayList<String>();

		Date now = new Date();

		for (Entry<MembershipType, Date> entry : memberships) {

			Date end = entry.getValue();

			if (end.after(now)) {

				newmemberships.add(entry);

			}

		}

		for (Entry<MembershipType, Date> entry : newmemberships) {

			Date newdate = entry.getValue();
			newdate.setTime(entry.getValue().getTime() + TimeUnit.DAYS.toMillis(type.getDays()));

			String trimmed = entry.getKey().getKey() + "#" + parseExpireDate(newdate);
			textmemberships.add(trimmed);

		}

		setMembershipLoot(player, StringUtils.join(textmemberships, ","));

	}

	public static void updateMembershipLoot(String player) {

		List<Entry<MembershipType, Date>> newmemberships = new ArrayList<Entry<MembershipType, Date>>();
		List<Entry<MembershipType, Date>> memberships = getMembershipLoot(player);

		List<String> textmemberships = new ArrayList<String>();

		Date now = new Date();

		for (Entry<MembershipType, Date> entry : memberships) {

			Date end = entry.getValue();

			if (end.after(now)) {

				newmemberships.add(entry);

			}

		}

		for (Entry<MembershipType, Date> entry : newmemberships) {

			String trimmed = entry.getKey().getKey() + "#" + parseExpireDate(entry.getValue());
			textmemberships.add(trimmed);

		}

		setMembershipLoot(player, StringUtils.join(textmemberships, ","));

	}

	public static RankType getTempRank(String player) {

		MakeSQLQuery msq = new MakeSQLQuery(TableType.RANK_DATA).select("p_temp_rank").where("p_id",
				Resolver.getNetworkIDByName(player));

		try {

			SQLResultSet sqr = msq.execute();

			if (sqr.next()) {

				String trimtemprank = sqr.get("p_temp_rank");

				if (trimtemprank.equals("") || trimtemprank.equals("none")) {
					return null;
				}

				return MembershipType.fromKey(trimtemprank).getRank();
			}

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static Date getTempRankExpireDate(String player) {

		MakeSQLQuery msq = new MakeSQLQuery(TableType.RANK_DATA).select("p_temp_rank_expire").where("p_id",
				Resolver.getNetworkIDByName(player));

		try {

			SQLResultSet sqr = msq.execute();

			if (sqr.next()) {

				String trimtemprankexpire = sqr.get("p_temp_rank_expire");

				if (trimtemprankexpire.equals("") || trimtemprankexpire.equals("none")) {
					return null;
				}

				return parseExpireDate(trimtemprankexpire);
			}

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static List<Entry<MembershipType, Date>> getMembershipLoot(String player) {

		List<Entry<MembershipType, Date>> loot = new ArrayList<>();

		MakeSQLQuery msq = new MakeSQLQuery(TableType.RANK_DATA).select("p_loot").where("p_id",
				Resolver.getNetworkIDByName(player));

		try {

			SQLResultSet sqr = msq.execute();

			if (sqr.next()) {

				String trimloot = sqr.get("p_loot");

				if (trimloot.contains(",")) {

					String[] container = trimloot.split(",");

					for (String k : container) {

						if (k.contains("#")) {

							Entry<MembershipType, Date> entry = MembershipType.getSeparatedInfo(k);
							loot.add(entry);

						}

					}

				} else {

					if (!trimloot.equals("none"))
						if (!trimloot.equals("")) {

							Entry<MembershipType, Date> entry;
							entry = MembershipType.getSeparatedInfo(trimloot);
							loot.add(entry);

						}

				}

				return loot;

			}

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return loot;

	}

	public static void setMembershipLoot(String player, String loot) {

		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.RANK_DATA, TableOperation.UPDATE);

		msu.rowOperation("p_loot", loot);
		msu.whereOperation("p_id", Resolver.getNetworkIDByName(player));

		try {

			msu.execute();
			return;

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return;

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
