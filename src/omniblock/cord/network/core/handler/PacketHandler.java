package omniblock.cord.network.core.handler;

import net.omniblock.core.protocol.manager.network.handler.modifier.PacketModifier;
import net.omniblock.core.protocol.manager.network.handler.modifier.PacketModifier.PacketModifierHandler;
import omniblock.cord.OmniCord;
import omniblock.cord.util.lib.omnicore.GamePreset;
import omniblock.cord.util.lib.omnicore.MessageType;

public class PacketHandler {

	@SuppressWarnings("deprecation")
	public void readPacket(String unserialized_packetmodifier) {
		
		PacketModifier modifier = PacketModifierHandler.deserialize(unserialized_packetmodifier);
		String message_type = modifier.getString(0);
		
		if(message_type.equalsIgnoreCase(MessageType.PLAYER_SEND_TO_SERVER.getKey())) {
			
			String player = modifier.getString(1);
			String servername = modifier.getString(2);
			Boolean party = modifier.getBoolean(0);
			
			PacketProcessorHandler.sendPlayer2Server(player, servername, party);
			return;
			
		} else if(message_type.equalsIgnoreCase(MessageType.SERVER_SOCKET_INFO.getKey())) {
			
			String servername = modifier.getString(1);
			Integer socket = modifier.getInteger(0);
			
			PacketProcessorHandler.SOCKET_PORTS.put(servername, socket);
			return;
			
		} else if(message_type.equalsIgnoreCase(MessageType.PLAYER_SEND_MESSAGE.getKey())) {
			
			String player = modifier.getString(1);
			String message = modifier.getString(2);
			
			PacketProcessorHandler.sendMessage2Player(player, message);
			return;
			
		} else if(message_type.equalsIgnoreCase(MessageType.PLAYER_SEND_TEXTUREPACK.getKey())) {

			String player = modifier.getString(1);
			String name = modifier.getString(2);
			
			PacketProcessorHandler.sendPack2Player(player, name);
			return;
			
		} else if(message_type.equalsIgnoreCase(MessageType.REQUEST_PLAYER_GAME_LOBBY_SERVERS.getKey())) {
			
			String player = modifier.getString(1);
			String data = modifier.getString(2);
			
			PacketProcessorHandler.sendLobbies2Player(player, data);
			return;
			
		} else if(message_type.equalsIgnoreCase(MessageType.PLAYER_SEND_BAN.getKey())) {

			String player = modifier.getString(1);
			
			PacketProcessorHandler.sendBan2Player(player);
			return;
			
		} else if(message_type.equalsIgnoreCase(MessageType.PLAYER_SEND_KICK.getKey())) {
			
			String player = modifier.getString(1);
			String mod = modifier.getString(2);
			String reason = modifier.getString(3);
			
			PacketProcessorHandler.sendKick2Player(player, mod, reason);
			return;
			
		} else if(message_type.equalsIgnoreCase(MessageType.SERVER_RELOAD_INFO.getKey())) {
			
			Integer socketport = modifier.getInteger(0);
			
			PacketProcessorHandler.sendData2Server(MessageType.SERVER_RELOAD_INFO.getKey(), MessageType.SERVER_RELOAD_INFO.getKey(), socketport);
			return;
			
		} else if(message_type.equalsIgnoreCase(MessageType.INITIALIZE_GAME.getKey())) {
			
			Integer socketport = modifier.getInteger(0);
			
			GamePreset preset = GamePreset.getGamePreset(modifier.getString(2));
			
			PacketProcessorHandler.sendData2Server(preset.toString(),
					 							   MessageType.INITIALIZE_GAME.getKey(),
					 							   socketport);
			return;
			
		} else if(message_type.equalsIgnoreCase(MessageType.WELCOME_PROXY.getKey())) {
			
			OmniCord.getPlugin().getProxy().getConsole().sendMessage("Welcome OmniCore :)! Connected!");
			return;
			
		} else if(message_type.equalsIgnoreCase(MessageType.BYE_PROXY.getKey())) {
			
			OmniCord.getPlugin().getProxy().getConsole().sendMessage("Goodbye OmniCore :(! Disconnected!");
			return;
			
		}
		
	}
	
}
