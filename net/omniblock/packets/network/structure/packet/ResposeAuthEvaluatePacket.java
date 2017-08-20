package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;

public class ResposeAuthEvaluatePacket extends MessagePacket {
	
	private static final long serialVersionUID = -17175846882541468L;
	
	protected String status;
	protected String playername;
	
	public ResposeAuthEvaluatePacket() {
		super(PacketType.RESPOSE_AUTH_EVALUATE);
	}
	
	public ResposeAuthEvaluatePacket setStatus(String status){
		
		this.status = status;
		return this;
		
	}
	
	public ResposeAuthEvaluatePacket setPlayername(String playername){
		
		this.playername = playername;
		return this;
		
	}
	
	@Override
	public PacketSocketData<ResposeAuthEvaluatePacket> build(){
		
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
		
		return new PacketSocketData<ResposeAuthEvaluatePacket>(this, this.getClass());
		
	}
	
}
