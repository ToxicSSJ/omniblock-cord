package omniblock.cord.addons.motd;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import omniblock.cord.OmniCord;
import omniblock.cord.addons.motd.type.MOTDType;
import omniblock.cord.util.TextUtil;

/**
 * 
 * Con esta clase se maneja el sistema
 * del MOTD del servidor cuando un jugador
 * pingea.
 * 
 * @author zlToxicNetherlz
 *
 */
public class MOTDManager implements Listener {

	public static MOTDType motd = MOTDType.COMMON_MOTD;
	
	/**
	 * 
	 * Con este metodo se inicializa el sistema registrando
	 * los eventos de la clase.
	 * 
	 */
	public static void start() {
		
		ProxyServer.getInstance().getPluginManager().registerListener(OmniCord.getInstance(), new MOTDManager());
		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
    public void onPing(ProxyPingEvent e){
		
        ServerPing serverPing = e.getResponse();
        serverPing.setDescription(TextUtil.format(motd.getPreset().getLine(1) + "" + motd.getPreset().getLine(2)));
        e.setResponse(serverPing);
        
    }
	
}
