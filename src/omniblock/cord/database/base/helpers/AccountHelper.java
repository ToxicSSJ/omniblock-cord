package omniblock.cord.database.base.helpers;

public class AccountHelper {

	public static boolean hasTag(AccountTagType tag, String tags) {

		if (tags.contains(tag.getKey().toLowerCase()))
			return true;
		return false;

	}
	
	public static enum AccountBoosterType {

		NETWORK_BOOSTER, PERSONAL_BOOSTER,

		;

	}

	public static enum AccountTagType {

		IP_LOGIN("iplogin"), PRIVATE_MSG("privatemsg"), TEXTURE_SOUND("texturesound"), FRIEND_REQUEST("friendrequest"),

		;

		private String key;

		AccountTagType(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}

	}
	
}
