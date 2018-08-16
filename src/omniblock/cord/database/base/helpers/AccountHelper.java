package omniblock.cord.database.base.helpers;

public class AccountHelper {

	public static boolean hasTag(AccountTagType tag, String tags) {

		return tags.contains(tag.getKey().toLowerCase());

	}
	
	public enum AccountBoosterType {

		NETWORK_BOOSTER,
		PERSONAL_BOOSTER,

		;

	}

	public enum AccountTagType {

		IP_LOGIN("iplogin"),
		PRIVATE_MSG("privatemsg"),
		TEXTURE_SOUND("texturesound"),
		FRIEND_REQUEST("friendrequest"),
		SEE_PLAYERS("seeplayers"),
		
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
