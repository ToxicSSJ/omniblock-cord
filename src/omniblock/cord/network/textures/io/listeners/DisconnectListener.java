package omniblock.cord.network.textures.io.listeners;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import omniblock.cord.network.textures.io.BungeeResourcepacks;
import omniblock.cord.network.textures.io.TextureType;

/**
 * Created by Phoenix616 on 14.05.2015.
 */
public class DisconnectListener implements Listener {

    @SuppressWarnings("unused")
	private BungeeResourcepacks plugin;

    public DisconnectListener(BungeeResourcepacks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        TextureType.OMNIBLOCK_DEFAULT.sendPack(event.getPlayer());
    }
}
