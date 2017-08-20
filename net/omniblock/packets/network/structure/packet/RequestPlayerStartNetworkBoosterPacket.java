package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;

public class RequestPlayerStartNetworkBoosterPacket extends MessagePacket {

	private static final long serialVersionUID = -6385978744685776582L;
	
	protected String playername;
	protected String gametype;
	protected String key;
	
	protected Integer duration;
	
	public RequestPlayerStartNetworkBoosterPacket() {
		super(PacketType.REQUEST_PLAYER_START_NETWORK_BOOSTER);
	}
	
	public RequestPlayerStartNetworkBoosterPacket setPlayername(String playername){
		
		this.playername = playername;
		return this;
		
	}
	
	public RequestPlayerStartNetworkBoosterPacket setGametype(String gametype){
		
		this.gametype = gametype;
		return this;
		
	}
	
	public RequestPlayerStartNetworkBoosterPacket setKey(String key){
		
		this.key = key;
		return this;
		
	}
	
	public RequestPlayerStartNetworkBoosterPacket setDuration(Integer duration){
		
		this.duration = duration;
		return this;
		
	}
	
	@Override
	public PacketSocketData<RequestPlayerStartNetworkBoosterPacket> build(){
		
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
		
		return new PacketSocketData<RequestPlayerStartNetworkBoosterPacket>(this, this.getClass());
		
	}
	
}
