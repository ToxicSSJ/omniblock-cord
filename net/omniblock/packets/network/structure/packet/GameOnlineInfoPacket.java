package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;

public class GameOnlineInfoPacket extends MessagePacket {

	private static final long serialVersionUID = 1042490164869152804L;
	
	protected String servername;
	protected String mapname;
	
	protected Integer onlineplayers;
	protected Integer maximiumplayers;
	
	public GameOnlineInfoPacket() {
		super(PacketType.GAME_ONLINE_INFO);
	}
	
	public GameOnlineInfoPacket setServername(String servername){
		
		this.servername = servername;
		return this;
		
	}
	
	public GameOnlineInfoPacket setMapname(String mapname){
		
		this.mapname = mapname;
		return this;
		
	}
	
	public GameOnlineInfoPacket setOnlinePlayers(int onlineplayers){
		
		this.onlineplayers = onlineplayers;
		return this;
		
	}
	
	public GameOnlineInfoPacket setMaximiumPlayers(int maximiumplayers){
		
		this.maximiumplayers = maximiumplayers;
		return this;
		
	}
	
	@Override
	public PacketSocketData<GameOnlineInfoPacket> build(){
		
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
		
		return new PacketSocketData<GameOnlineInfoPacket>(this, this.getClass());
		
	}
	
}
