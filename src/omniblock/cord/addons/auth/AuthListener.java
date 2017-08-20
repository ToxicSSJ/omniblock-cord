package omniblock.cord.addons.auth;

import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import omniblock.cord.network.packets.PacketsTools;

public class AuthListener implements Listener {

	@EventHandler
	public void onPlayerNetworkJoin(PostLoginEvent e) {
		
		ProxiedPlayer player = e.getPlayer();
		PendingConnection connection = player.getPendingConnection();
		boolean premium = connection.isOnlineMode();
		
		if(!premium){
			
			if(PacketsTools.SAVED_IP_AUTH.containsKey(player.getName())){
				
				String ip = PacketsTools.SAVED_IP_AUTH.get(player.getName());
				if(ip == connection.getAddress().getAddress().getHostName()) return;
				
			}
			
			PacketsTools.sendPlayer2Server(player.getName(), "AUTH", false);
			return;
			
		}
		
	}
	
}