package omniblock.cord.util.lib.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import omniblock.cord.network.core.Packet;

/**
 * 
 * Esta clase funciona en forma de un adaptador
 * de Sockets para el BungeeCord de tal forma que
 * pueda enviar y recibir sockets. En el caso de
 * está clase y como su nombre lo dice la forma
 * del envio de sockets va de la siguiente manera:
 * <br><br>
 * <center>Envio desde el Proxy -> Servidores de la Network</center>		
 * <center>Recepción desde el Proxy -> Sistema OmniCore</center>	
 * 
 * @author zlToxicNetherlz
 *
 */
public class ProxySocketAdapter {

	public static ServerSocket serverSocket = null;
	public static Thread thread = null;
	
	/**
	 * 
	 * Metodo para iniciar el servidor de sockets
	 * para recibir paquetes desde el OmniCore.
	 * 
	 * @param port El puerto es el necesario para iniciar el servidor, si se deja en 0 se tomará uno al azar.
	 */
	public void startServer(int port) {

        Runnable serverTask = new Runnable() {
            @Override
            public void run() {
                try {
                	
                	refreshPort(port);
                    serverSocket = new ServerSocket(port);
                    
                    while (true) {
                    	
                        Socket client = serverSocket.accept();
                        
                        DataInputStream dis = new DataInputStream(client.getInputStream());
                        String data = dis.readUTF();
                        
                        Packet.HANDLER.readPacket(data);
                        
                        dis.close();
                        
                        
                    }
                    
                } catch (IOException e) {
                	
                    e.printStackTrace();
                    
                }
            }
        };
        
        thread = new Thread(serverTask);
        thread.start();
        
    }
	
	/**
	 * 
	 * Con este metodo se envian datos a un servidor por medio
	 * del puerto y la respectiva información que se dese enviar
	 * en formato String.
	 * 
	 * @param data Los datos que se desean enviar.
	 * @param port El puerto del servidor.
	 */
	public void sendData(String data, int port) {
		
		try {
			
			Socket client = new Socket("localhost", port);
			
			DataOutputStream ds = new DataOutputStream(client.getOutputStream());
			ds.writeUTF(data);
			ds.close();
			
			client.close();
			return;
			
		} catch (UnknownHostException e1) {
			
			e1.printStackTrace();
			
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		
		return;
		
	}
	
	/**
	 * 
	 * Con este metodo se verifica si internamente hay un puerto en
	 * uso, Este metodo está con el fin de verficar si los puertos
	 * por default (OmniCore, Default Server) Está en uso y/o están
	 * abiertos para enviar datos.
	 * 
	 * @param port El puerto del servidor que se desea comprobar.
	 * @return <strong>true</strong> si el servidor está abierto.
	 */
	public boolean isLocalPortInUse(int port) {
	    try {
	    	
	        new ServerSocket(port).close();
	        return false;
	        
	    } catch(IOException e) {
	    	
	        return true;
	        
	    }
	}
	
	/**
	 * 
	 * Con este metodo se puede "refrescar" el puerto
	 * especificado con el fin de reutilizarlo debido
	 * a que por alguna razón dicho puerto puede estár
	 * ocupado por algun crash o por algun bug en el sistema
	 * entonces es de prioridad que una vez se desea iniciar
	 * el sistema o cualquier servidor se compruebe
	 * que está abierto y funcionando para ser cerrado.
	 * 
	 * @param port El puerto que se desea refrescar.
	 */
	private void refreshPort(int port) {
		
		ServerSocket socket = null;
		
	    try {
	    	
	        socket = new ServerSocket(port);
	        
	    } catch (IOException e) {
	    } finally {
	    	
	        if (socket != null) {
	        	try { socket.close(); } 
	        	catch (IOException e) { }
	        }
	        
	    }
	    
	}
	
}
