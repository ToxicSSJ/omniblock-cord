package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;

public class ResposeReloadInfoPacket extends MessagePacket {
	
	private static final long serialVersionUID = -1835615194433503711L;
	
	public ResposeReloadInfoPacket() {
		super(PacketType.RESPOSE_RELOAD_INFO);
	}
	
	@Override
	public PacketSocketData<ResposeReloadInfoPacket> build(){
		
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
		
		return new PacketSocketData<ResposeReloadInfoPacket>(this, this.getClass());
		
	}
	
}

