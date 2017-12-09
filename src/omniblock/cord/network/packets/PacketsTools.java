package omniblock.cord.network.packets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.packet.PlayerSendBanPacket;
import net.omniblock.packets.network.structure.packet.ResposeAuthEvaluatePacket;
import net.omniblock.packets.network.structure.packet.ResposeBoostedGamesPacket;
import net.omniblock.packets.network.structure.packet.ResposeGamePartyInfoPacket;
import net.omniblock.packets.network.structure.packet.ResposePlayerNetworkBoosterPacket;
import omniblock.cord.OmniCord;
import omniblock.cord.addons.network.PARTYManager.PartyUtils;
import omniblock.cord.network.core.io.MSGPatcher;
import omniblock.cord.network.textures.io.TextureType;
import omniblock.cord.util.TextUtil;
import omniblock.cord.util.lib.minecraft.bungee.BoosterTask;

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
	public static Map<String, TextureType> SAVED_TEXTURES = new HashMap<String, TextureType>();
	
	public static Map<String, BoosterTask> NETWORK_BOOSTERS = new HashMap<String, BoosterTask>();
	public static Map<String, Integer> SOCKET_PORTS = new HashMap<String, Integer>();
	
	static {
		
		NETWORK_BOOSTERS.put("Skywars", null);
		NETWORK_BOOSTERS.put("Arcade", null);
		
	}
	
	/**
	 * 
	 * Con este metodo se enviara un jugador al servidor especificado.
	 * 
	 * @param player El nombre del jugador al cual deseas enviar al servidor.
	 * @param servername El nombre del servidor a donde enviarás el jugador.
	 * @param party Si es necesario que se teletransporte su party.
	 * @param requestuuid La UUID del paquete request.
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
					
					ProxyServer.getInstance().getScheduler().schedule(OmniCord.getInstance(), new Runnable() {
						
			            @SuppressWarnings("serial")
						@Override
			            public void run() {
			            	
			            	List<String> players_names = new ArrayList<String>(){
			            		
			            		{
			            			
			            			players.stream().forEach(player ->
		            				add(player.getName()));
			            			
			            		}
			            		
			            	};
			            	
			            	Packets.STREAMER.streamPacket(new ResposeGamePartyInfoPacket()
			            			.setPlayers(players_names)
			            			.build().setReceiver(SOCKET_PORTS.get(server.getName()))
			            			);
			            	
			            }
			            
			        }, 1, TimeUnit.SECONDS);
					
					return;
					
				}
				
			}
		}
		
	}
	
	public static void promptNetworkBooster2Player(String player, String key, String gametype, int duration){
		
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
		
		if(target == null) return;
		if(!target.isConnected()) return;
		
		if(NETWORK_BOOSTERS.containsKey(gametype)){
			
			if(NETWORK_BOOSTERS.get(gametype) == null){
				
				NETWORK_BOOSTERS.put(gametype, new BoosterTask(player, gametype, duration));
				
				sendMessage2All(
						"&r[br]"
					  +	"[center]&9&l¡" + player + " ha activado un booster global!" + "[/center][br]"
					  + "&8&m-&r &7Recibirás bonificaciones adicionales por cada partida que juegues en la modalidad"
					  + "&7de &d&l" + gametype.toUpperCase() + "&7 donde se activó el booster![br]"
						);
				
				sendTitle2All("&9&lNETWORK BOOSTER ACTIVADO", "&4&k||&r &bJugador: &7" + player + " &bModalidad: &7" + gametype + " &4&k||&r");
				
				Packets.STREAMER.streamPacket(new ResposePlayerNetworkBoosterPacket()
            			.setBoosterkey(key)
            			.setGametype(gametype)
            			.setPlayername(player)
            			.build().setReceiver(SOCKET_PORTS.get(target.getServer().getInfo().getName()))
            			);
				
				return;
				
			} else {
				
				sendMessage2Player(player, "&8&lB&8oosters &c&l» &7Lo sentimos, Ya existe un booster activado en esta modalidad.");
				return;
				
			}
			
		}
		
	}
	
	/**
	 * 
	 * Con este metodo se enviará información acerca de los boosters
	 * de los juegos a el servidor argumentado.
	 * 
	 * @param servername El servidor al cual se le enviará la información.
	 */
	public static void sendBoostedGames2Server(String servername) {
		
		List<String> boostedgames = new ArrayList<String>();
		
		NETWORK_BOOSTERS.entrySet().stream().forEach(entry -> {
			
			if(entry.getValue() != null){
				
				boostedgames.add(entry.getKey() + "#" + entry.getValue().getPlayer());
				
			}
			
		});
		
		Packets.STREAMER.streamPacket(new ResposeBoostedGamesPacket()
    			.setBoostedGames(boostedgames)
    			.build().setReceiver(SOCKET_PORTS.get(servername))
    			);
		
	}
	
	/**
	 * 
	 * Con este metodo se puede recibir el nombre del paquete
	 * de texturas que posee un jugador en base al nombre de
	 * dicho jugador.
	 * 
	 * @param playername El nombre del jugador.
	 * @return El nombre del resourcepack, si no se encuentra
	 * al jugador retornará el paquete de texturas 'OMNIBLOCK_DEFAULT'.
	 */
	public static String getTexturehash4Player(String playername){
		
		Entry<String, TextureType> entry = SAVED_TEXTURES.entrySet().stream().filter(k -> k.getKey() == playername).findAny().orElse(null);
		
		if(entry == null) return TextureType.OMNIBLOCK_DEFAULT.getPack().getName();
		else return entry.getValue().getPack().getName();
		
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
							
							type.sendPack(target);
							SAVED_TEXTURES.put(player, type);
							return;
							
						} else {
							
							if(type != TextureType.OMNIBLOCK_DEFAULT) type.sendPack(target);
							SAVED_TEXTURES.put(player, type);
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
