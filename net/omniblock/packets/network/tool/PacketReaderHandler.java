package net.omniblock.packets.network.tool;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.omniblock.packets.OmniPackets;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.tool.annotation.PacketEvent;
import net.omniblock.packets.network.tool.annotation.type.PacketPriority;
import net.omniblock.packets.network.tool.object.PacketReader;
import net.omniblock.packets.network.tool.object.PacketResponder;
import net.omniblock.packets.object.exception.InvalidSystemException;
import net.omniblock.packets.util.Lists;

/**
 * 
 * Esta clase es la encargada del manejo de la lectura
 * de los paquetes por medio de metodos importantes
 * para agregar, remover y inciar el procesamiento de
 * lecturas (PacketReader).
 * 
 * @author zlToxicNetherlz
 * @see PacketReader
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class PacketReaderHandler {
	
	protected List<PacketReader> readers = new LinkedList<PacketReader>();
	protected int serialID = -1;
	
	/**
	 * 
	 * Constructor principal de la Clase.
	 * 
	 * @param packets La instancia de la clase Packets.
	 */
	public PacketReaderHandler(Packets packets){
		return;
	}
	
	/**
	 * 
	 * Este metodo inicia el proceso de lectura de un PacketSocketData
	 * donde se hacen primero ciertas comprobaciones y luego se listan
	 * todos los readers cuyo tipo de paquete en terminos de lecturas
	 * sean iguales al tipo de paquete del PacketSocketData y ser añadidas
	 * a una lista, donde luego se ejecutan en orden a su prioridad
	 * ejecutandose primero las de mayor prioridad (CONSOLE) y de ultimo
	 * las de menor prioridad (LOW).
	 * 
	 * @param packetsocketdata
	 */
	public void startRead(PacketSocketData<? extends MessagePacket> packetsocketdata){
		
		if(!OmniPackets.isStarted()) throw new InvalidSystemException("El sistema actual no es valido, ¿Se inició OmniPackets#setupSystem()?");
		if(packetsocketdata.isCancelled()) return;
		
		Map<UUID, PacketResponder> responders = Packets.STREAMER.getResponders();
		
		if(responders.containsKey(packetsocketdata.getPacketUUID())){
			
			PacketResponder responder = responders.get(packetsocketdata.getPacketUUID());
			Packets.STREAMER.getResponders().remove(packetsocketdata.getPacketUUID());
			responder.readRespose(packetsocketdata);
			
			return;
			
		}
		
		List<Entry<PacketReader, PacketPriority>> priorities = Lists.newArrayList();
		List<UUID> readershashes = Lists.newArrayList();
		
		for(int row = 0; row < readers.size(); row++){
			
			if(packetsocketdata.isCancelled()) break;
			
			PacketReader reader = readers.get(row);
			
			if(reader.getPacket().getPacketType() == packetsocketdata.getPacket().getPacketType()){
				
				priorities.add(
						new AbstractMap.SimpleEntry<PacketReader,
						PacketPriority>(reader, getPacketPriority(reader)));
				
			}
			
		}
		
		for(int row = PacketPriority.CONSOLE.getSlot(); row >= PacketPriority.LOW.getSlot(); row--) {
			
			if(packetsocketdata.isCancelled()) break;
			
			ENTRY_LOOP: for(Entry<PacketReader, PacketPriority> entry : priorities){
				
				if(readershashes.contains(entry.getKey().getUUID())) continue ENTRY_LOOP;
				
				if(entry.getValue().getSlot() == row){
					
					entry.getKey().readPacket(packetsocketdata);
					readershashes.add(entry.getKey().getUUID());
					
					if(packetsocketdata.isCancelled()) break ENTRY_LOOP;
					
				}
				
			}
			
		}
		
	}
	
	/**
	 * 
	 * Este metodo permite obtener la prioridad de un PacketReader
	 * basandose de la anotación @PacketEvent que proviene del metodo
	 * readPacket dentro del mismo PacketReader, que pasando a ser
	 * sobreescrita desde la inciación de una instancia se puede
	 * definir libremente dicha notación y darle una prioridad de
	 * manera fácil.
	 * 
	 * @param reader La instancia del PacketReader donde se leerá su prioridad
	 * por medio de la notación @PacketEvent colocada en el metodo sobreescrito
	 * readPacket.
	 * @return La prioridad en el formato del enumerador PacketPriority.
	 * @see PacketPriority
	 */
	public PacketPriority getPacketPriority(PacketReader<? extends MessagePacket> reader){
		
		try {
			
			Method m = reader.getClass().getMethod("readPacket", PacketSocketData.class);
			PacketEvent e = m.getAnnotation(PacketEvent.class);
			
			PacketPriority priority = e.priority();
			
			return priority;
			
		} catch (Exception e) {
			
			if(e instanceof NullPointerException) return PacketPriority.NORMAL;
			e.printStackTrace();
			
		}
		
		return PacketPriority.NORMAL;
		
	}
	
	/**
	 * 
	 * Esta clase permite registrar un nuevo lector de paquetes donde
	 * la manera de uso es inicializar una instancia de PacketReader con
	 * un paquete definido y sobreescribir los metodos de lectura y recepción
	 * del tipo de clase que se está tratando.
	 * 
	 * @param reader El lector del paquete que se registrará.
	 * @return el serialID del numero (turno) del registro.
	 * @see PacketReader
	 */
	public int registerReader(PacketReader<? extends MessagePacket> reader){
		
		if(readers.contains(reader)){
			
			throw new RuntimeException("El PacketReader que se intenta registrar ya se encuentra en el sistema.");
			
		}
		
		readers.add(reader);
		return serialID++;
		
	}
	
	/**
	 * 
	 * Esta clase permite remover un lector de paquetes ya registrado
	 * en el sistema.
	 * 
	 * @param reader El lector del paquete que se removerá.
	 * @see PacketReader
	 */
	public void removeReader(PacketReader<? extends MessagePacket> reader){
		
		if(!readers.contains(reader)){
			
			throw new RuntimeException("El PacketReader no se ha logrado encontrar en el sistema.");
			
		}
		
		readers.remove(reader);
		
	}
	
}
