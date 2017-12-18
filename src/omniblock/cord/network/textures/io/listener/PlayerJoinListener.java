package omniblock.cord.network.textures.io.listener;

import de.themoep.resourcepacksplugin.bungee.BungeeResourcepacks;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import omniblock.cord.network.packets.PacketsTools;
import omniblock.cord.network.textures.io.TextureType;

public class PlayerJoinListener implements Listener {

	@EventHandler
	public void onPlayerConnect(PostLoginEvent event) {
		
		BungeeResourcepacks.getInstance().getUserManager().setUserPack(event.getPlayer().getUniqueId(), TextureType.OMNIBLOCK_DEFAULT.getPack());
		PacketsTools.SAVED_TEXTURES.put(event.getPlayer().getName(), TextureType.OMNIBLOCK_DEFAULT);
		return;
		
	}
	
}
