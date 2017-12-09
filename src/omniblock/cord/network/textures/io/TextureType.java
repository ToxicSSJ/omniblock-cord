package omniblock.cord.network.textures.io;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import omniblock.cord.util.lib.textures.ResourcePack;

public enum TextureType {

	OMNIBLOCK_DEFAULT(new ResourcePack(
			
			"OMNIBLOCKDEF",
			"http://omniblock.net/gameserver/DEFAULT.zip",
			"1798caf219d9eaaf7d33e61053a49a32988ae9eb",
			1,
			false,
			""
			
			)),
	
	SKYWARS_Z_PACK(new ResourcePack(
			
			"SKWZ",
			"http://omniblock.net/gameserver/SKWZvC1.zip",
			"d4a6b3cf9b72fe2d63adbf29fcb101f9f9c362f4",
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
		
		ResourcePackHandler.sendPacket(player, this);
		return;
		
	}
	
}
