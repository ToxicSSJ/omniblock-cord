package omniblock.cord.addons.resolver;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import omniblock.cord.OmniCord;

/**
 * @author wirlie
 *
 */
public class ResolverListener implements Listener {

	public static void setup() {
		ProxyServer.getInstance().getPluginManager().registerListener(OmniCord.getInstance(), new ResolverListener());
	}
	
	@EventHandler
	public void onPlayerNetworkJoin(PostLoginEvent e) {
		ProxiedPlayer player = e.getPlayer();
		PendingConnection connection = player.getPendingConnection();
		
		
	}

}
