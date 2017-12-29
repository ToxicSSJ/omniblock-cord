package omniblock.cord.network.packets.readers;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.packet.ByeProxyPacket;
import net.omniblock.packets.network.structure.packet.RequestActionExecutorPacket;
import net.omniblock.packets.network.structure.packet.WelcomeProxyPacket;
import net.omniblock.packets.network.tool.object.PacketReader;
import omniblock.cord.network.packets.PacketsTools;

public class ProxyReader {
	
	public static void start() {
		
		Packets.READER.registerReader(new PacketReader<WelcomeProxyPacket>(){

			@SuppressWarnings("deprecation")
			@Override
			public void readPacket(PacketSocketData<WelcomeProxyPacket> packetsocketdata) {
				
				ProxyServer.getInstance().getConsole().sendMessage("OmniCore conectado, Enviando petición de información a los servidores previamente registrados!");
				
				for(ServerInfo server : ProxyServer.getInstance().getServers().values()) {
					
					if(PacketsTools.SOCKET_PORTS.containsKey(server.getName())) {
						
						Packets.STREAMER.streamPacket(
								new RequestActionExecutorPacket()
									.setRequestAction("registerrequest")
									.setArgs("")
									.setRequesterPort(PacketsTools.SOCKET_PORTS.get(server.getName()))
									.build()
								);
						
					}
					
				}
				
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
				
				ProxyServer.getInstance().getConsole().sendMessage("Omnicore Desconectado!");
				return;
				
			}

			@Override
			public Class<ByeProxyPacket> getAttachedPacketClass() {
				return ByeProxyPacket.class;
			}
			
		});
		
	}
	
}
