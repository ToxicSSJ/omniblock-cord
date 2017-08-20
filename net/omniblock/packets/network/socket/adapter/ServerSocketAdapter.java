package net.omniblock.packets.network.socket.adapter;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.socket.helper.SocketHelper;
import net.omniblock.packets.network.structure.data.PacketSocketData.PacketSocketDataHandler;

/**
 * 
 * Este es el adaptador para el servidor
 * en cuanto a los sockets.
 * 
 * @author zlToxicNetherlz
 *
 */
public class ServerSocketAdapter {

	/**
	 * 
	 * La instancia estatica del ServerSocket
	 * donde se pueden manejar distintos metodos
	 * proveidos por java.
	 * 
	 */
	public static ServerSocket serverSocket = null;
	
	/**
	 * 
	 * La variable estatica del puerto en donde
	 * corre el servidor en el momento que se
	 * pide.
	 * 
	 */
	public static Integer serverPort = null;
	
	/**
	 * 
	 * El Thread en donde corre todo el sistema
	 * del servidor de sockets.
	 * 
	 */
	public static Thread thread = null;
	
	/**
	 * 
	 * Este metodo inicializa el servidor de sockets
	 * creando las variables principales y escogiendo
	 * un puerto al azar.
	 * 
	 */
	public void startServer() {
		
		startServer(SocketHelper.getOpenPort());
    	
        
    }
	
	/**
	 * 
	 * Este metodo inicializa el servidor de sockets
	 * creando las variables principales pero en este
	 * caso se inicia desde el puerto dado por el primer
	 * argumento.
	 * 
	 * @param port El puerto sobre el cual correrá el servidor (debe estár
	 * abierto).
	 */
	public void startServer(int port) {
		
        final ExecutorService clientprocessor = Executors
                .newFixedThreadPool(10);

        Runnable serverTask = new Runnable() {
            @Override
            public void run() {
                try {
                	
                	serverPort = port;
                    serverSocket = new ServerSocket(port);
                    
                    while (true) {
                    	
                        Socket clientSocket = serverSocket.accept();
                        
                        clientprocessor
                                .submit(new SocketAdapter(clientSocket));
                        
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
	 * Esta clase es la que se encarga de correr
	 * el procesamiento del socket en un thread
	 * ejecutado de por sí en la Pool.
	 * 
	 * 
	 * @author zlToxicNetherlz
	 *
	 */
    private class SocketAdapter implements Runnable {
    	
        private final Socket clientSocket;
        
        /**
         * 
         * Constructor de la clase
         * 
         * @param clientSocket El socket que será analizado y procesado.
         */
        private SocketAdapter(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
        	
            try {
            	
            	DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                String clientData = dis.readUTF();
                
                handlePacket(clientData);
                
                clientSocket.close();
                return;
                
            } catch (IOException e) { return; }
            
        }
        
        /**
         * 
         * Este metodo procesa la data recibida
         * evadiendo el procesamiento en el caso
         * de que los datos sean nulos o iguales a
         * básicamente nada.
         * 
         * @param data Los datos leídos del socket.
         */
    	public void handlePacket(String data) {
    		
    		if(data != null && data != "") {
				
				Packets.READER.startRead(PacketSocketDataHandler.deserialize(data));
				return;
				
			}
    		
    	}
        
    }
	
}
