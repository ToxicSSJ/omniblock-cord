package omniblock.cord.database.base;

import java.sql.SQLException;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.packet.PlayerLoginEvaluatePacket;
import net.omniblock.packets.network.structure.packet.PlayerLoginSucessPacket;
import net.omniblock.packets.network.structure.packet.PlayerSendToServerPacket;
import net.omniblock.packets.network.structure.type.PacketSenderType;
import net.omniblock.packets.object.external.ServerType;
import omniblock.cord.database.base.helpers.AccountHelper;
import omniblock.cord.database.base.helpers.AccountHelper.AccountTagType;
import omniblock.cord.database.sql.make.MakeSQLQuery;
import omniblock.cord.database.sql.make.MakeSQLUpdate;
import omniblock.cord.database.sql.make.MakeSQLUpdate.TableOperation;
import omniblock.cord.database.sql.type.TableType;
import omniblock.cord.database.sql.util.Resolver;
import omniblock.cord.database.sql.util.SQLResultSet;
import omniblock.cord.util.TextUtil;

public class AuthBase {

	public static final String DEFAULT_PASS = "$ZPASS";

	public static void setPassword(ProxiedPlayer player, String password) {

		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.PLAYER_SETTINGS, TableOperation.UPDATE);

		msu.rowOperation("p_pass", password);
		msu.whereOperation("p_id", Resolver.getNetworkID(player));

		try {

			msu.execute();
			return;

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

	}

	public static String getPassword(ProxiedPlayer player) {

		MakeSQLQuery msq = new MakeSQLQuery(TableType.PLAYER_SETTINGS).select("p_pass").where("p_id",
				Resolver.getNetworkID(player));

		try {

			SQLResultSet sqr = msq.execute();

			if (sqr.next()) {
				return sqr.get("p_pass");
			}

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return DEFAULT_PASS;

	}

	public static boolean isRegister(ProxiedPlayer player) {

		MakeSQLQuery msq = new MakeSQLQuery(TableType.PLAYER_SETTINGS).select("p_pass").where("p_id",
				Resolver.getNetworkID(player));

		try {

			SQLResultSet sqr = msq.execute();

			if (sqr.next()) {

				String pass = sqr.get("p_pass");

				return !pass.equalsIgnoreCase(DEFAULT_PASS);

			}

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return false;

	}

	public static void sucess(ProxiedPlayer player) {

		Packets.STREAMER.streamPacket(new PlayerLoginSucessPacket().setPlayername(player.getName())
				.useIPLogin(AccountHelper.hasTag(AccountTagType.IP_LOGIN, AccountBase.getTags(player))).build()
				.setReceiver(PacketSenderType.OMNICORD));

	}

	@SuppressWarnings("deprecation")
	public static void evaluate(ProxiedPlayer player) {

		player.sendMessage(TextUtil.format("&8&lC&8uentas &6&l» &fSe está comprobando el estado de tu cuenta..."));

		if (Resolver.getOnlineUUIDByName(player.getName()) == null) {

			Packets.STREAMER.streamPacket(new PlayerLoginEvaluatePacket().setPlayername(player.getName())
					.useIPLogin(AccountHelper.hasTag(AccountTagType.IP_LOGIN, AccountBase.getTags(player))).build()
					.setReceiver(PacketSenderType.OMNICORD));
			return;

		}

		player.sendMessage(TextUtil
				.format("&8&lC&8uentas &a&l» &aHas sido logeado automaticamente porque eres un usuario premium!"));

		Packets.STREAMER.streamPacket(new PlayerSendToServerPacket().setPlayername(player.getName())
				.setServertype(ServerType.MAIN_LOBBY_SERVER).setParty(false).build()
				.setReceiver(PacketSenderType.OMNICORE));

		Packets.STREAMER.streamPacket(new PlayerLoginSucessPacket().setPlayername(player.getName())
				.useIPLogin(AccountHelper.hasTag(AccountTagType.IP_LOGIN, AccountBase.getTags(player))).build()
				.setReceiver(PacketSenderType.OMNICORD));

	}

}
