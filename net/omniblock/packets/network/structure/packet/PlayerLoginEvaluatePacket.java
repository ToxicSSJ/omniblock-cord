package net.omniblock.packets.network.structure.packet;

import java.lang.reflect.Field;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;

public class PlayerLoginEvaluatePacket extends MessagePacket {

	private static final long serialVersionUID = -4981548002656572357L;

	protected String playername;
	
	protected Boolean iplogin;
	
	public PlayerLoginEvaluatePacket() {
		super(PacketType.PLAYER_LOGIN_EVALUATE);
	}
	
	public PlayerLoginEvaluatePacket setPlayername(String playername){
		
		this.playername = playername;
		return this;
		
	}
	
	public PlayerLoginEvaluatePacket useIPLogin(boolean iplogin){
		
		this.iplogin = iplogin;
		return this;
		
	}
	
	@Override
	public PacketSocketData<PlayerLoginEvaluatePacket> build(){
		
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
		
		return new PacketSocketData<PlayerLoginEvaluatePacket>(this, this.getClass());
		
	}
	
}
