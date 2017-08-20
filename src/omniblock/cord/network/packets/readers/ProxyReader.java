package omniblock.cord.network.packets.readers;

import net.md_5.bungee.api.ProxyServer;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.packet.ByeProxyPacket;
import net.omniblock.packets.network.structure.packet.WelcomeProxyPacket;
import net.omniblock.packets.network.tool.object.PacketReader;

public class ProxyReader {
	
	public static void start() {
		
		Packets.READER.registerReader(new PacketReader<WelcomeProxyPacket>(){

			@SuppressWarnings("deprecation")
			@Override
			public void readPacket(PacketSocketData<WelcomeProxyPacket> packetsocketdata) {
				
				ProxyServer.getInstance().getConsole().sendMessage("Welcome OmniCore :)! Connected!");
				return;
				
			}

			@Override
			public Class<WelcomeProxyPacket> getAttachedPacketClass() {
				return WelcomeProxyPacket.class;
			}
			
		});
		
		Packets.READER.registerReader(new PacketReader<ByeProxyPacket>(){

			@SuppressWarnings("deprecation")
			@Override
			public void readPacket(PacketSocketData<ByeProxyPacket> packetsocketdata) {
				
				ProxyServer.getInstance().getConsole().sendMessage("Goodbye OmniCore :(! Disconnected!");
				return;
				
			}

			@Override
			public Class<ByeProxyPacket> getAttachedPacketClass() {
				return ByeProxyPacket.class;
			}
			
		});
		
	}
	
}
