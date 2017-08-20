package net.omniblock.packets.network.tool;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.omniblock.packets.OmniPackets;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.socket.Sockets;
import net.omniblock.packets.network.socket.type.DataSenderStatus;
import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.data.PacketSocketData.PacketSocketDataHandler;
import net.omniblock.packets.network.structure.type.PacketShowType;
import net.omniblock.packets.network.tool.object.PacketResponder;
import net.omniblock.packets.object.exception.InvalidSystemException;

/**
 * 
 * Esta clase es la encargada de proveer los metodos
 * ques e usarán para distribución (envio) de los
 * paquetes al sistema remoto.
 * 
 * @author zlToxicNetherlz
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class PacketStreamerHandler {

	/**
	 * 
	 * Este mapa es el que contiene las respuestas que
	 * se deben ejecutar a la llegada de los paquetes que
	 * contengan la UUID puesta como 'key'. Este sistema
	 * se usa a la hora de enviar un paquete tipo respuesta.
	 * 
	 */
	protected static Map<UUID, PacketResponder> responders = new HashMap<UUID, PacketResponder>();
	
	/**
	 * 
	 * Constructor de la clase.
	 * 
	 * @param packets Instancia de la clase Packets.
	 */
	public PacketStreamerHandler(Packets packets){
		return;
	}
	
	/**
	 * 
	 * Distribuír un paquete al sistema remoto
	 * definido dentro del PacketSocketData.
	 * 
	 * @param data La data que será enviada.
	 * @see PacketSocketData
	 */
	public void streamPacket(PacketSocketData data){
		
		if(!OmniPackets.isStarted()) throw new InvalidSystemException("El sistema actual no es valido, ¿Se inició OmniPackets#setupSystem()?");
		if(data.getPacket().getPacketType().getShow() == PacketShowType.RESPOSE) throw new RuntimeException("El paquete que se intenta enviar es de tipo respuesta, Use en dicho caso el metodo PacketStreamerHandler#streamPacketAndRespose(PacketSocketData)!");
		if(data.isCancelled()) return;
		if(data.getReceiver() == null) return;
		
		Integer receiver = data.getReceiver();
		String serialized = PacketSocketDataHandler.serialize(data);
		
		sendSocketData(serialized, receiver);
		return;
		
	}
	
	/**
	 * 
	 * Distribuír un paquete al sistema remoto
	 * definido dentro del PacketSocketData y
	 * a su vez registrar una respuesta debido
	 * a que el paquete es de dicho tipo. Una
	 * vez el equipo remoto reenvie el paquete
	 * de respuesta, se ejecutará primero en el
	 * PacketResponder que en los demás, siendo
	 * entonces posible cancelarlo desde allí.
	 * 
	 * @param data La data que será enviada.
	 * @param responder La instancia con el metodo
	 * de respuesta.
	 * @see PacketSocketData
	 */
	public void streamPacketAndRespose(PacketSocketData data, PacketResponder<? extends MessagePacket> responder){
		
		if(!OmniPackets.isStarted()) throw new InvalidSystemException("El sistema actual no es valido, ¿Se inició OmniPackets#setupSystem()?");
		if(data.getPacket().getPacketType().getShow() == PacketShowType.ACTION) throw new RuntimeException("El paquete que se intenta enviar es de tipo acción, Use en dicho caso el metodo PacketStreamerHandler#streamPacket(PacketSocketData)!");
		if(data.isCancelled()) return;
		if(data.getReceiver() == null) return;
		
		Integer receiver = data.getReceiver();
		String serialized = PacketSocketDataHandler.serialize(data);
		
		registerResponder(responder, data.getPacketUUID());
		sendSocketData(serialized, receiver);
		return;
		
	}
	
	/**
	 * 
	 * Este metodo registra un responder facilmente
	 * en el mapa con el fin de ser utilizado luego
	 * para encontrarlo en base a su UUID y ejecutar
	 * su metodo de lectura.
	 * 
	 * @param responder La instancia con el metodo
	 * de respuesta.
	 * @param uuid La uuid del paquete generada.
	 */
	private void registerResponder(PacketResponder responder, UUID uuid){
		
		if(responder == null) return;
		if(uuid == null) return;
		
		responders.put(uuid, responder);
		return;
		
	}
	
	/**
	 * 
	 * Este metodo más definido es el que envia
	 * la data a cierto puerto usando los metodos
	 * proveidos por el adaptador del tipo Cliente.
	 * 
	 * @param data La data que será enviada.
	 * @param receiver El puerto donde será enviada.
	 * @return El estado del paquete.
	 */
	private DataSenderStatus sendSocketData(String data, Integer receiver){
		
		return Sockets.CLIENT.sendData(data, receiver);
		
	}
	
	public Map<UUID, PacketResponder> getResponders(){
		return responders;
	}
	
}
