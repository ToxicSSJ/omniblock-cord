package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;

public class ServerSocketInfoPacket extends MessagePacket {

	private static final long serialVersionUID = 5664435942893600084L;
	
	protected String servername;
	protected Integer serversocket;
	
	public ServerSocketInfoPacket() {
		super(PacketType.SERVER_SOCKET_INFO);
	}
	
	public ServerSocketInfoPacket setServername(String servername){
		
		this.servername = servername;
		return this;
		
	}
	
	public ServerSocketInfoPacket setServersocket(Integer serversocket){
		
		this.serversocket = serversocket;
		return this;
		
	}
	
	@Override
	public PacketSocketData<ServerSocketInfoPacket> build(){
		
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
		
		return new PacketSocketData<ServerSocketInfoPacket>(this, this.getClass());
		
	}
	
}
