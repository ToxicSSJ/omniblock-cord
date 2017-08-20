package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;

public class WelcomeProxyPacket extends MessagePacket {
	
	private static final long serialVersionUID = -8461069225170979370L;

	public WelcomeProxyPacket() {
		super(PacketType.WELCOME_PROXY);
	}
	
	@Override
	public PacketSocketData<WelcomeProxyPacket> build(){
		
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
		
		return new PacketSocketData<WelcomeProxyPacket>(this, this.getClass());
		
	}
	
}
