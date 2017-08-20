package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;

public class ServerReloadInfoPacket extends MessagePacket {

	private static final long serialVersionUID = -5716697033227511177L;
	
	protected String servername;
	protected Integer socketport;
	
	public ServerReloadInfoPacket() {
		super(PacketType.SERVER_RELOAD_INFO);
	}
	
	public ServerReloadInfoPacket setServername(String servername){
		
		this.servername = servername;
		return this;
		
	}
	
	public ServerReloadInfoPacket setSocketport(Integer socketport){
		
		this.socketport = socketport;
		return this;
		
	}
	
	@Override
	public PacketSocketData<ServerReloadInfoPacket> build(){
		
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
		
		return new PacketSocketData<ServerReloadInfoPacket>(this, this.getClass());
		
	}
	
}
