package omniblock.cord.network.textures.io.listener;

import de.themoep.resourcepacksplugin.bungee.BungeeResourcepacks;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import omniblock.cord.network.packets.PacketsTools;
import omniblock.cord.network.textures.io.TextureType;

public class PlayerQuitListener implements Listener {

	@EventHandler
	public void onPlayerDisconnect(PlayerDisconnectEvent event) {
		
		if(PacketsTools.SAVED_TEXTURES.containsKey(event.getPlayer().getName())) { 
			if(TextureType.OMNIBLOCK_DEFAULT == PacketsTools.SAVED_TEXTURES.get(event.getPlayer().getName())) {
				PacketsTools.SAVED_TEXTURES.remove(event.getPlayer().getName());
				return;
			}
		}
		
		BungeeResourcepacks.getInstance().getUserManager().setUserPack(event.getPlayer().getUniqueId(), TextureType.OMNIBLOCK_DEFAULT.getPack());
		PacketsTools.SAVED_TEXTURES.remove(event.getPlayer().getName());
		TextureType.OMNIBLOCK_DEFAULT.sendPack(event.getPlayer());
		return;
		
	}
	
}
