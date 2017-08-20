package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;

public class ServerRemoveInfoPacket extends MessagePacket {

	private static final long serialVersionUID = -7559777007876644540L;
	
	protected String servername;
	
	public ServerRemoveInfoPacket() {
		super(PacketType.SERVER_REMOVE_INFO);
	}
	
	public ServerRemoveInfoPacket setServername(String servername){
		
		this.servername = servername;
		return this;
		
	}
	
	@Override
	public PacketSocketData<ServerRemoveInfoPacket> build(){
		
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
		
		return new PacketSocketData<ServerRemoveInfoPacket>(this, this.getClass());
		
	}
	
}
