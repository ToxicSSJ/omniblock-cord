package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;
import net.omniblock.packets.object.external.GamePreset;

public class GameStructureInfoPacket extends MessagePacket {
	
	private static final long serialVersionUID = 4709462108951589393L;
	
	protected String servername;
	protected String gamepreset;
	
	protected Integer minimiumplayers;
	protected Integer maximiumplayers;
	
	protected String mapname;
	protected String extrainfo;
	
	protected Boolean islocked;
	protected Boolean isstarted;
	protected Boolean isreload;
	protected Boolean isnext;
	
	public GameStructureInfoPacket() {
		super(PacketType.GAME_STRUCTURE_INFO);
	}
	
	public GameStructureInfoPacket setServername(String servername){
		
		this.servername = servername;
		return this;
		
	}
	
	public GameStructureInfoPacket setGamepreset(GamePreset gamepreset){
		
		this.gamepreset = gamepreset.toString();
		return this;
		
	}
	
	public GameStructureInfoPacket setMinimiumplayers(int minimiumplayers){
		
		this.minimiumplayers = minimiumplayers;
		return this;
		
	}
	
	public GameStructureInfoPacket setMaximiumplayers(int maximiumplayers){
		
		this.maximiumplayers = maximiumplayers;
		return this;
		
	}
	
	public GameStructureInfoPacket setMapname(String mapname){
		
		this.mapname = mapname;
		return this;
		
	}
	
	public GameStructureInfoPacket setExtrainfo(String extrainfo){
		
		this.extrainfo = extrainfo;
		return this;
		
	}
	
	public GameStructureInfoPacket setLocked(boolean islocked){
		
		this.islocked = islocked;
		return this;
		
	}
	
	public GameStructureInfoPacket setStarted(boolean isstarted){
		
		this.isstarted = isstarted;
		return this;
		
	}
	
	public GameStructureInfoPacket setReload(boolean isreload){
		
		this.isreload = isreload;
		return this;
		
	}
	
	public GameStructureInfoPacket setNext(boolean isnext){
		
		this.isnext = isnext;
		return this;
		
	}
	
	@Override
	public PacketSocketData<GameStructureInfoPacket> build(){
		
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
		
		return new PacketSocketData<GameStructureInfoPacket>(this, this.getClass());
		
	}
	
}
