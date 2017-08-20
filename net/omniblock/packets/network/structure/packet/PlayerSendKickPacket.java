package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;

public class PlayerSendKickPacket extends MessagePacket {

	private static final long serialVersionUID = 2660994477704059123L;
	
	protected String playername;
	protected String moderator;
	protected String reason;
	
	public PlayerSendKickPacket() {
		super(PacketType.PLAYER_SEND_KICK);
	}
	
	public PlayerSendKickPacket setPlayername(String playername){
		
		this.playername = playername;
		return this;
		
	}
	
	public PlayerSendKickPacket setModerator(String moderator){
		
		this.moderator = moderator;
		return this;
		
	}
	
	public PlayerSendKickPacket setReason(String reason){
		
		this.reason = reason;
		return this;
		
	}
	
	@Override
	public PacketSocketData<PlayerSendKickPacket> build(){
		
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
		
		return new PacketSocketData<PlayerSendKickPacket>(this, this.getClass());
		
	}
	
}
