package net.omniblock.packets.network.tool.annotation.type;

/**
 * 
 * Este enumerador contiene todos
 * los tipos de prioridades de los
 * paquetes.
 * 
 * @author zlToxicNetherlz
 *
 */
public enum PacketPriority {

	LOW(0),
	NORMAL(1),
	HIGH(2),
	CONSOLE(3),
	
	;
	
	private final int slot;
	
	private PacketPriority(int slot){
		this.slot = slot;
	}
	
	public int getSlot(){
		return slot;
	}
	
}
