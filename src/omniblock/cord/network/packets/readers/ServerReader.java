package omniblock.cord.network.packets.readers;

import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.data.PacketStructure;
import net.omniblock.packets.network.structure.data.PacketStructure.DataType;
import net.omniblock.packets.network.structure.packet.ServerSocketInfoPacket;
import net.omniblock.packets.network.tool.object.PacketReader;
import omniblock.cord.network.packets.PacketsTools;

public class ServerReader {

	public static void start() {
		
		Packets.READER.registerReader(new PacketReader<ServerSocketInfoPacket>(){

			@Override
			public void readPacket(PacketSocketData<ServerSocketInfoPacket> packetsocketdata) {
				
				PacketStructure structure = packetsocketdata.getStructure();
				
				String servername = structure.get(DataType.STRINGS, "servername");
				Integer serversocket = structure.get(DataType.INTEGERS, "serversocket");
				
				PacketsTools.SOCKET_PORTS.put(servername, serversocket);
				return;
				
			}

			@Override
			public Class<ServerSocketInfoPacket> getAttachedPacketClass() {
				return ServerSocketInfoPacket.class;
			}
			
		});
		
	}
	
}
