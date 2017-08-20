package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;
import net.omniblock.packets.object.external.GamePreset;

public class ResposeGameInitializerPacket extends MessagePacket {
	
	private static final long serialVersionUID = -5596804265606537108L;
	
	protected String gamepreset;
	
	public ResposeGameInitializerPacket() {
		super(PacketType.RESPOSE_GAME_INITIALIZER);
	}
	
	public ResposeGameInitializerPacket setGamepreset(GamePreset preset){
		
		this.gamepreset = preset.toString();
		return this;
		
	}
	
	@Override
	public PacketSocketData<ResposeGameInitializerPacket> build(){
		
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
		
		return new PacketSocketData<ResposeGameInitializerPacket>(this, this.getClass());
		
	}
	
}
