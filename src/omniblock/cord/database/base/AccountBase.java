package omniblock.cord.database.base;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import omniblock.cord.database.sql.make.MakeSQLQuery;
import omniblock.cord.database.sql.make.MakeSQLUpdate;
import omniblock.cord.database.sql.make.MakeSQLUpdate.TableOperation;
import omniblock.cord.database.sql.type.TableType;
import omniblock.cord.database.sql.util.Resolver;
import omniblock.cord.database.sql.util.SQLResultSet;
import omniblock.cord.database.sql.util.VariableUtils.StartVariables;

public class AccountBase {

	public static String getTags(ProxiedPlayer player) {

		return getTags(player.getName());
	}

	public static String getTags(String player) {

		MakeSQLQuery msq = new MakeSQLQuery(TableType.PLAYER_SETTINGS).select("p_settings").where("p_id",
				Resolver.getNetworkIDByName(player));

		try {
			SQLResultSet sqr = msq.execute();
			if (sqr.next()) {
				return sqr.get("p_settings");
			}
		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return StartVariables.P_SETTINGS.getInitial();
	}

	public static void setTags(String player, String tags) {

		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.PLAYER_SETTINGS, TableOperation.UPDATE);

		msu.rowOperation("p_settings", tags);
		msu.whereOperation("p_id", Resolver.getNetworkIDByName(player));

		try {

			msu.execute();

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

	}

	public static void setTags(ProxiedPlayer player, String tags) {

		setTags(player.getName(), tags);

	}

	public static void addTag(ProxiedPlayer player, String tag) {

		addTag(player.getName(), tag);

	}

	public static void addTag(String player, String tag) {

		List<String> tags = new ArrayList<>(Arrays.asList(getTags(player).split(",")));

		if (!tags.contains(tag.toLowerCase())) {
			tags.add(tag.toLowerCase());
		}

		String newtags = StringUtils.join(tags, ',');
		setTags(player, newtags);

	}

	public static void removeTag(ProxiedPlayer player, String tag) {

		removeTag(player.getName(), tag);

	}

	public static void removeTag(String player, String tag) {

		List<String> tags = new ArrayList<>(Arrays.asList(getTags(player).split(",")));

		tags.remove(tag.toLowerCase());

		String newtags = StringUtils.join(tags, ',');
		setTags(player, newtags);

	}

}
