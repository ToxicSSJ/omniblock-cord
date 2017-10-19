package omniblock.cord.network.packets.readers;

import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.data.PacketStructure;
import net.omniblock.packets.network.structure.data.PacketStructure.DataType;
import net.omniblock.packets.network.structure.packet.GameInitializerInfoPacket;
import net.omniblock.packets.network.tool.object.PacketReader;
import net.omniblock.packets.object.external.GamePreset;
import omniblock.cord.network.packets.PacketsTools;

public class GameReader {

	public static void start() {
		
		Packets.READER.registerReader(new PacketReader<GameInitializerInfoPacket>(){

			@Override
			public void readPacket(PacketSocketData<GameInitializerInfoPacket> packetsocketdata) {
				
				PacketStructure structure = packetsocketdata.getStructure();
				
				String gamepreset = structure.get(DataType.STRINGS, "gamepreset");
				String servername = structure.get(DataType.STRINGS, "servername");
				String data = structure.get(DataType.STRINGS, "data");
				
				Integer socketport = structure.get(DataType.INTEGERS, "socketport");
				
				Packets.STREAMER.streamPacket(new GameInitializerInfoPacket()
						
						.setData(data)
						.setGamepreset(GamePreset.valueOf(gamepreset))
						.setServername(servername)
						.setSocketport(socketport)
						
						.build().setReceiver(PacketsTools.SOCKET_PORTS.get(servername)));
				
				return;
				
			}

			@Override
			public Class<GameInitializerInfoPacket> getAttachedPacketClass() {
				return GameInitializerInfoPacket.class;
			}
			
		});
		
	}
	
}
