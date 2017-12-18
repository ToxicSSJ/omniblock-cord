package omniblock.cord.network.textures.io;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import de.themoep.resourcepacksplugin.core.ResourcePack;

public enum TextureType {

	OMNIBLOCK_DEFAULT(new ResourcePack(
			
			"omniblockdef",
			"http://omniblock.net/gameserver/DEFAULT.zip",
			"f5f1ae1188a4b04ddea8ce3967a4de4fd5fdd757",
			0,
			false,
			""
			
			)),
	
	SKYWARS_Z_PACK(new ResourcePack(
			
			"skwz",
			"http://omniblock.net/gameserver/SKWZvC1.zip",
			"1798caf219d9eaaf7d33e61053a49a32988ae9eb",
			0,
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
