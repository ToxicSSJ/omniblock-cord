package omniblock.cord.network.core.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import omniblock.cord.OmniCord;
import omniblock.cord.addons.network.PARTYManager.PartyUtils;
import omniblock.cord.network.core.io.MSGPatcher;
import omniblock.cord.network.socket.Socket;
import omniblock.cord.network.textures.io.TextureType;
import omniblock.cord.util.TextUtil;
import omniblock.cord.util.lib.omnicore.MessageType;

/**
 * 
 * Esta clase se encargará de disponer las clases
 * necesarias para el respectivo procesamiento de
 * paquetes o datos.
 * 
 * @author zlToxicNetherlz
 *
 */
public class PacketProcessorHandler {
	
	public static Map<String, String> SAVED_IP_AUTH = new HashMap<String, String>();
	public static Map<String, TextureType> SAVED_TEXTURES = new HashMap<String, TextureType>();
	
	public static Map<String, Integer> SOCKET_PORTS = new HashMap<String, Integer>();
	
	/**
	 * 
	 * Con este metodo se enviara un jugador al servidor especificado.
	 * 
	 * @param player El nombre del jugador al cual deseas enviar al servidor.
	 * @param servername El nombre del servidor a donde enviarás el jugador.
	 * @param party Si es necesario que se teletransporte su party.
	 */
	public static void sendPlayer2Server(String player, String servername, boolean party) {
		
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
		ServerInfo server = ProxyServer.getInstance().getServerInfo(servername);
		
		if(target != null && server != null) {
			if(target.isConnected() && !server.getAddress().isUnresolved()) {
				
				if(party == false){
					
					target.connect(server);
					return;
					
				} else {
					
					List<ProxiedPlayer> players = new ArrayList<ProxiedPlayer>();
					
					if(PartyUtils.isOwner(target)){
						
						for(String k : PartyUtils.getPartyMembers(target)){
							
							ProxiedPlayer member = PartyUtils.getPlayer(k);
							if(player != null){
								
								players.add(member);
								member.connect(server);
								continue;
								
							}
							
						}
						
					}
					
					players.add(target);
					target.connect(server);
					
					if(players.size() <= 1) return;
					
					OmniCord.getInstance().getProxy().getScheduler().schedule(OmniCord.getPlugin(), new Runnable() {
						
			            @SuppressWarnings("serial")
						@Override
			            public void run() {
			                
			            	PacketProcessorHandler.sendData2Server(
			            		
			            	    StringUtils.join(",", new ArrayList<String>(){
			            			{
			            				players.stream().forEach(player ->
			            				add(player.getName()));
			            			}
			            		}),	
			            			
			            		MessageType.GAME_PARTY_INFO.getKey(),
			            		
			            		SOCKET_PORTS.get(server.getName())
			            		
			            	);
			            	
			            }
			            
			        }, 1, TimeUnit.SECONDS);
					
					return;
					
				}
				
			}
		}
		
	}
	
