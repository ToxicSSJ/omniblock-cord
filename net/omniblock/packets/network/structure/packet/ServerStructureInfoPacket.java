package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;
import net.omniblock.packets.object.external.ServerType;

public class ServerStructureInfoPacket extends MessagePacket {
	
	private static final long serialVersionUID = -4363488044932137116L;
	
	protected String servername;
	protected String servertype;
	
	protected Integer socketport;
	protected Integer serverport;
	
	protected Integer onlineplayers;
	protected Integer maxplayers;
	
	protected String mapname;
	protected String info;
	
	protected Boolean isfull;
	protected Boolean islobbyserver;
	protected Boolean isgameserver;
	protected Boolean isgamelobbyserver;
	protected Boolean isstaffserver;
	protected Boolean isotherserver;
	
	public ServerStructureInfoPacket() {
		super(PacketType.SERVER_STRUCTURE_INFO);
	}
	
	public ServerStructureInfoPacket setServername(String servername){
		
		this.servername = servername;
		return this;
		
	}
	
	public ServerStructureInfoPacket setServertype(ServerType servertype){
		
		this.servertype = servertype.toString();
		return this;
		
	}
	
	public ServerStructureInfoPacket setSocketport(int socketport){
		
		this.socketport = socketport;
		return this;
		
	}
	
	public ServerStructureInfoPacket setServerport(int serverport){
		
		this.serverport = serverport;
		return this;
		
	}
	
	public ServerStructureInfoPacket setOnlineplayers(int onlineplayers){
		
		this.onlineplayers = onlineplayers;
		return this;
		
	}
	
	public ServerStructureInfoPacket setMaxplayers(int maxplayers){
		
		this.maxplayers = maxplayers;
		return this;
		
	}

	public ServerStructureInfoPacket setMapname(String mapname){
		
		this.mapname = mapname;
		return this;
		
	}
	
	public ServerStructureInfoPacket setInfo(String info){
		
		this.info = info;
		return this;
		
	}
	
	public ServerStructureInfoPacket setFull(boolean isfull){
		
		this.isfull = isfull;
		return this;
		
	}
	
	public ServerStructureInfoPacket setLobbyserver(boolean islobbyserver){
		
		this.islobbyserver = islobbyserver;
		return this;
		
	}
	
	public ServerStructureInfoPacket setGameserver(boolean isgameserver){
		
		this.isgameserver = isgameserver;
		return this;
		
	}
	
	public ServerStructureInfoPacket setGamelobbyserver(boolean isgamelobbyserver){
		
		this.isgamelobbyserver = isgamelobbyserver;
		return this;
		
	}
	
	public ServerStructureInfoPacket setStaffserver(boolean isstaffserver){
		
		this.isstaffserver = isstaffserver;
		return this;
		
	}
	
	public ServerStructureInfoPacket setOtherserver(boolean isotherserver){
		
		this.isotherserver = isotherserver;
		return this;
		
	}
	
	@Override
	public PacketSocketData<ServerStructureInfoPacket> build(){
		
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
		
		return new PacketSocketData<ServerStructureInfoPacket>(this, this.getClass());
		
	}
	
}
