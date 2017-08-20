package net.omniblock.packets.network.structure.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

import net.omniblock.packets.network.socket.helper.SocketHelper;
import net.omniblock.packets.network.structure.MessagePacket;
import net.omniblock.packets.network.structure.data.PacketStructure.DataType;
import net.omniblock.packets.network.structure.type.PacketSenderType;

/**
 * 
 * Esta clase almacena toda la información serializable
 * de un paquete para ser enviado en forma de Socket
 * desde los adaptadores y ser procesada por el receptor
 * del paquete.
 * 
 * @author zlToxicNetherlz
 *
 * @param <T> La clase del paquete extendida a MessagePacket.
 * @see MessagePacket
 * @see Serializable
 */
public class PacketSocketData<T extends MessagePacket> implements Serializable {

	private static final long serialVersionUID = 8391144260600004749L;
	
	/**
	 * 
	 * Variable que define el estado de los datos
	 * del paquete que tiene como fin parar el
	 * procesamiento una vez se cancele.
	 * 
	 */
	private boolean cancelled = false;
	
	/**
	 * 
	 * Esta UUID es el identificador unico de
	 * cada paquete, el cual es utilizado para
	 * el sistema de envio y respuesta.
	 * 
	 */
	private UUID packetUUID;
	
	/**
	 * 
	 * La clase del paquete en formato ("Clase".class)
	 * 
	 */
	private Class<? extends MessagePacket> packetClass;
	
	/**
	 * 
	 * La instancia del paquete ya construido.
	 * 
	 */
	private MessagePacket packet;
	
	/**
	 * 
	 * La estructura con todas las variables ya en formato
	 * PacketObject del paquete.
	 * 
	 */
	private PacketStructure structure = new PacketStructure();
	
	/**
	 * 
	 * El puerto que recibirá el paquete.
	 * 
	 */
	private Integer receiver;
	
	/**
	 * 
	 * Constructor principal de la clase, en él se toman todas las variables
	 * del paquete colocado en el paramo 1, con el fin de ser pegadas automaticamente
	 * en la estructura {@link PacketStructure} para luego ser utilizadas de
	 * una manera amigable (Creado con el fin de que no se pueda reconstruir
	 * la imutabilidad del paquete). En este caso la identidad unica (uuid) se
	 * generará al azar, ya que no se define que es un paquete tipo respuesta.
	 * 
	 * @param packet El paquete como tál, (Su instancia).
	 * @param clazz La clase del paquete, (Su .class).
	 * @see PacketStructure
	 * @see DataType
	 */
	public PacketSocketData(MessagePacket packet, Class<? extends MessagePacket> clazz){
		this(packet, clazz, UUID.randomUUID());
	}
	
	/**
	 * 
	 * Constructor principal de la clase, en él se toman todas las variables
	 * del paquete colocado en el paramo 1, con el fin de ser pegadas automaticamente
	 * en la estructura {@link PacketStructure} para luego ser utilizadas de
	 * una manera amigable (Creado con el fin de que no se pueda reconstruir
	 * la imutabilidad del paquete).
	 * 
	 * @param packet El paquete como tál, (Su instancia).
	 * @param clazz La clase del paquete, (Su .class).
	 * @param uuid La identidad unica del paquete (funcional para 
	 * el sistema de respuestas).
	 * @see PacketStructure
	 * @see DataType
	 */
	public PacketSocketData(MessagePacket packet, Class<? extends MessagePacket> clazz, UUID uuid){
		
		this.packet = packet;
		this.packetClass = clazz;
		this.packetUUID = UUID.randomUUID();
		
		for(Field f : clazz.getDeclaredFields()){
			
			f.setAccessible(true);
			
			try {
				
				Object param = f.get(packet);
				DataType dataType = null;
				
				for(DataType type : DataType.values()) { if(param.getClass() == type.getGenericClass()) { dataType = type; break; } }
				
				if(dataType == null) throw new RuntimeException("El tipo de objeto '" + param.getClass().getName() + "' no es un objeto para paquetes valido.");
				else {
						
					structure.set(dataType, f.getName(), param);
					
				}
				
			} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
				
				e.printStackTrace();
				
			}
			
			f.setAccessible(false);
			
			continue;
			
		}
		
	}
	
	/**
	 * 
	 * Este metodo permite colocar como receptor
	 * al enumerador amigable PacketSenderType.
	 * 
	 * @param receiver Un valor del enumerador.
	 * @return La instancia para funcionar de manera Immutable.
	 * @see PacketSenderType
	 */
	public PacketSocketData<T> setReceiver(PacketSenderType receiver){
		this.receiver = SocketHelper.getReceiverPort(receiver);
		return this;
	}
	
	/**
	 * 
	 * Este metodo permite colocar como receptor
	 * al puerto donde se enviará este paquete.
	 * 
	 * @param socketport El puerto donde se enviará el socket.
	 * @return La instancia para funcionar de manera Immutable.
	 */
	public PacketSocketData<T> setReceiver(int socketport){
		this.receiver = socketport;
		return this;
	}
	
	public Integer getReceiver(){
		return receiver;
	}
	
	public PacketSocketData<T> setCancelled(boolean cancelled){
		this.cancelled = cancelled;
		return this;
	}
	
	public boolean isCancelled(){
		return cancelled;
	}
	
	public PacketStructure getStructure(){
		return structure;
	}
	
	@SuppressWarnings("unchecked")
	public T getPacket(){
		return (T) packet;
	}
	
	public Class<? extends MessagePacket> getPacketClass() {
		return packetClass;
	}
	
	public PacketSocketData<T> setPacketUUID(UUID uuid) {
		
		this.packetUUID = uuid;
		return this;
		
	}
	
	public UUID getPacketUUID() {
		return packetUUID;
	}

	/**
	 * 
	 * Esta clase es el Serializador del objeto
	 * cuyas funciones son utiles a la hora de
	 * leer y encriptar (serializar) la información.
	 * 
	 * @author zlToxicNetherlz
	 *
	 */
	public static class PacketSocketDataHandler {
		
		@SuppressWarnings("unchecked")
		public static PacketSocketData<? extends MessagePacket> deserialize(String str) {
			try {
				
				byte [] data = Base64.getDecoder().decode(str);
		        ObjectInputStream ois = new ObjectInputStream( 
		                                        new ByteArrayInputStream(data));
		        Object o  = ois.readObject();
		        ois.close();
		        return (PacketSocketData<? extends MessagePacket>) o;
			    
			} catch (Exception e) {
				
				e.printStackTrace();
				return null;
				
			}
	    }

		public static String serialize(PacketSocketData<? extends MessagePacket> obj) {
			try {
				
				 ByteArrayOutputStream baos = new ByteArrayOutputStream();
			     ObjectOutputStream oos = new ObjectOutputStream(baos);
			     oos.writeObject(obj);
			     oos.close();
			     return Base64.getEncoder().encodeToString(baos.toByteArray());
			     
			 } catch (Exception e) {
				 
			     e.printStackTrace();
			     return null;
			     
			 }
		}
		
	}
	
}