	/**
	 * 
	 * Con este metodo podrémos evaluar el estado de un jugador ante
	 * el sistema de logeo. Básicamente le enviará información al servidor
	 * que pide la información como comprobante para hacerlo o no
	 * logear. (Este sistema solo es util para usuarios no premium)
	 * 
	 * @param player El nombre del jugador que se desea evaluar.
	 */
	public static void sendAuthEvaluate2Player(String player) {
		
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
		
		if(target != null){
			
			String ip = target.getAddress().getAddress().getHostAddress();
			ServerInfo server = target.getServer().getInfo();
			
			if(server != null){
				
				if(SAVED_IP_AUTH.containsKey(player)){
					
					if(SAVED_IP_AUTH.get(player).equalsIgnoreCase(ip)){
						
						sendData2Server("AUTH", "SUCESS", SOCKET_PORTS.get(server.getName()));
						return;
						
					}
					
				}
				
				sendData2Server("AUTH", "LOGIN", SOCKET_PORTS.get(server.getName()));
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
	 */
	public static void registerAuthSucess2Player(String player) {
		
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
	 * Con este metodo se le podrá dar un pack de texturas al jugador con
	 * el sistema que envia el paquete automaticamente desde el OmniCord
	 * al jugador por medio de envio de paquetes en la conexión.
	 * El sistema cuenta con reconocedor automatico y además con el sistema
	 * de remover el paquete.
	 * 
	 * @param player Nombre del jugador al cual se le enviará el pack de texturas.
	 * @param name Nombre del pack que se le enviará al jugador (ID).
	 */
	public static void sendPack2Player(String player, String name) {
		
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
		
		if(target != null) {
			if(target.isConnected()) {
				
				for(TextureType type : TextureType.values()){
					
					if(type.getPack().getName().equalsIgnoreCase(name)){
						
						if(SAVED_TEXTURES.containsKey(player)){
							
							if(SAVED_TEXTURES.get(player) == type){
								return;
							}
							
							SAVED_TEXTURES.put(player, type);
							type.sendPack(target);
							return;
							
						} else {
							
							SAVED_TEXTURES.put(player, type);
							if(type != TextureType.OMNIBLOCK_DEFAULT) type.sendPack(target);
							return;
							
						}
						
					}
					
				}
				
			}
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
	 * Con este metodo se enviará información al servidor de la data
	 * recolectada por el OmniCore sobre los lobbies disponibles en
	 * cierta modalidad. Este metodo es en respuesta al pedido que
	 * genera un jugador por medio del servidor y es con el fin de
	 * recibir el listado y ser representado por medio de una
	 * GUI interactiva.
	 * 
	 * @param player El jugador al cual se le enviarán las lobbies.
	 * @param data La data de las lobbies que se le enviarán al jugador.
	 */
	public static void sendLobbies2Player(String player, String data) {
		
		if(data.contains("*")){
			
			ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
			
			if(target != null) {
				if(target.isConnected()) {
					
					if(SOCKET_PORTS.containsKey(target.getServer().getInfo().getName())){
						
						sendData2Server(player + "#" + data, MessageType.REQUEST_PLAYER_GAME_LOBBY_SERVERS.getKey(), SOCKET_PORTS.get(target.getServer().getInfo().getName()));
						return;
						
					}
					
				}
			}
			
		}
		
		sendMessage2Player(player, "&cEl sistema de lobbies está deshabilitado actualmente.");
		return;
		
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
		
		OmniCord.getInstance().getProxy().getScheduler().schedule(OmniCord.getPlugin(), new Runnable() {
			
            @SuppressWarnings("deprecation")
			@Override
            public void run() {
               
            	ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
        		
        		if(target != null) {
        			if(target.isConnected()) {
        				
        				ServerInfo server = target.getServer().getInfo();
        				
        				if(SOCKET_PORTS.containsKey(server.getName())){
        					
        					Integer PORT = SOCKET_PORTS.get(server.getName());
        					sendData2Server(target.getName(), MessageType.PLAYER_SEND_BAN.getKey(), PORT);
        					
        					OmniCord.getInstance().getProxy().getScheduler().schedule(OmniCord.getPlugin(), new Runnable() {
        						
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
	 * Con este metodo se envia data neta al servidor, cualquier
	 * tipo de data, luego dicha data será procesada por el OmniNetwork
	 * del servidor correspondiente al cual se le envio la data para efectuar
	 * las acciones.
	 * 
	 * El puerto es especificado por el registro en el mapa 
	 * donde en base al servername (nombre del servidor) se podrá
	 * obtener el puerto de dicho servidor y poder enviar la data.
	 * 
	 * @param type Es el identificador de la data que será procesada (ID).
	 * @param data Es la data neta que se enviará al servidor.
	 * @param port Es el puerto al cual se le enviará la información.
	 * 
	 * @see PacketProcessorHandler.SOCKET_PORTS
	 */
	public static void sendData2Server(String type, String data, int port) {
		
		if(type == null || data == null) return;
		
		String packet = StringUtils.join(new String[] { type, data }, "@");
		Socket.ADAPTER.sendData(packet, port);
		return;
		
	}
	
}
