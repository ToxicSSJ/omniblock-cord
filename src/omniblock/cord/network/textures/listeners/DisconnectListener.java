package omniblock.cord.network.textures.listeners;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import omniblock.cord.network.core.handler.PacketProcessorHandler;
import omniblock.cord.network.textures.io.TextureType;

public class DisconnectListener implements Listener {

	@EventHandler
	public void onLeft(PlayerDisconnectEvent e){
		
		if(PacketProcessorHandler.SAVED_TEXTURES.containsKey(e.getPlayer().getName())){
			
			if(PacketProcessorHandler.SAVED_TEXTURES.get(e.getPlayer().getName()) != TextureType.OMNIBLOCK_DEFAULT){
				TextureType.OMNIBLOCK_DEFAULT.sendPack(e.getPlayer());
			}
			
			PacketProcessorHandler.SAVED_TEXTURES.remove(e.getPlayer().getName());
			return;
			
		}
		
	}
	
}
