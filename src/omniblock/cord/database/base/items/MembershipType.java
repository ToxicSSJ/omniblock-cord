package omniblock.cord.database.base.items;

import java.util.AbstractMap;
import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import omniblock.cord.database.base.RankBase;
import omniblock.cord.database.sql.type.RankType;

public enum MembershipType {
	
	OMNICOINS_NETWORK_MEMBERSHIP_GOLEM_1MONTH("ongolembundle1m", "Rango Golem (1MES)", RankType.GOLEM, 31),
	OMNICOINS_NETWORK_MEMBERSHIP_TITAN_1MONTH("ontitanbundle1m", "Rango Titan (1MES)", RankType.TITAN, 31),
	OMNICOINS_NETWORK_MEMBERSHIP_GOLEM_3MONTHS("ongolembundle3m", "Rango Golem (3MESES)", RankType.GOLEM, 93),
	OMNICOINS_NETWORK_MEMBERSHIP_TITAN_3MONTHS("ontitanbundle3m", "Rango Titan (3MESES)", RankType.TITAN, 93),

	;

	private String key;
	private String name;
	private RankType rank;

	private int days;

	MembershipType(String key, String name, RankType rank, int days) {

		this.key = key;
		this.name = name;
		this.rank = rank;

		this.days = days;

	}

	public RankType getRank() {
		return rank;
	}

	public int getDays() {
		return days;
	}

	public String getName() {
		return name;
	}

	public String getKey() {
		return key;
	}

	public Date getEndDate() {

		Date end = new Date();
		end.setTime(end.getTime() + TimeUnit.DAYS.toMillis(days));
		return end;

	}

	public static Entry < MembershipType,
	Date > getSeparatedInfo(String row) {

		if (row.contains("#")) {

			String[] container = row.split("#");

			String key = container[0];
			String expiredate = container[1];

			return new AbstractMap.SimpleEntry < MembershipType,
			Date > (MembershipType.fromKey(key), RankBase.parseExpireDate(expiredate));

		}

		return null;

	}

	public static String buildSeparatedInfo(String key, Date expiredate) {

		return key + "#" + RankBase.parseExpireDate(expiredate);

	}

	public static MembershipType fromKey(String key) {

		for (MembershipType membership: MembershipType.values()) {
			if (membership.getKey().equalsIgnoreCase(key)) {
				return membership;
			}
		}

		return null;

	}

}