package omniblock.cord.network.textures.io;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.BadPacketException;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants;
import omniblock.cord.OmniCord;
import omniblock.cord.network.textures.io.events.ResourcePackSelectEvent;
import omniblock.cord.network.textures.io.events.ResourcePackSendEvent;
import omniblock.cord.network.textures.io.listeners.DisconnectListener;
import omniblock.cord.network.textures.io.packets.ResourcePackSendPacket;
import omniblock.cord.util.lib.textures.PackManager;
import omniblock.cord.util.lib.textures.ResourcePack;
import omniblock.cord.util.lib.textures.ResourcepacksPlayer;
import omniblock.cord.util.lib.textures.ResourcepacksPlugin;
import omniblock.cord.util.lib.textures.UserManager;
import omniblock.cord.util.lib.textures.events.IResourcePackSelectEvent;
import omniblock.cord.util.lib.textures.events.IResourcePackSendEvent;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Created by Phoenix616 on 18.03.2015.
 */
public class BungeeResourcepacks implements ResourcepacksPlugin {

    private static BungeeResourcepacks instance;
    
    private static PackManager pm;

    private static UserManager um;
    
    private static Level loglevel = Level.INFO;

    /**
     * Set of uuids of players which got send a pack by the backend server. 
     * This is needed so that the server does not send the bungee pack if the user has a backend one.
     */
    private Map<UUID, Boolean> backendPackedPlayers = new ConcurrentHashMap<>();

    /**
     * Set of uuids of players which were authenticated by a backend server's plugin
     */
    private Set<UUID> authenticatedPlayers = new HashSet<>();

    private static int bungeeVersion;

    @SuppressWarnings("unchecked")
	public static void start() {
    	
        instance = new BungeeResourcepacks();
        pm = new PackManager(instance);
        um = new UserManager(instance);
        
        for(TextureType textures : TextureType.values()) {
        	
        	pm.addPack(textures.getPack());
        	continue;
        	
        }
        
        try {
        	
            List<Integer> supportedVersions = new ArrayList<>();
            
            try {
                Field svField = Protocol.class.getField("supportedVersions");
                supportedVersions = (List<Integer>) svField.get(null);
            } catch(Exception e1) {
                // Old bungee protocol version, try new one
            }
            if(supportedVersions.size() == 0) {
                Field svIdField = ProtocolConstants.class.getField("SUPPORTED_VERSION_IDS");
                supportedVersions = (List<Integer>) svIdField.get(null);
            }

            bungeeVersion = supportedVersions.get(supportedVersions.size() - 1);
            
            if(bungeeVersion == ProtocolConstants.MINECRAFT_1_8) {
            	OmniCord.getInstance().getLogger().log(Level.INFO, "BungeeCord 1.8 detected!");
                Method reg = Protocol.DirectionData.class.getDeclaredMethod("registerPacket", int.class, Class.class);
                reg.setAccessible(true);
                reg.invoke(Protocol.GAME.TO_CLIENT, 0x48, ResourcePackSendPacket.class);
            } else if(bungeeVersion >= ProtocolConstants.MINECRAFT_1_9 && bungeeVersion < ProtocolConstants.MINECRAFT_1_9_4){
            	OmniCord.getInstance().getLogger().log(Level.INFO, "BungeeCord 1.9-1.9.3 detected!");
                Method reg = Protocol.DirectionData.class.getDeclaredMethod("registerPacket", int.class, int.class, Class.class);
                reg.setAccessible(true);
                reg.invoke(Protocol.GAME.TO_CLIENT, 0x48, 0x32, ResourcePackSendPacket.class);
            } else if(bungeeVersion >= ProtocolConstants.MINECRAFT_1_9_4){
            	OmniCord.getInstance().getLogger().log(Level.INFO, "BungeeCord 1.9.4+ detected!");
                Method map = Protocol.class.getDeclaredMethod("map", int.class, int.class);
                map.setAccessible(true);
                List<Object> mappings = new ArrayList<>();
                mappings.add(map.invoke(null, ProtocolConstants.MINECRAFT_1_8, 0x48));
                mappings.add(map.invoke(null, ProtocolConstants.MINECRAFT_1_9, 0x32));
                if (ProtocolConstants.SUPPORTED_VERSION_IDS.contains(ProtocolConstants.MINECRAFT_1_12)) {
                    mappings.add(map.invoke(null, ProtocolConstants.MINECRAFT_1_12, 0x33));
                }
                if (ProtocolConstants.SUPPORTED_VERSION_IDS.contains(ProtocolConstants.MINECRAFT_1_12_1)) {
                    mappings.add(map.invoke(null, ProtocolConstants.MINECRAFT_1_12_1, 0x34));
                }
                Object mappingsObject = Array.newInstance(mappings.get(0).getClass(), mappings.size());
                for (int i = 0; i < mappings.size(); i++) {
                    Array.set(mappingsObject, i, mappings.get(i));
                }
                Object[] mappingsArray = (Object[]) mappingsObject;
                Method reg = Protocol.DirectionData.class.getDeclaredMethod("registerPacket", Class.class, mappingsArray.getClass());
                reg.setAccessible(true);
                try {
                    reg.invoke(Protocol.GAME.TO_CLIENT, ResourcePackSendPacket.class, mappingsArray);
                } catch (Throwable t) {
                	OmniCord.getInstance().getLogger().log(Level.SEVERE, "Protocol version " + bungeeVersion + " is not supported! Please look for an update!");
                    return;
                }
            } else {
            	OmniCord.getInstance().getLogger().log(Level.SEVERE, "Unsupported BungeeCord version (" + bungeeVersion + ") found! You need at least 1.8 for this plugin to work!");
                return;
            }

            um = new UserManager(instance);

            OmniCord.getInstance().getProxy().getPluginManager().registerListener(OmniCord.getInstance(), new DisconnectListener(instance));
            
            OmniCord.getInstance().getProxy().registerChannel("Resourcepack");

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
        	OmniCord.getInstance().getLogger().log(Level.SEVERE, "Couldn't find the registerPacket method in the Protocol.DirectionData class! Please update this plugin or downgrade BungeeCord!");
            e.printStackTrace();
        } catch(NoSuchFieldException e) {
        	OmniCord.getInstance().getLogger().log(Level.SEVERE, "Couldn't find the field with the supported versions! Please update this plugin or downgrade BungeeCord!");
            e.printStackTrace();
        }
    }

