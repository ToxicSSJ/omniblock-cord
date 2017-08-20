package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;

public class ResposePlayerGameLobbiesPacket extends MessagePacket {
	
	private static final long serialVersionUID = -5844363787443217541L;
	
	protected String playername;
	protected String servers;
	
	public ResposePlayerGameLobbiesPacket() {
		super(PacketType.RESPOSE_PLAYER_GAME_LOBBIES);
	}
	
	public ResposePlayerGameLobbiesPacket setPlayername(String playername){
		
		this.playername = playername;
		return this;
		
	}
	
	public ResposePlayerGameLobbiesPacket setServers(String servers){
		
		this.servers = servers;
		return this;
		
	}
	
	@Override
	public PacketSocketData<ResposePlayerGameLobbiesPacket> build(){
		
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
		
		return new PacketSocketData<ResposePlayerGameLobbiesPacket>(this, this.getClass());
		
	}
	
}
