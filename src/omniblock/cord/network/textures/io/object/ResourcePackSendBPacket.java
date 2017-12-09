package omniblock.cord.network.textures.io.object;

import java.beans.ConstructorProperties;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.logging.Level;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.connection.DownstreamBridge;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.PacketWrapper;
import omniblock.cord.OmniCord;
import omniblock.cord.network.textures.io.BungeeResourcepacks;
import omniblock.cord.network.textures.io.packets.ResourcePackSendPacket;
import omniblock.cord.util.lib.textures.ResourcePack;

public class ResourcePackSendBPacket extends DefinedPacket {

	private String url;
	private String hash;
	
	@ConstructorProperties({"url", "hash"})
    public ResourcePackSendBPacket(String url) {
        this.url = url;
        this.hash = Hashing.sha1().hashString(url, Charsets.UTF_8).toString().toLowerCase();
    }
	
	@Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        if(handler instanceof DownstreamBridge) {
            DownstreamBridge bridge = (DownstreamBridge) handler;
            try {
                Field con = bridge.getClass().getDeclaredField("con");
                con.setAccessible(true);
                try {
                    UserConnection usercon = (UserConnection) con.get(bridge);
                    relayPacket(usercon, new PacketWrapper(this, Unpooled.copiedBuffer(ByteBuffer.allocate(Integer.toString(this.getUrl().length()).length()))));
                } catch (IllegalAccessException e) {
                	OmniCord.getInstance().getLogger().log(Level.WARNING, "Sorry but you are not allowed to do this.");
                    e.printStackTrace();
                }
            } catch (NoSuchFieldException e) {
            	OmniCord.getInstance().getLogger().log(Level.SEVERE, "Error while trying to get the UserConnection field from the DownstreamBridge object. Is the plugin up to date?");
            }
        } else {
            throw new UnsupportedOperationException("Only players can receive ResourcePackSend packets!");
        }
    }

    public void relayPacket(UserConnection usercon, PacketWrapper packet) throws Exception {
        BungeeResourcepacks plugin = BungeeResourcepacks.getInstance();
        
        ResourcePack pack = plugin.getPackManager().getByHash(getHash());
        if (pack == null) {
            pack = plugin.getPackManager().getByUrl(getUrl());
        }
        if (pack == null) {
            pack = new ResourcePack("backend-" + getUrl().substring(getUrl().lastIndexOf('/') + 1, getUrl().length()).replace(".zip", "").toLowerCase(), getUrl(), getHash());
            try {
                plugin.getPackManager().addPack(pack);
            } catch (IllegalArgumentException e) {
                // Can only happen when pack was gotten by hash but another pack had the same url
                pack = plugin.getPackManager().getByUrl(getUrl());
            }
        }
        plugin.setBackend(usercon.getUniqueId());
        OmniCord.getInstance().getLogger().log(BungeeResourcepacks.getInstance().getLogLevel(), "Backend mc server send pack " + pack.getName() + " (" + pack.getUrl() + ") to player " + usercon.getName());
        plugin.getUserManager().setUserPack(usercon.getUniqueId(), pack);
        
    }
	
    public void read(ByteBuf buf) {
        this.url = readString(buf);
        try {
            this.hash = readString(buf);
        } catch (IndexOutOfBoundsException ignored) {} // No hash
    }

    public void write(ByteBuf buf) {
        writeString(this.url, buf);
        writeString(this.hash, buf);
    }

    public String getUrl() {
        return this.url;
    }

    public String getHash() {
        return this.hash;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHash(String hash) {
        if(hash != null) {
            this.hash = hash.substring(0, 39).toLowerCase();
        } else {
            this.hash = Hashing.sha1().hashString(this.getUrl(), Charsets.UTF_8).toString().substring(0, 39).toLowerCase();
        }
    }

    public String toString() {
        return "ResourcePackSend(url=" + this.getUrl() + ", hash=" + this.getHash() + ")";
    }

    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        } else if(obj instanceof ResourcePackSendPacket) {
            ResourcePackSendPacket other = (ResourcePackSendPacket)obj;
            String this$url = this.getUrl();
            String other$url = other.getUrl();
            if(this$url == null && other$url == null) {
                return true;
            }
            if(this$url == null || other$url == null) {
                return false;
            }
            if(!this$url.equals(other$url)) {
                return false;
            }
            String this$hash = this.getHash();
            String other$hash = other.getHash();

            if(this$hash == null && other$hash == null) {
                return true;
            }
            if(this$hash == null || other$hash == null) {
                return false;
            }
            return this$hash.equals(other$hash);
        }
        return false;
    }

    public int hashCode() {
        int result = 1;
        String $url = this.getUrl();
        result = result * 59 + ($url == null?0:$url.hashCode());
        String $hash = this.getHash();
        result = result * 59 + ($hash == null?0:$hash.hashCode());
        return result;
    }
	
}
