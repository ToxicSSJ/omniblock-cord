package omniblock.cord.util.lib.omnicore;

public enum ServerType {

	MAIN_LOBBY_SERVER("LOBBY"),
	
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
