package omniblock.cord.util.lib.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import omniblock.cord.network.core.Packet;

public class ProxySocketAdapter {

	public static ServerSocket serverSocket = null;
	public static Thread thread = null;
	
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
	
	public boolean isLocalPortInUse(int port) {
	    try {
	    	
	        new ServerSocket(port).close();
	        return false;
	        
	    } catch(IOException e) {
	    	
	        return true;
	        
	    }
	}
	
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
