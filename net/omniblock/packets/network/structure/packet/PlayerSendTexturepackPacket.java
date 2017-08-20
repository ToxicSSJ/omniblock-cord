package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;

public class PlayerSendTexturepackPacket extends MessagePacket {

	private static final long serialVersionUID = 6572343040301064851L;
	
	protected String playername;
	protected String texturehash;
	
	public PlayerSendTexturepackPacket() {
		super(PacketType.PLAYER_SEND_TEXTUREPACK);
	}
	
	public PlayerSendTexturepackPacket setPlayername(String playername){
		
		this.playername = playername;
		return this;
		
	}
	
	public PlayerSendTexturepackPacket setTexturehash(String texturehash){
		
		this.texturehash = texturehash;
		return this;
		
	}
	
	@Override
	public PacketSocketData<PlayerSendTexturepackPacket> build(){
		
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
		
		return new PacketSocketData<PlayerSendTexturepackPacket>(this, this.getClass());
		
	}
	
}