    public static BungeeResourcepacks getInstance() {
        return instance;
    }
    
    /**
     * Resends the pack that corresponds to the player's server
     * @param player The player to set the pack for
     */
    public void resendPack(ProxiedPlayer player) {
        String serverName = "";
        if(player.getServer() != null) {
            serverName = player.getServer().getInfo().getName();
        }
        getPackManager().applyPack(player.getName(), player.getUniqueId(), serverName);
    }

    public void resendPack(UUID playerId) {
        ProxiedPlayer player = OmniCord.getInstance().getProxy().getPlayer(playerId);
        if(player != null) {
            resendPack(player);
        }
    }
    
    /**
     * Send a resourcepack to a connected player
     * @param player The ProxiedPlayer to send the pack to
     * @param pack The resourcepack to send the pack to
     */
    protected void sendPack(ProxiedPlayer player, ResourcePack pack) {
        int clientVersion = player.getPendingConnection().getVersion();
        if(clientVersion >= ProtocolConstants.MINECRAFT_1_8) {
            try {
                ResourcePackSendPacket packet = new ResourcePackSendPacket(pack.getUrl(), pack.getHash());
                player.unsafe().sendPacket(packet);
                sendPackInfo(player, pack);
                OmniCord.getInstance().getLogger().log(getLogLevel(), "Send pack " + pack.getName() + " (" + pack.getUrl() + ") to " + player.getName());
            } catch(BadPacketException e) {
            	OmniCord.getInstance().getLogger().log(Level.SEVERE, e.getMessage() + " Please check for updates!");
            } catch(ClassCastException e) {
            	OmniCord.getInstance().getLogger().log(Level.SEVERE, "Packet defined was not ResourcePackSendPacket? Please check for updates!");
            }
        } else {
        	OmniCord.getInstance().getLogger().log(Level.WARNING, "Cannot send the pack " + pack.getName() + " (" + pack.getUrl() + ") to " + player.getName() + " as he uses the unsupported protocol version " + clientVersion + "!");
            OmniCord.getInstance(). getLogger().log(Level.WARNING, "Consider blocking access to your server for clients with version under 1.8 if you want this plugin to work for everyone!");
        }
    }

