package omniblock.cord.network.textures.io;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import omniblock.cord.network.textures.BungeeResourcepacks;
import omniblock.cord.util.lib.textures.ResourcePack;

public enum TextureType {

	OMNIBLOCK_DEFAULT(new ResourcePack(
			
			"OMNIBLOCKDEF",
			"http://download1645.mediafire.com/kjocn4144whg/45r35w628sfid24/DEFAULT.zip",
			"1798caf219d9eaaf7d33e61053a49a32988ae9eb",
			1,
			false,
			""
			
			)),
	
	SKYWARS_Z_PACK(new ResourcePack(
			
			"SKWZ",
			"http://download1595.mediafire.com/85mv8upvkkyg/gl524rvbwza8ih7/SKWZK8.zip",
			"b73fe8d443a0ff11fff36991689abd460b29d1d",
			1,
			false,
			""
			
			));
	
	;
	
	private ResourcePack pack;
	
	TextureType(ResourcePack pack){
		this.pack = pack;
	}

	public ResourcePack getPack() {
		return pack;
	}
	
	public void sendPack(ProxiedPlayer player){
		
		BungeeResourcepacks.sendPack(player, pack);
		
	}
	
}
