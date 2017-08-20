package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;

public class PlayerSendBanPacket extends MessagePacket {

	private static final long serialVersionUID = -6374579709582297646L;
	
	protected String playername;
	
	public PlayerSendBanPacket() {
		super(PacketType.PLAYER_SEND_BAN);
	}
	
	public PlayerSendBanPacket setPlayername(String playername){
		
		this.playername = playername;
		return this;
		
	}
	
	@Override
	public PacketSocketData<PlayerSendBanPacket> build(){
		
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
		
		return new PacketSocketData<PlayerSendBanPacket>(this, this.getClass());
		
	}
	
}
