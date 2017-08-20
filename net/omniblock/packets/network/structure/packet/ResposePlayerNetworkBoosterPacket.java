package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;

public class ResposePlayerNetworkBoosterPacket extends MessagePacket {
	
	private static final long serialVersionUID = -1835615194433503711L;
	
	protected String playername;
	protected String boosterkey;
	protected String gametype;
	
	public ResposePlayerNetworkBoosterPacket() {
		super(PacketType.RESPOSE_PLAYER_NETWORK_BOOSTER);
	}
	
	public ResposePlayerNetworkBoosterPacket setPlayername(String playername){
		
		this.playername = playername;
		return this;
		
	}
	
	public ResposePlayerNetworkBoosterPacket setBoosterkey(String boosterkey){
		
		this.boosterkey = boosterkey;
		return this;
		
	}
	
	public ResposePlayerNetworkBoosterPacket setGametype(String gametype){
		
		this.gametype = gametype;
		return this;
		
	}
	
	@Override
	public PacketSocketData<ResposePlayerNetworkBoosterPacket> build(){
		
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
		
		return new PacketSocketData<ResposePlayerNetworkBoosterPacket>(this, this.getClass());
		
	}
	
}
