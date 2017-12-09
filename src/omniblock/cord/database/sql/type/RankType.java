package omniblock.cord.database.sql.type;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import omniblock.cord.util.TextUtil;

public enum RankType {

	USER(0, false, false, "Usuario", ""),

	GOLEM(1, true, false, "Golem", TextUtil.format("&8(&eGolem&8)")),
	TITAN(2, true, false, "Titan", TextUtil.format("&8(&6Titan&8)")),

	HELPER(3, true, true, "Ayudante", TextUtil.format("&8(&aAY&8)")),
	MOD(4, true, true, "Moderador", TextUtil.format("&8(&cMod&8)")),

	BNF(5, true, true, "Benefactor", TextUtil.format("&8(&bBNF&8)")),
	GM(6, true, true, "Game Master", TextUtil.format("&8(&9GM&8)")),

	ADMIN(7, true, true, "Administrador", TextUtil.format("&8(&4Admin&8)")),
	CEO(8, true, true, "CEO", TextUtil.format("&8(&dCEO&8)")),

	;

	public static final RankType defrank = RankType.USER;

	private int id;

	private String name;
	private String prefixname;

	private boolean staff = false;
	private boolean prefix = false;

	RankType(int id, boolean hasprefix, boolean isstaff, String name, String prefix) {

		this.id = id;

		this.name = name;
		this.prefixname = prefix;

		this.prefix = hasprefix;
		this.staff = isstaff;

	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCustomName(ProxiedPlayer player) {
		if (getPrefix() == null) return TextUtil.format("&7" + player.getName());
		if (getPrefix().equals("") || getPrefix() == "") return TextUtil.format("&7" + player.getName());
		return TextUtil.format(getPrefix() + " &7" + player.getName());
	}

	public String getCustomName(ProxiedPlayer player, char color) {
		if (getPrefix() == null) return TextUtil.format("&" + color + player.getName());
		if (getPrefix().equals("") || getPrefix() == "") return TextUtil.format("&" + color + player.getName());
		return TextUtil.format(getPrefix() + " &" + color + player.getName());
	}

	public static RankType getByName(String rankName) {

		for (RankType type: RankType.values()) {

			if (type.getName().equalsIgnoreCase(rankName)) return type;

		}

		return RankType.USER;

	}

	public static boolean exists(String rankName) {

		for (RankType type: RankType.values()) {

			if (type.getName().equalsIgnoreCase(rankName)) return true;

		}

		return false;

	}

	public String getPrefix() {
		return prefixname;
	}

	public boolean hasPrefix() {
		return prefix;
	}

	public boolean isStaff() {
		return staff;
	}

}