package net.omniblock.packets.network.tool.object;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;

/**
 * 
 * Esta interface permite inicializar
 * una instancia con el fin de leer un
 * paquete tipo respuesta fácilmente
 * ejecutandose el metodo 'readRespose'
 * una vez llegue el paquete remoto
 * con la UUID registrada del enviado.
 * 
 * @author zlToxicNetherlz
 *
 * @param <T> El tipo de paquete que se
 * tomará como respuesta.
 */
public interface PacketResponder<T extends MessagePacket> {

	/**
	 * 
	 * Este metodo permite leer la respuesta
	 * de un paquete tipo respuesta, leyendose
	 * tál cual como lo hace un reader simple.
	 * 
	 * @param data El paquete de respuesta.
	 */
	public void readRespose(PacketSocketData<T> packetsocketdata);
	
}
