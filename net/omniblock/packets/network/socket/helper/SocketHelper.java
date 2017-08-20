package net.omniblock.packets.network.socket.helper;

import java.io.IOException;
import java.net.ServerSocket;

import net.omniblock.packets.network.structure.type.PacketSenderType;

/**
 * 
 * Esta clase provee utilidades significativas
 * a los adaptadores de sockets.
 * 
 * @author zlToxicNetherlz
 *
 */
public class SocketHelper {

	/**
	 * 
	 * Puerto en donde corre el sistema (cerebro) de
	 * Omniblock Network, El cual es el OmniCore.
	 * 
	 */
	public static final int OMNICORE_SOCKET_PORT = 8000;
	
	/**
	 * 
	 * Puerto en donde corre el sistema (bungee) de
	 * Omniblock Network, El cual es el OmniCord.
	 * 
	 */
	public static final int OMNICORD_SOCKET_PORT = 8005;
	
	/**
	 * 
	 * Este metodo permite obtener el puerto en base
	 * al enumerador denominado PacketSenderType, donde
	 * de forma fácil en los PacketSocketData se pueden
	 * definir como un receptor y así hacer más amigable
	 * el envio de paquetes.
	 * 
	 * Nota: Si el sender es OmniNetwork, No se enviará
	 * el paquete, esto se debe a que se debe especificar
	 * cual es el puerto especifico donde se enviará el
	 * paquete.
	 * 
	 * @param sender El tipo de PacketSenderType que se quiere
	 * convertir a puerto (int).
	 * @return El puerto donde corre el sender.
	 */
	public static int getReceiverPort(PacketSenderType sender){
		
		if(sender == PacketSenderType.OMNICORE) return OMNICORE_SOCKET_PORT;
		if(sender == PacketSenderType.OMNICORD) return OMNICORD_SOCKET_PORT;
		
		return OMNICORE_SOCKET_PORT;
		
	}
	
	/**
	 * 
	 * Este metodo permite obtener
	 * un puerto al azar y abierto.
	 * 
	 * @return El puerto al azar y abierto.
	 */
	public static Integer getOpenPort() {
		
		ServerSocket socket = null;
		
	    try {
	    	
	    	socket= new ServerSocket(0);
	    	return socket.getLocalPort();

	    } catch(IOException e) {
	    } finally {
	    	if(socket != null){
	    		try {
					socket.close();
				} catch (IOException e) {
				}
	    	}
	    }
	    
	    return -1;
	    
	}
	
	/**
	 * 
	 * Este metodo permite saber si un puerto local
	 * está en uso.
	 * 
	 * @param port El puerto que se desa saber si se
	 * encuentra o no en uso.
	 * @return <strong>true</strong> si el puerto está en uso.
	 */
	public static boolean isLocalPortInUse(int port) {
	    try {
	    	
	        new ServerSocket(port).close();
	        return false;
	        
	    } catch(IOException e) {
	    	
	        return true;
	        
	    }
	}
	
}
