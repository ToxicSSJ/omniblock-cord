package net.omniblock.packets.network.socket;

import net.omniblock.packets.network.socket.adapter.ClientSocketAdapter;
import net.omniblock.packets.network.socket.adapter.ServerSocketAdapter;

/**
 * 
 * Clase tipo estatica que se encarga de
 * tener las variables principales para
 * el manejo de sockets desde la parte del
 * SERVER, que es quien recibe los sockets
 * y el CLIENT que es quien los envia.
 * 
 * @author zlToxicNetherlz
 *
 */
public class Sockets {

	/**
	 * 
	 * Este adaptador es el encargado de enviar sockets
	 * y también de darle utilidad a algunos metodos
	 * importantes al momento de manejar envio de sockets.
	 * 
	 * @see ClientSocketAdapter
	 */
	public static final ClientSocketAdapter CLIENT = new ClientSocketAdapter();
	
	/**
	 * 
	 * Este adaptador es el encargado de recibir sockets
	 * y luego mandarlos a procesar por medio del handler
	 * encargado de dicho proceso.
	 * 
	 * @see ServerSocketAdapter
	 */
	public static final ServerSocketAdapter SERVER = new ServerSocketAdapter();
	
	static {
		
		SERVER.startServer();
		
	}
	
}
