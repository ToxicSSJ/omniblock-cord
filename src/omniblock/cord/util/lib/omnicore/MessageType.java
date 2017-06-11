package omniblock.cord.util.lib.omnicore;

public enum MessageType {

	WELCOME_PROXY("WELCOME"),
	BYE_PROXY("BYE"),
	
	REQUEST_PLAYER_GAME_LOBBY_SERVERS("RPGLS"),
	
	SERVER_SOCKET_INFO("SSOCK"),
	SERVER_REMOVE_INFO("SSSI"),
	SERVER_RELOAD_INFO("SRI"),
	
	PLAYER_SEND_TO_SERVER("PSTS"),
	PLAYER_SEND_TO_GAME("PSTG"),
	PLAYER_SEND_MESSAGE("PSMSG"),
	PLAYER_SEND_TEXTUREPACK("PSTP"),
	
	PLAYER_LOGIN_EVALUATE("PLE"),
	PLAYER_LOGIN_SUCESS("PLS"),
	
	PLAYER_SEND_KICK("PSKICK"),
	PLAYER_SEND_BAN("PSBAN"),
	
	GAME_LOCK_STATUS("GLS"),
	SERVER_FULL_STATUS("SFS"),
	
	GAME_ONLINE_INFO("GSPSI"),
	GAME_PARTY_INFO("GPI"),
	
	INITIALIZE_GAME("IG"),
	
	;
	
	private String key;
	
	MessageType(String key){
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}
