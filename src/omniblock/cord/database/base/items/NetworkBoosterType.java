package omniblock.cord.database.base.items;

import java.util.Date;

public enum NetworkBoosterType {
	OMNICOINS_NETWORK_BOOSTER_2HOURS("onetworkboost2h", "Network Booster (2H)", 7200), OMNICOINS_NETWORK_BOOSTER_6HOURS(
			"onetworkboost6h", "Network Booster (6H)", 21600), OMNICOINS_NETWORK_BOOSTER_12HOURS("onetworkboost12h",
					"Network Booster (12H)",
					43200), OMNICOINS_NETWORK_BOOSTER_1DAY("onetworkboost1d", "Network Booster (1D)", 86400),

	;

	private String key;
	private String name;
	private int seconds;

	NetworkBoosterType(String key, String name, int seconds) {
		this.key = key;
		this.name = name;
		this.seconds = seconds;
	}

	public Date getEndDate() {

		Date end = new Date();
		end.setTime(end.getTime() + (seconds * 1000));
		return end;

	}

	public int getSeconds() {
		return seconds;
	}

	public String getName() {
		return name;
	}

	public String getKey() {
		return key;
	}

	public static NetworkBoosterType fromKey(String key) {

		for (NetworkBoosterType booster : NetworkBoosterType.values()) {
			if (booster.getKey().equalsIgnoreCase(key)) {
				return booster;
			}
		}

		return null;

	}

}
