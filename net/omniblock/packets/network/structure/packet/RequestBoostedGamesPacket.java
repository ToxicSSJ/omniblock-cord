package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;

public class RequestBoostedGamesPacket extends MessagePacket {

	private static final long serialVersionUID = -2530134428885074753L;
	
	protected String servername;
	
	public RequestBoostedGamesPacket() {
		super(PacketType.REQUEST_BOOSTED_GAMES);
	}
	
	public RequestBoostedGamesPacket setServername(String servername){
		
		this.servername = servername;
		return this;
		
	}
	
	@Override
	public PacketSocketData<RequestBoostedGamesPacket> build(){
		
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
		
		return new PacketSocketData<RequestBoostedGamesPacket>(this, this.getClass());
		
	}
	
}
