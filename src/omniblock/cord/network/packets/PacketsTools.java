package omniblock.cord.network.packets;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.packet.PlayerSendBanPacket;
import net.omniblock.packets.network.structure.packet.ResposeAuthEvaluatePacket;
import omniblock.cord.OmniCord;
import omniblock.cord.network.core.io.MSGPatcher;
import omniblock.cord.util.TextUtil;

/**
 * 
 * Esta clase se encargará de disponer las clases
 * necesarias para el respectivo procesamiento de
 * paquetes o datos.
 * 
 * @author zlToxicNetherlz
 *
 */
public class PacketsTools {
	
	public static Map<String, String> SAVED_IP_AUTH = new HashMap<String, String>();

	public static Map<String, Integer> SOCKET_PORTS = new HashMap<String, Integer>();


	@SuppressWarnings("deprecation")
	public static void promptNetworkBooster2Player(String player, String key, String gametype, int duration){
		
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
		
		if(target == null) return;
		if(!target.isConnected()) return;
		
	}

	/**
	 * 
	 * Con este metodo podrémos evaluar el estado de un jugador ante
	 * el sistema de logeo. Básicamente le enviará información al servidor
	 * que pide la información como comprobante para hacerlo o no
	 * logear. (Este sistema solo es util para usuarios no premium)
	 * 
	 * @param player El nombre del jugador que se desea evaluar.
	 * @param iplogin Si el jugador a activado el logeo por IP en su config.
	 */
	public static void sendAuthEvaluate2Player(String player, boolean iplogin) {
		
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
		
		if(target != null){
			
			String ip = target.getAddress().getAddress().getHostAddress();
			ServerInfo server = target.getServer().getInfo();
			
			if(server != null){
				
				if(iplogin == true){
					
					if(SAVED_IP_AUTH.containsKey(player)){
						
						if(SAVED_IP_AUTH.get(player).equalsIgnoreCase(ip)){
							
							Packets.STREAMER.streamPacket(new ResposeAuthEvaluatePacket()
					    			.setPlayername(player)
					    			.setStatus("SUCESS")
					    			.build().setReceiver(SOCKET_PORTS.get(server.getName()))
					    			);
							return;
							
						}
						
					}
					
				}
				
				Packets.STREAMER.streamPacket(new ResposeAuthEvaluatePacket()
		    			.setPlayername(player)
		    			.setStatus("LOGIN")
		    			.build().setReceiver(SOCKET_PORTS.get(server.getName()))
		    			);
				return;
				
			}
			
		}
		
	}
	
	/**
	 * 
	 * Con este metodo se puede registrar un Auth, es decir
	 * se registrará o se guardará en un mapa el estado del usuario
	 * con el sistema de logeo, esto solo aplica para jugadores
	 * no premium.
	 * 
	 * @param player El jugador el cual ya coloco la contraseña bien y ha sido logeado.
	 * @param iplogin Si el jugador a activado el logeo por IP en su config.
	 */
	public static void registerAuthSucess2Player(String player, boolean iplogin) {
		
		if(iplogin == false) return;
		
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
		
		if(target != null){
			
			String ip = target.getAddress().getAddress().getHostAddress();
			
			if(SAVED_IP_AUTH.containsKey(player)) SAVED_IP_AUTH.remove(player);
			SAVED_IP_AUTH.put(player, ip);
			return;
			
		}
		
	}
	
	/**
	 * 
	 * Con este metodo se le puede hacer un kickeo a un jugador por medio
	 * de ciertos parametros obligatorios.
	 * 
	 * @param player Nombre del jugador al cual se le kickeara.
	 * @param moderator Nombre del moderador el cual es quien está kickeando a dicho jugador.
	 * @param reason Razón del porque se está haciendo el kickeo.
	 */
	@SuppressWarnings("deprecation")
	public static void sendKick2Player(String player, String moderator, String reason) {
		
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
		
		if(target != null) {
			if(target.isConnected()) {
				
				target.disconnect(MSGPatcher.YOURE_KICKED
						.replaceFirst("VAR_KICK_MOD", moderator)
						.replaceFirst("VAR_KICK_REASON", reason));
				
			}
		}
		
	}
	
