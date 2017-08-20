package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;

public class PlayerSendToNamedServerPacket extends MessagePacket {

	private static final long serialVersionUID = -6025769575932784455L;
	
	protected String playername;
	protected String servername;
	
	protected Boolean party;
	
	public PlayerSendToNamedServerPacket() {
		super(PacketType.PLAYER_SEND_TO_NAMED_SERVER);
	}
	
	public PlayerSendToNamedServerPacket setPlayername(String playername){
		
		this.playername = playername;
		return this;
		
	}
	
	public PlayerSendToNamedServerPacket setServername(String playername){
		
		this.playername = playername;
		return this;
		
	}
	
	public PlayerSendToNamedServerPacket setParty(Boolean party){
		
		this.party = party;
		return this;
		
	}
	
	@Override
	public PacketSocketData<PlayerSendToGamePacket> build(){
		
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
		
		return new PacketSocketData<PlayerSendToGamePacket>(this, this.getClass());
		
	}
	
}
