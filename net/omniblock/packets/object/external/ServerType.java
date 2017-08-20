package net.omniblock.packets.object.external;

public enum ServerType {

	MAIN_AUTH_SERVER("AUTH"),
	MAIN_LOBBY_SERVER("LOBBY"),
	
	SURVIVAL("SURVIVAL"),
	
	SKYWARS_LOBBY_SERVER("SKWLB"),
	SKYWARS_GAME_SERVER("SKWGS"),
	
	;
	
	private String servertypekey;
	
	ServerType(String servertypekey){
		this.servertypekey = servertypekey;
	}

	public String getServertypekey() {
		return servertypekey;
	}
	
}
