package omniblock.cord.network.packets.readers;

import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.data.PacketStructure;
import net.omniblock.packets.network.structure.data.PacketStructure.DataType;
import net.omniblock.packets.network.structure.packet.RequestBoostedGamesPacket;
import net.omniblock.packets.network.structure.packet.RequestPlayerGameLobbyServersPacket;
import net.omniblock.packets.network.structure.packet.RequestPlayerStartNetworkBoosterPacket;
import net.omniblock.packets.network.tool.object.PacketReader;
import omniblock.cord.network.packets.PacketsTools;

public class RequestReader {

	public static void start() {
		
		Packets.READER.registerReader(new PacketReader<RequestPlayerStartNetworkBoosterPacket>(){

			@Override
			public void readPacket(PacketSocketData<RequestPlayerStartNetworkBoosterPacket> packetsocketdata) {
				
				PacketStructure structure = packetsocketdata.getStructure();
				
				String playername = structure.get(DataType.STRINGS, "playername");
				String gametype = structure.get(DataType.STRINGS, "gametype");
				String key = structure.get(DataType.STRINGS, "key");
				
				Integer duration = structure.get(DataType.INTEGERS, "duration");
				
				PacketsTools.promptNetworkBooster2Player(playername, key, gametype, duration);
				return;
				
			}

			@Override
			public Class<RequestPlayerStartNetworkBoosterPacket> getAttachedPacketClass() {
				return RequestPlayerStartNetworkBoosterPacket.class;
			}
			
		});
		
		Packets.READER.registerReader(new PacketReader<RequestPlayerGameLobbyServersPacket>(){

			@Override
			public void readPacket(PacketSocketData<RequestPlayerGameLobbyServersPacket> packetsocketdata) {
				
				PacketStructure structure = packetsocketdata.getStructure();
				
				String playername = structure.get(DataType.STRINGS, "playername");
				String data = structure.get(DataType.STRINGS, "data");
				
				PacketsTools.sendLobbies2Player(playername, data);
				return;
				
			}

			@Override
			public Class<RequestPlayerGameLobbyServersPacket> getAttachedPacketClass() {
				return RequestPlayerGameLobbyServersPacket.class;
			}
			
		});
		
		Packets.READER.registerReader(new PacketReader<RequestBoostedGamesPacket>(){

			@Override
			public void readPacket(PacketSocketData<RequestBoostedGamesPacket> packetsocketdata) {
				
				PacketStructure structure = packetsocketdata.getStructure();
				
				String servername = structure.get(DataType.STRINGS, "servername");
				
				PacketsTools.sendBoostedGames2Server(servername);
				return;
				
			}

			@Override
			public Class<RequestBoostedGamesPacket> getAttachedPacketClass() {
				return RequestBoostedGamesPacket.class;
			}
			
		});
		
	}
	
}
