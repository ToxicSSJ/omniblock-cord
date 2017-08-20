package net.omniblock.packets.network.socket.adapter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import net.omniblock.packets.network.socket.helper.SocketHelper;
import net.omniblock.packets.network.socket.type.DataSenderStatus;
import net.omniblock.packets.network.structure.type.PacketSenderType;

/**
 * 
 * Esta clase es el adaptador para
 * el cliente en cuanto a los sockets.
 * 
 * @author zlToxicNetherlz
 *
 */
public class ClientSocketAdapter {
	
	/**
	 * 
	 * Este metodo envia datos a un sistema remoto.
	 * 
	 * @param data Los datos que serán enviados.
	 * @param sender El tipo de receptor que se espera que lea los datos.
	 * @return El estado del envio por medio de un enumerador.
	 */
	public DataSenderStatus sendData(String data, PacketSenderType sender) {
		
		return sendData(data, SocketHelper.getReceiverPort(sender));
		
	}
	
	/**
	 * 
	 * Este metodo envia datos a un sistema remoto.
	 * 
	 * @param data Los datos que serán enviados.
	 * @param port El puerto local abierto al cual se le enviarán dichos datos.
	 * @return El estado del envio por medio de un enumerador.
	 */
	public DataSenderStatus sendData(String data, int port) {
		
		try {
			
			Socket client = new Socket("localhost", port);
			
			DataOutputStream ds = new DataOutputStream(client.getOutputStream());
			ds.writeUTF(data);
			ds.close();
			
			client.close();
			return DataSenderStatus.SENDED;
			
		} catch (UnknownHostException e1) {
			
			e1.printStackTrace();
			
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		
		return DataSenderStatus.ERROR;
		
	}
	
}
