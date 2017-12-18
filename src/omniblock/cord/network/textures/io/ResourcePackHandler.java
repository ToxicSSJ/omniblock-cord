package omniblock.cord.network.textures.io;

import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import omniblock.cord.OmniCord;
import omniblock.cord.network.textures.io.listener.PlayerJoinListener;
import omniblock.cord.network.textures.io.listener.PlayerQuitListener;

public class ResourcePackHandler {

	public static void registerListeners() {
		
		ProxyServer.getInstance().getPluginManager().registerListener(OmniCord.getPlugin(), new PlayerJoinListener());
		ProxyServer.getInstance().getPluginManager().registerListener(OmniCord.getPlugin(), new PlayerQuitListener());
		
	}
	
	public static void sendPacket(ProxiedPlayer player, TextureType type) {
		
		ProxyServer.getInstance().getScheduler().schedule(OmniCord.getInstance(), new Runnable() {
			
		    public void run() {
		    	
		    	ProxyServer.getInstance().getPluginManager().dispatchCommand(
						ProxyServer.getInstance().getConsole(),
						"gen_command_usepack " + type.getPack().getName().toLowerCase() + " " + player.getName());
		    	
		    }
		    
		}, 3, TimeUnit.SECONDS);
		
		
		return;
		
	}
	
}
