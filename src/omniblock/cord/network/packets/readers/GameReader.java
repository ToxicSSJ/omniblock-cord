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
		
		/*
		 * 
		 * Con este reader se logra reenviar una
		 * información de inicialización de juego
		 * a cierto servidor tipo juego.
		 * 
		 */
		Packets.READER.registerReader(new PacketReader<GameInitializerInfoPacket>(){

			@Override
			public void readPacket(PacketSocketData<GameInitializerInfoPacket> packetsocketdata) {
				
				PacketStructure structure = packetsocketdata.getStructure();
				
				GamePreset gamepreset = GamePreset.valueOf(structure.get(DataType.STRINGS, "gamepreset"));
				String servername = structure.get(DataType.STRINGS, "servername");
				
				Integer socketport = structure.get(DataType.INTEGERS, "socketport");
				
				Packets.STREAMER.streamPacket(new GameInitializerInfoPacket()
            			.setGamepreset(gamepreset)
            			.setSocketport(socketport)
            			.setServername(servername)
            			.build().setReceiver(PacketsTools.SOCKET_PORTS.get(servername))
            			);
				return;
				
			}

			@Override
			public Class<GameInitializerInfoPacket> getAttachedPacketClass() {
				return GameInitializerInfoPacket.class;
			}
			
		});
		
	}
	
}
