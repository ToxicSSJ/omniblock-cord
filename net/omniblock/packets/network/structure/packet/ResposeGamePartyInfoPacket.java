package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;
import java.util.List;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;

public class ResposeGamePartyInfoPacket extends MessagePacket {
	
	private static final long serialVersionUID = 8444718099006251800L;
	
	protected String players;
	
	public ResposeGamePartyInfoPacket() {
		super(PacketType.RESPOSE_GAME_PARTY_INFO);
	}
	
	public ResposeGamePartyInfoPacket setPlayers(List<String> players){
		
		this.players = String.join(",", players);
		return this;
		
	}
	
	@Override
	public PacketSocketData<ResposeGamePartyInfoPacket> build(){
		
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
		
		return new PacketSocketData<ResposeGamePartyInfoPacket>(this, this.getClass());
		
	}
	
}
