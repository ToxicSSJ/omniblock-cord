package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;
import java.util.List;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;

public class ResposeBoostedGamesPacket extends MessagePacket {
	
	private static final long serialVersionUID = 3647335823198540192L;
	
	protected String boostedgames;
	
	public ResposeBoostedGamesPacket() {
		super(PacketType.RESPOSE_BOOSTED_GAMES);
	}
	
	public ResposeBoostedGamesPacket setBoostedGames(List<String> boostedgames){
		
		this.boostedgames = String.join(",", boostedgames);
		return this;
		
	}
	
	@Override
	public PacketSocketData<ResposeBoostedGamesPacket> build(){
		
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
		
		return new PacketSocketData<ResposeBoostedGamesPacket>(this, this.getClass());
		
	}
	
}
