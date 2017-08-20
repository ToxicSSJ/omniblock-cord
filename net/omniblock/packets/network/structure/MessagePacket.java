package net.omniblock.packets.network.structure;

import java.io.Serializable;

import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.type.PacketType;

/**
 * 
 * Esta clase es la base para los paquetes
 * y tiene las clases principales de todos
 * los paquetes con el fin de que se respete
 * la estructura definida.
 * 
 * Es serializable para que se pueda convertir
 * desde un handler en un ObjectInputStream.
 * 
 * @author zlToxicNetherlz
 *
 */
public class MessagePacket implements Serializable {

	private static final long serialVersionUID = 8944712585676092877L;
	
	private PacketType type;
	
	/**
	 * 
	 * Constructor principal de la clase.
	 * 
	 * @param type El tipo de paquete desde el enumerador con todos
	 * los tipos de paquetes posibles.
	 */
	public MessagePacket(PacketType type){
		
		this.type = type;
		return;
		
	}
	
	/**
	 * 
	 * Este metodo construye el paquete de tál manera
	 * que se pueda crear una clase tipo PacketSocketData
	 * para ser serializada y a su vez enviada como un
	 * Socket desde los adaptadores.
	 * 
	 * @return El PacketSocketData que contiene toda la información
	 * del paquete.
	 * @see PacketSocketData
	 */
	@SuppressWarnings("rawtypes")
	public PacketSocketData build(){
		
		throw new UnsupportedOperationException("No se ha definido el metodo build() en la clase extendida de MessagePacket.");
		
	}
	
	/**
	 * 
	 * @return El tipo de paquete que se utiliza en este paquete.
	 */
	public PacketType getPacketType(){
		
		return type;
		
	}
	
}
