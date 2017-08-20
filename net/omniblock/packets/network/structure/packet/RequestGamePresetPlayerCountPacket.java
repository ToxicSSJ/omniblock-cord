package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;
import net.omniblock.packets.object.external.GamePreset;

public class RequestGamePresetPlayerCountPacket extends MessagePacket {
	
	private static final long serialVersionUID = -6519638133157751879L;
	
	protected String servername;
	protected String gamepreset;
	
	public RequestGamePresetPlayerCountPacket() {
		super(PacketType.REQUEST_GAMEPRESET_PLAYER_COUNT);
	}
	
	public RequestGamePresetPlayerCountPacket setServername(String servername){
		
		this.servername = servername;
		return this;
		
	}
	
	public RequestGamePresetPlayerCountPacket setGamepreset(GamePreset gamepreset){
		
		this.gamepreset = gamepreset.toString();
		return this;
		
	}
	
	@Override
	public PacketSocketData<RequestGamePresetPlayerCountPacket> build(){
		
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
		
		return new PacketSocketData<RequestGamePresetPlayerCountPacket>(this, this.getClass());
		
	}
	
}
