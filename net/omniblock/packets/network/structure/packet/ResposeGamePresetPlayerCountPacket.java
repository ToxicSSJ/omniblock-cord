package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;
import net.omniblock.packets.object.external.GamePreset;

public class ResposeGamePresetPlayerCountPacket extends MessagePacket {
	
	private static final long serialVersionUID = -812018949437147633L;
	
	protected String gamepreset;
	
	protected Integer playercount;
	
	public ResposeGamePresetPlayerCountPacket() {
		super(PacketType.RESPOSE_GAMEPRESET_PLAYER_COUNT);
	}
	
	public ResposeGamePresetPlayerCountPacket setGamepreset(GamePreset gamepreset){
		
		this.gamepreset = gamepreset.toString();
		return this;
		
	}
	
	public ResposeGamePresetPlayerCountPacket setPlayercount(int playercount){
		
		this.playercount = playercount;
		return this;
		
	}
	
	@Override
	public PacketSocketData<ResposeGamePresetPlayerCountPacket> build(){
		
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
		
		return new PacketSocketData<ResposeGamePresetPlayerCountPacket>(this, this.getClass());
		
	}
	
}
