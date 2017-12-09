package omniblock.cord.network.textures.io;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import omniblock.cord.network.textures.io.object.ResourcePackSendBPacket;
import omniblock.cord.util.lib.textures.ResourcePack;

public class ResourcePackHandler {

	public static void sendPacket(ProxiedPlayer player, TextureType type) {
		
		player.unsafe().sendPacket(new ResourcePackSendBPacket(type.getPack().getUrl()));
		sendPacketInfo(player, type.getPack());
		return;
		
	}
	
	@SuppressWarnings("unused")
	private static void sendPacketInfo(ProxiedPlayer player, ResourcePack pack) {
		
		if (player.getServer() == null) {
            return;
        }
		
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        
        if(pack != null) {
        	
            out.writeUTF("packChange");
            out.writeUTF(player.getName());
            out.writeUTF(pack.getName());
            out.writeUTF(pack.getUrl());
            out.writeUTF(pack.getHash());
            
        } else {
        	
            out.writeUTF("clearPack");
            out.writeUTF(player.getName());
            
        }
        
        player.getServer().sendData("Resourcepack", out.toByteArray());
	}
	
}
