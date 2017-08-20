package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;

public class PlayerSendMessagePacket extends MessagePacket {

	private static final long serialVersionUID = 4848603757156286536L;
	
	protected String playername;
	protected String message;
	
	public PlayerSendMessagePacket() {
		super(PacketType.PLAYER_SEND_MESSAGE);
	}
	
	public PlayerSendMessagePacket setPlayername(String playername){
		
		this.playername = playername;
		return this;
		
	}
	
	public PlayerSendMessagePacket setMessage(String message){
		
		this.message = message;
		return this;
		
	}
	
	@Override
	public PacketSocketData<PlayerSendMessagePacket> build(){
		
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
		
		return new PacketSocketData<PlayerSendMessagePacket>(this, this.getClass());
		
	}
	
}

