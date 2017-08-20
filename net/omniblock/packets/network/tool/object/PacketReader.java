package net.omniblock.packets.network.tool.object;

import java.util.UUID;

import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.tool.annotation.PacketEvent;
import net.omniblock.packets.network.tool.annotation.type.PacketPriority;

/**
 * 
 * Esta clase es la encargada de dar los metodos
 * necesarios para la lectura de un paquete de manera
 * que se debe instanciar y añadirse el handler
 * de procesamiento de lectores.
 * 
 * @author zlToxicNetherlz
 *
 * @param <T> El tipo de paquete que se leerá (La clase extendida de MessagePacket).
 */
public interface PacketReader<T extends MessagePacket> {
	
	public UUID readerhash = UUID.randomUUID();
	
	/**
	 * 
	 * Este metodo que se debe sobreescribir es el que se
	 * encarga de leer el paquete el cual lo recibirá por
	 * medio de la clase de datos serializable PacketSocketData,
	 * A su vez a este metodo se le puede colocar una prioridad
	 * la cual es definida por la anotación @PacketEvent que
	 * se le puede colocar directamente una vez sobreescrito
	 * y que por defecto es NORMAL.
	 * 
	 * @param packetsocketdata Los datos recibidos.
	 * @see PacketSocketData
	 */
	@PacketEvent(priority = PacketPriority.NORMAL)
	public void readPacket(PacketSocketData<T> packetsocketdata);
	
	/**
	 * @return La clase del paquete extendido de MessagePacket.
	 */
	public Class<T> getAttachedPacketClass();
	
	/**
	 * @return La identidad unica de este lector.
	 */
	public default UUID getUUID(){
		return readerhash;
	}
	
	/**
	 * @return El paquete inicializado desde el metodo
	 * de adjunción de Class<T>.
	 */
	public default T getPacket(){
		
		try { return (T) getAttachedPacketClass().newInstance(); }
		catch (InstantiationException | IllegalAccessException e) { e.printStackTrace(); }
		
		return null;
		
	}
	
}
