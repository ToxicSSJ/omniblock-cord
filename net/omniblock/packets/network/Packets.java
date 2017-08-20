package net.omniblock.packets.network;

import net.omniblock.packets.network.tool.PacketReaderHandler;
import net.omniblock.packets.network.tool.PacketStreamerHandler;

/**
 * 
 * Esta clase principal almacena los handlers
 * de paquetes como variables generales y estaticas
 * para ser usadas a manera de API.
 * 
 * @author zlToxicNetherlz
 *
 */
public class Packets {

	/**
	 * 
	 * El Handler tipo READER para la lectura de los
	 * paquetes.
	 * 
	 */
	public static final PacketReaderHandler READER = new PacketReaderHandler(new Packets());
	
	/**
	 * 
	 * El Handler tipo STREAMER para la lectura de los
	 * paquetes.
	 * 
	 */
	public static final PacketStreamerHandler STREAMER = new PacketStreamerHandler(new Packets());
	
}