    /**
      * <p>Send a plugin message to the server the player is connected to!</p>
      * <p>Channel: Resourcepack</p>
      * <p>sub-channel: packChange</p>
      * <p>arg1: player.getName()</p>
      * <p>arg2: pack.getName();</p>
      * <p>arg3: pack.getUrl();</p>
      * <p>arg4: pack.getHash();</p>
      * @param player The player to update the pack on the player's bukkit server
      * @param pack The ResourcePack to send the info of the the Bukkit server, null if you want to clear it!
      */
    public void sendPackInfo(ProxiedPlayer player, ResourcePack pack) {
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

    public void setPack(String playername, UUID playerId, ResourcePack pack) {
        getPackManager().setPack(playername, playerId, pack);
    }

    public void sendPack(UUID playerId, ResourcePack pack) {
        ProxiedPlayer player = OmniCord.getInstance().getProxy().getPlayer(playerId);
        if(player != null) {
            sendPack(player, pack);
        }
    }

    public void clearPack(ProxiedPlayer player) {
        getUserManager().clearUserPack(player.getUniqueId());
        sendPackInfo(player, null);
    }

    public void clearPack(UUID playerId) {
        getUserManager().clearUserPack(playerId);
        ProxiedPlayer player = OmniCord.getInstance().getProxy().getPlayer(playerId);
        if (player != null) {
            sendPackInfo(player, null);
        }
    }

    public PackManager getPackManager() {
        return pm;
    }

    public UserManager getUserManager() {
        return um;
    }

    /**
     * Add a player's UUID to the list of players with a backend pack
     * @param playerId The uuid of the player
     */
    public void setBackend(UUID playerId) {
        backendPackedPlayers.put(playerId, false);
    }

    /**
     * Remove a player's UUID from the list of players with a backend pack
     * @param playerId The uuid of the player
     */
    public void unsetBackend(UUID playerId) {
        backendPackedPlayers.remove(playerId);
    }

    /**
     * Check if a player has a pack set by a backend server
     * @param playerId The UUID of the player
     * @return If the player has a backend pack
     */
    public boolean hasBackend(UUID playerId) {
        return backendPackedPlayers.containsKey(playerId);
    }

    public String getMessage(String key) {
        return "";
    }

    public String getMessage(String key, Map<String, String> replacements) {
        String msg = getMessage(key);
        if (replacements != null) {
            for(Map.Entry<String, String> repl : replacements.entrySet()) {
                msg = msg.replace("%" + repl.getKey() + "%", repl.getValue());
            }
        }
        return msg;
    }

    public Level getLogLevel() {
        return loglevel;
    }

    @Override
    public ResourcepacksPlayer getPlayer(UUID playerId) {
        ProxiedPlayer player = OmniCord.getInstance().getProxy().getPlayer(playerId);
        if(player != null) {
            return new ResourcepacksPlayer(player.getName(), player.getUniqueId());
        }
        return null;
    }

    @Override
    public ResourcepacksPlayer getPlayer(String playerName) {
        ProxiedPlayer player = OmniCord.getInstance().getProxy().getPlayer(playerName);
        if(player != null) {
            return new ResourcepacksPlayer(player.getName(), player.getUniqueId());
        }
        return null;
    }

    @Override
    public boolean sendMessage(ResourcepacksPlayer player, String message) {
        return sendMessage(player, Level.INFO, message);
    }

    @Override
    public boolean sendMessage(ResourcepacksPlayer player, Level level, String message) {
        if(player != null) {
            ProxiedPlayer proxyPlayer = OmniCord.getInstance().getProxy().getPlayer(player.getUniqueId());
            if(proxyPlayer != null) {
                proxyPlayer.sendMessage(TextComponent.fromLegacyText(message));
                return true;
            }
        } else {
        	OmniCord.getInstance().getLogger().log(level, message);
        }
        return false;
    }

    @Override
     public boolean checkPermission(ResourcepacksPlayer player, String perm) {
        // Console
        if(player == null)
            return true;
        return checkPermission(player.getUniqueId(), perm);

    }

    @Override
    public boolean checkPermission(UUID playerId, String perm) {
        ProxiedPlayer proxiedPlayer = OmniCord.getInstance().getProxy().getPlayer(playerId);
        if(proxiedPlayer != null) {
            return proxiedPlayer.hasPermission(perm);
        }
        return false;

    }

    @Override
    public int getPlayerPackFormat(UUID playerId) {
        ProxiedPlayer proxiedPlayer = OmniCord.getInstance().getProxy().getPlayer(playerId);
        if(proxiedPlayer != null) {
            return getPackManager().getPackFormat(proxiedPlayer.getPendingConnection().getVersion());
        }
        return -1;
    }

    @Override
    public IResourcePackSelectEvent callPackSelectEvent(UUID playerId, ResourcePack pack, IResourcePackSelectEvent.Status status) {
        ResourcePackSelectEvent selectEvent = new ResourcePackSelectEvent(playerId, pack, status);
        OmniCord.getInstance().getProxy().getPluginManager().callEvent(selectEvent);
        return selectEvent;
    }

    @Override
    public IResourcePackSendEvent callPackSendEvent(UUID playerId, ResourcePack pack) {
        ResourcePackSendEvent sendEvent = new ResourcePackSendEvent(playerId, pack);
        OmniCord.getInstance().getProxy().getPluginManager().callEvent(sendEvent);
        return sendEvent;
    }

    @Override
    public int runTask(Runnable runnable) {
        return OmniCord.getInstance().getProxy().getScheduler().schedule(OmniCord.getInstance(), runnable, 0, TimeUnit.MICROSECONDS).getId();
    }

    @Override
    public int runAsyncTask(Runnable runnable) {
        return OmniCord.getInstance().getProxy().getScheduler().runAsync(OmniCord.getInstance(), runnable).getId();
    }

    public void setAuthenticated(UUID playerId, boolean b) {
        if(b) {
            authenticatedPlayers.add(playerId);
        } else {
            authenticatedPlayers.remove(playerId);
        }
    }

    public int getBungeeVersion() {
        return bungeeVersion;
    }

	@Override
	public boolean isAuthenticated(UUID playerId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setStoredPack(UUID playerId, String packName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getStoredPack(UUID playerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUsepackTemporary() {
		// TODO Auto-generated method stub
		return false;
	}

}
