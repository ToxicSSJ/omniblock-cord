package omniblock.cord.network.packets.readers;

import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.data.PacketStructure;
import net.omniblock.packets.network.structure.data.PacketStructure.DataType;
import net.omniblock.packets.network.structure.packet.PlayerLoginEvaluatePacket;
import net.omniblock.packets.network.structure.packet.PlayerLoginSucessPacket;
import net.omniblock.packets.network.structure.packet.PlayerSendBanPacket;
import net.omniblock.packets.network.structure.packet.PlayerSendKickPacket;
import net.omniblock.packets.network.structure.packet.PlayerSendMessagePacket;
import net.omniblock.packets.network.structure.packet.PlayerSendTexturepackPacket;
import net.omniblock.packets.network.structure.packet.PlayerSendToServerPacket;
import net.omniblock.packets.network.tool.object.PacketReader;
import omniblock.cord.network.packets.PacketsTools;

public class PlayerReader {

	public static void start() {
		
		Packets.READER.registerReader(new PacketReader<PlayerSendMessagePacket>(){

			@Override
			public void readPacket(PacketSocketData<PlayerSendMessagePacket> packetsocketdata) {
				
				PacketStructure structure = packetsocketdata.getStructure();
				
				String playername = structure.get(DataType.STRINGS, "playername");
				String message = structure.get(DataType.STRINGS, "message");
				
				PacketsTools.sendMessage2Player(playername, message);
				return;
				
			}

			@Override
			public Class<PlayerSendMessagePacket> getAttachedPacketClass() {
				return PlayerSendMessagePacket.class;
			}
			
		});
		
		Packets.READER.registerReader(new PacketReader<PlayerSendToServerPacket>(){

			@Override
			public void readPacket(PacketSocketData<PlayerSendToServerPacket> packetsocketdata) {
				
				PacketStructure structure = packetsocketdata.getStructure();
				
				String playername = structure.get(DataType.STRINGS, "playername");
				String servername = structure.get(DataType.STRINGS, "servername");
				Boolean party = structure.get(DataType.BOOLEANS, "party");
				
				PacketsTools.sendPlayer2Server(playername, servername, party);
				return;
				
			}

			@Override
			public Class<PlayerSendToServerPacket> getAttachedPacketClass() {
				return PlayerSendToServerPacket.class;
			}
			
		});
		
		Packets.READER.registerReader(new PacketReader<PlayerSendTexturepackPacket>(){

			@Override
			public void readPacket(PacketSocketData<PlayerSendTexturepackPacket> packetsocketdata) {
				
				PacketStructure structure = packetsocketdata.getStructure();
				
				String playername = structure.get(DataType.STRINGS, "playername");
				String texturehash = structure.get(DataType.STRINGS, "texturehash");
				
				PacketsTools.sendPack2Player(playername, texturehash);
				return;
				
			}

			@Override
			public Class<PlayerSendTexturepackPacket> getAttachedPacketClass() {
				return PlayerSendTexturepackPacket.class;
			}
			
		});
		
		Packets.READER.registerReader(new PacketReader<PlayerSendBanPacket>(){

			@Override
			public void readPacket(PacketSocketData<PlayerSendBanPacket> packetsocketdata) {
				
				PacketStructure structure = packetsocketdata.getStructure();
				
				String playername = structure.get(DataType.STRINGS, "playername");
				
				PacketsTools.sendBan2Player(playername);
				return;
				
			}

			@Override
			public Class<PlayerSendBanPacket> getAttachedPacketClass() {
				return PlayerSendBanPacket.class;
			}
			
		});
		
		Packets.READER.registerReader(new PacketReader<PlayerSendKickPacket>(){

			@Override
			public void readPacket(PacketSocketData<PlayerSendKickPacket> packetsocketdata) {
				
				PacketStructure structure = packetsocketdata.getStructure();
				
				String playername = structure.get(DataType.STRINGS, "playername");
				String moderator = structure.get(DataType.STRINGS, "moderator");
				String reason = structure.get(DataType.STRINGS, "reason");
				
				PacketsTools.sendKick2Player(playername, moderator, reason);
				return;
				
			}

			@Override
			public Class<PlayerSendKickPacket> getAttachedPacketClass() {
				return PlayerSendKickPacket.class;
			}
			
		});
		
		Packets.READER.registerReader(new PacketReader<PlayerLoginEvaluatePacket>(){

			@Override
			public void readPacket(PacketSocketData<PlayerLoginEvaluatePacket> packetsocketdata) {
				
				PacketStructure structure = packetsocketdata.getStructure();
				
				String playername = structure.get(DataType.STRINGS, "playername");
				Boolean iplogin = structure.get(DataType.BOOLEANS, "iplogin");
				
				PacketsTools.sendAuthEvaluate2Player(playername, iplogin);
				return;
				
			}

			@Override
			public Class<PlayerLoginEvaluatePacket> getAttachedPacketClass() {
				return PlayerLoginEvaluatePacket.class;
			}
			
		});
		
		Packets.READER.registerReader(new PacketReader<PlayerLoginSucessPacket>(){

			@Override
			public void readPacket(PacketSocketData<PlayerLoginSucessPacket> packetsocketdata) {
				
				PacketStructure structure = packetsocketdata.getStructure();
				
				String playername = structure.get(DataType.STRINGS, "playername");
				Boolean iplogin = structure.get(DataType.BOOLEANS, "iplogin");
				
				PacketsTools.registerAuthSucess2Player(playername, iplogin);
				return;
				
			}

			@Override
			public Class<PlayerLoginSucessPacket> getAttachedPacketClass() {
				return PlayerLoginSucessPacket.class;
			}
			
		});
		
	}
	
}
