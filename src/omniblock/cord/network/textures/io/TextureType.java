package omniblock.cord.network.textures.io;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import omniblock.cord.network.textures.BungeeResourcepacks;
import omniblock.cord.util.lib.textures.ResourcePack;

public enum TextureType {

	OMNIBLOCK_DEFAULT(new ResourcePack(
			
			"OMNIBLOCKDEF",
			"http://api.omniblock.net/network/textures/DEFAULT.zip",
			"omniblockdef",
			1,
			false,
			""
			
			)),
	
	SKYWARS_Z_PACK(new ResourcePack(
			
			"SKWZ",
			"http://api.omniblock.net/network/textures/SKWZK8.zip",
			"skwzpackvk8",
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
