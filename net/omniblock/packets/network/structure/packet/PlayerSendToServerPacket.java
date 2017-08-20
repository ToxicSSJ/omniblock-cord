package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;
import net.omniblock.packets.object.external.ServerType;

public class PlayerSendToServerPacket extends MessagePacket {

	private static final long serialVersionUID = 430959018552876497L;
	
	protected String playername;
	protected String servertype;
	
	protected Boolean party;
	
	public PlayerSendToServerPacket() {
		super(PacketType.PLAYER_SEND_TO_SERVER);
	}
	
	public PlayerSendToServerPacket setPlayername(String playername){
		
		this.playername = playername;
		return this;
		
	}
	
	public PlayerSendToServerPacket setServertype(ServerType servertype){
		
		this.servertype = servertype.toString();
		return this;
		
	}
	
	public PlayerSendToServerPacket setParty(Boolean party){
		
		this.party = party;
		return this;
		
	}
	
	@Override
	public PacketSocketData<PlayerSendToServerPacket> build(){
		
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
		
		return new PacketSocketData<PlayerSendToServerPacket>(this, this.getClass());
		
	}
	
}
