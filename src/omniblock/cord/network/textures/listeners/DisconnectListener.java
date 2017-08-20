package omniblock.cord.network.textures.listeners;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import omniblock.cord.network.packets.PacketsTools;
import omniblock.cord.network.textures.io.TextureType;

public class DisconnectListener implements Listener {

	@EventHandler
	public void onLeft(PlayerDisconnectEvent e){
		
		if(PacketsTools.SAVED_TEXTURES.containsKey(e.getPlayer().getName())){
			
			if(PacketsTools.SAVED_TEXTURES.get(e.getPlayer().getName()) != TextureType.OMNIBLOCK_DEFAULT){
				TextureType.OMNIBLOCK_DEFAULT.sendPack(e.getPlayer());
			}
			
			PacketsTools.SAVED_TEXTURES.remove(e.getPlayer().getName());
			return;
			
		}
		
	}
	
}
