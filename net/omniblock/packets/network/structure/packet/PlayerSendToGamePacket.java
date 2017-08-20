package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;
import net.omniblock.packets.object.external.GamePreset;

public class PlayerSendToGamePacket extends MessagePacket {

	private static final long serialVersionUID = -2962440052225339338L;
	
	protected String playername;
	protected String preset;
	
	protected Boolean party;
	
	public PlayerSendToGamePacket() {
		super(PacketType.PLAYER_SEND_TO_GAME);
	}
	
	public PlayerSendToGamePacket setPlayername(String playername){
		
		this.playername = playername;
		return this;
		
	}
	
	public PlayerSendToGamePacket useParty(boolean party){
		
		this.party = party;
		return this;
		
	}
	
	public PlayerSendToGamePacket setPreset(GamePreset preset){
		
		this.preset = preset.toString();
		return this;
		
	}
	
	@Override
	public PacketSocketData<PlayerSendToGamePacket> build(){
		
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
		
		return new PacketSocketData<PlayerSendToGamePacket>(this, this.getClass());
		
	}
	
}
