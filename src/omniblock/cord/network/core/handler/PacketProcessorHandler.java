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

public class PacketProcessorHandler {
	
	public static Map<String, TextureType> SAVED_TEXTURES = new HashMap<String, TextureType>();
	public static Map<String, Integer> SOCKET_PORTS = new HashMap<String, Integer>();
	
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
	
	public static void sendData2Server(String type, String data, int port) {
		
		if(type == null || data == null) return;
		
		String packet = StringUtils.join(new String[] { type, data }, "@");
		Socket.ADAPTER.sendData(packet, port);
		return;
		
	}
	
}
