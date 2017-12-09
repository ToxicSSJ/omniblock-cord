package omniblock.cord.database.base.items;

import java.util.Date;

public enum BoosterType {
	OMNICOINS_BOOSTER_6HOURS("oboost6h", "Pequeño Booster (6Horas)", 21600), OMNICOINS_BOOSTER_3DAYS("oboost3d",
			"Gran Booster (3Dias)", 259200), OMNICOINS_BOOSTER_10DAYS("oboost10d", "Mega Booster (10Dias)",
					864000), OMNICOINS_BOOSTER_30DAYS("oboost30d", "Premium Booster (30Dias)", 2592000),

	;

	private String key;
	private String name;
	private int seconds;

	BoosterType(String key, String name, int seconds) {
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

	public static BoosterType fromKey(String key) {

		for (BoosterType booster : BoosterType.values()) {

			if (booster.getKey().equalsIgnoreCase(key)) {
				return booster;
			}
		}

		return null;

	}

}
