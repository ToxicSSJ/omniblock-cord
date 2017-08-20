package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;
import net.omniblock.packets.object.external.ServerType;

public class RequestPlayerGameLobbyServersPacket extends MessagePacket {

	private static final long serialVersionUID = 3719625733430875418L;
	
	protected String playername;
	protected String servertype;
	
	public RequestPlayerGameLobbyServersPacket() {
		super(PacketType.REQUEST_PLAYER_GAME_LOBBY_SERVERS);
	}
	
	public RequestPlayerGameLobbyServersPacket setPlayername(String playername){
		
		this.playername = playername;
		return this;
		
	}
	
	public RequestPlayerGameLobbyServersPacket setServertype(ServerType servertype){
		
		this.servertype = servertype.toString();
		return this;
		
	}
	
	@Override
	public PacketSocketData<RequestPlayerGameLobbyServersPacket> build(){
		
		for(Field f : this.getClass().getDeclaredFields()){
			
			f.setAccessible(true);
			
			try {
				
				if(f.get(this) == null){
					
					throw new UnsupportedOperationException("El campo '" + f.getName() + "' no ha sido definido en el paquete " + this.getClass().getName());
					
				}
				
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			
			continue;
			
		}
		
		return new PacketSocketData<RequestPlayerGameLobbyServersPacket>(this, this.getClass());
		
	}
	
}
