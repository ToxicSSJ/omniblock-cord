package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;
import net.omniblock.packets.object.external.GamePreset;

public class GameInitializerInfoPacket extends MessagePacket {
	
	private static final long serialVersionUID = 8075486302926242657L;

	protected String servername;
	protected String gamepreset;
	
	protected Integer socketport;
	
	public GameInitializerInfoPacket() {
		super(PacketType.INITIALIZE_GAME);
	}
	
	public GameInitializerInfoPacket setServername(String servername){
		
		this.servername = servername;
		return this;
		
	}
	
	public GameInitializerInfoPacket setGamepreset(GamePreset gamepreset){
		
		this.gamepreset = gamepreset.toString();
		return this;
		
	}
	
	public GameInitializerInfoPacket setSocketport(int socketport){
		
		this.socketport = socketport;
		return this;
		
	}
	
	@Override
	public PacketSocketData<GameInitializerInfoPacket> build(){
		
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
		
		return new PacketSocketData<GameInitializerInfoPacket>(this, this.getClass());
		
	}
	
}