	/**
	 * 
	 * Con este metodo se expulsarán a todos los jugadores con el mensaje predefinido
	 * de mantenimiento. Este metodo será efectivo para la creación de mantenimientos
	 * en base a paquetes externos de cualquier medio.
	 */
	@SuppressWarnings("deprecation")
	public static void sendMaintenance() {
		
		for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
			
			player.disconnect(MSGPatcher.MAINTENANCE_KICKED);
			continue;
			
		}
		
	}
	
	/**
	 * 
	 * Con este metodo se enviara una orden de efecto de baneo al jugador
	 * definido en el parametro. Este efecto de baneo provocará "rayos" en el
	 * servidor justo en la pocisión de ese jugador para hacerlo más visual y
	 * a parte expulsarlo del servidor.
	 * Es la unica función de este metodo, el que se encarga de verdaderamente
	 * banearlo es el OmniNetwork con las bases de datos.
	 * 
	 * @param player El jugador al cual se le enviará el efecto "ban".
	 */
	public static void sendBan2Player(String player) {
		
		ProxyServer.getInstance().getScheduler().schedule(OmniCord.getInstance(), new Runnable() {
			
            @SuppressWarnings("deprecation")
			@Override
            public void run() {
               
            	ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
        		
        		if(target != null) {
        			if(target.isConnected()) {
        				
        				ServerInfo server = target.getServer().getInfo();
        				
        				if(SOCKET_PORTS.containsKey(server.getName())){
        					
        					Integer PORT = SOCKET_PORTS.get(server.getName());
        					
        					Packets.STREAMER.streamPacket(new PlayerSendBanPacket()
    				    			.setPlayername(player)
    				    			.build().setReceiver(PORT)
    				    			);
        					
        					ProxyServer.getInstance().getScheduler().schedule(OmniCord.getInstance(), new Runnable() {
        						
        						@Override
        			            public void run() {
        			            	
        			            	if(target.isConnected()) target.disconnect(MSGPatcher.YOURE_BANNED_WITHOUT_VARS);
        			            	return;
        			            	
        			            }
        					}, 2, TimeUnit.SECONDS);
        					return;
        					
        				} else {
        					
        					target.disconnect(MSGPatcher.YOURE_BANNED_WITHOUT_VARS);
        					return;
        					
        				}
        				
        			}
        		}
            	
            }
            
        }, 250, TimeUnit.MILLISECONDS);
		
	}
	
	/**
	 * 
	 * Con este metodo se le enviará un mensaje general a 
	 * todos los jugadores que se encuentren en la network.
	 * <br><br>
	 * Vease también: {@link PacketsTools#sendMessage2Player(String, String)}
	 * 
	 * @param message El mensaje.
	 */
	public static void sendMessage2All(String message){
		
		for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
			
			sendMessage2Player(player.getName(), message);
			continue;
			
		}
		
	}
	
	/**
	 * 
	 * Con este metodo se le enviará un titulo general de
	 * minecraft a todos los jugadores que se encuentren
	 * en la network.
	 * <br><br>
	 * Vease también: {@link PacketsTools#sendTitle2Player(String, String, String)}
	 * 
	 * @param title La primera linea del titulo.
	 * @param subtitle La segunda linea del titulo.
	 */
	public static void sendTitle2All(String title, String subtitle){
		
		for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
			
			sendTitle2Player(player.getName(), title, subtitle);
			continue;
			
		}
		
	}
	
	/**
	 * 
	 * Con este metodo se le enviará un mensaje al jugador, Este mensaje
	 * pasa por un filtro el cual le dará atributos y colores al mensaje
	 * para que este presentable al jugador.
	 * <br><br>
	 * Vease también: {@link TextUtil#filter(String)}
	 * 
	 * @param player Jugador al cual se le enviará el mensaje.
	 * @param message El mensaje.
	 */
	@SuppressWarnings("deprecation")
	public static void sendMessage2Player(String player, String message) {
		
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
		
		if(target != null) {
			if(target.isConnected()) {
				
				message = TextUtil.filter(message);
				target.sendMessage(message);
				
			}
		}
		
	}
	
	/**
	 * 
	 * Con este metodo se le enviará un titulo general de
	 * minecraft al jugador especificado que se encuentren
	 * en la network.
	 * 
	 * @param player El jugador al cual se le enviará el titulo.
	 * @param title La primera linea del titulo.
	 * @param subtitle La segunda linea del titulo.
	 */
	public static void sendTitle2Player(String player, String title, String subtitle) {
		
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
		
		if(target != null) {
			if(target.isConnected()) {
				
				ProxyServer.getInstance().createTitle()
					.title(new ComponentBuilder(TextUtil.format(title)).create())
					.subTitle(new ComponentBuilder(TextUtil.format(subtitle)).create())
					.fadeIn(20)
				    .stay(100)
				    .fadeOut(20)
				    .send(target);
				return;
				
			}
		}
		
	}
	
}
