package omniblock.cord.network.textures;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.BadPacketException;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants;
import omniblock.cord.OmniCord;
import omniblock.cord.network.textures.events.ResourcePackSelectEvent;
import omniblock.cord.network.textures.events.ResourcePackSendEvent;
import omniblock.cord.network.textures.io.TextureType;
import omniblock.cord.network.textures.listeners.DisconnectListener;
import omniblock.cord.network.textures.packets.ResourcePackSendPacket;
import omniblock.cord.util.lib.textures.PackManager;
import omniblock.cord.util.lib.textures.ResourcePack;
import omniblock.cord.util.lib.textures.ResourcepacksPlayer;
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
import java.util.logging.Level;

public abstract class BungeeResourcepacks {
    
    private static PackManager pm;

    private static UserManager um;
    
    private static Level loglevel = Level.INFO;

    /**
     * Set of uuids of players which got send a pack by the backend server. 
     * This is needed so that the server does not send the bungee pack if the user has a backend one.
     */
    private static Map<UUID, Boolean> backendPackedPlayers = new ConcurrentHashMap<>();

    /**
     * Set of uuids of players which were authenticated by a backend server's plugin
     */
    private static Set<UUID> authenticatedPlayers = new HashSet<>();

    private static int bungeeVersion;

	@SuppressWarnings("unchecked")
	public static void setup() {
        
        try {
        	
            List<Integer> supportedVersions = new ArrayList<>();
            
            try {
                Field svField = Protocol.class.getField("supportedVersions");
                supportedVersions = (List<Integer>) svField.get(null);
            } catch(Exception e1) {
            }
            
            if(supportedVersions.size() == 0) {
                Field svIdField = ProtocolConstants.class.getField("SUPPORTED_VERSION_IDS");
                supportedVersions = (List<Integer>) svIdField.get(null);
            }

            bungeeVersion = supportedVersions.get(supportedVersions.size() - 1);
            
            if(bungeeVersion == ProtocolConstants.MINECRAFT_1_8) {
            	
                Method reg = Protocol.DirectionData.class.getDeclaredMethod("registerPacket", int.class, Class.class);
                reg.setAccessible(true);
                reg.invoke(Protocol.GAME.TO_CLIENT, 0x48, ResourcePackSendPacket.class);
                
            } else if(bungeeVersion >= ProtocolConstants.MINECRAFT_1_9 && bungeeVersion < ProtocolConstants.MINECRAFT_1_9_4){
            	
                Method reg = Protocol.DirectionData.class.getDeclaredMethod("registerPacket", int.class, int.class, Class.class);
                reg.setAccessible(true);
                reg.invoke(Protocol.GAME.TO_CLIENT, 0x48, 0x32, ResourcePackSendPacket.class);
                
            } else if(bungeeVersion >= ProtocolConstants.MINECRAFT_1_9_4){
            	
                Method map = Protocol.class.getDeclaredMethod("map", int.class, int.class);
                map.setAccessible(true);
                Object mapping18 = map.invoke(null, ProtocolConstants.MINECRAFT_1_8, 0x48);
                Object mapping19 = map.invoke(null, ProtocolConstants.MINECRAFT_1_9, 0x32);
                Object mappingsObject = Array.newInstance(mapping18.getClass(), 2);
                Array.set(mappingsObject, 0, mapping18);
                Array.set(mappingsObject, 1, mapping19);
                Object[] mappings = (Object[]) mappingsObject;
                Method reg = Protocol.DirectionData.class.getDeclaredMethod("registerPacket", Class.class, mappings.getClass());
                reg.setAccessible(true);
                reg.invoke(Protocol.GAME.TO_CLIENT, ResourcePackSendPacket.class, mappings);
                
            } else {
                return;
            }

            pm = new PackManager();
            um = new UserManager();
            registerPacks();

            ProxyServer.getInstance().getPluginManager().registerListener(OmniCord.getInstance(), new DisconnectListener());
            
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch(NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void registerPacks() {
    	
    	for(TextureType type : TextureType.values()){
    		
    		System.out.println("Registrando pack de texturas " + type.getPack().getName() + "!");
    		getPackManager().addPack(type.getPack());
    		continue;
    		
    	}
        
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
        getPackManager().applyPack(player.getUniqueId(), serverName);
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
    public static void sendPack(ProxiedPlayer player, ResourcePack pack) {
        int clientVersion = player.getPendingConnection().getVersion();
        if(clientVersion >= ProtocolConstants.MINECRAFT_1_8) {
            try {
                ResourcePackSendPacket packet = new ResourcePackSendPacket(pack.getUrl(), pack.getHash());
                player.unsafe().sendPacket(packet);
                sendPackInfo(player, pack);
            } catch(BadPacketException e) {
            } catch(ClassCastException e) {
            }
        } else {
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
    public static void sendPackInfo(ProxiedPlayer player, ResourcePack pack) {
    	
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
        player.getServer().sendData("BungeeCord", out.toByteArray());
    }

    public static void setPack(UUID playerId, ResourcePack pack) {
        getPackManager().setPack(playerId, pack);
    }

    public static void sendPack(UUID playerId, ResourcePack pack) {
        ProxiedPlayer player = OmniCord.getInstance().getProxy().getPlayer(playerId);
        if(player != null) {
            sendPack(player, pack);
        }
    }

    public static void clearPack(ProxiedPlayer player) {
        getUserManager().clearUserPack(player.getUniqueId());
        sendPackInfo(player, null);
    }

    public static void clearPack(UUID playerId) {
        getUserManager().clearUserPack(playerId);
        ProxiedPlayer player = OmniCord.getInstance().getProxy().getPlayer(playerId);
        if (player != null) {
            sendPackInfo(player, null);
        }
    }

    public static PackManager getPackManager() {
        return pm;
    }

    public static UserManager getUserManager() {
        return um;
    }

    /**
     * Add a player's UUID to the list of players with a backend pack
     * @param playerId The uuid of the player
     */
    public static void setBackend(UUID playerId) {
        backendPackedPlayers.put(playerId, false);
    }

    /**
     * Remove a player's UUID from the list of players with a backend pack
     * @param playerId The uuid of the player
     */
    public static void unsetBackend(UUID playerId) {
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
        return ChatColor.translateAlternateColorCodes('&', "&6Esta modalidad usa un Texture Pack customizado, Por favor acepte la descarga del texture pack...");
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

    public ResourcepacksPlayer getPlayer(UUID playerId) {
        ProxiedPlayer player = OmniCord.getInstance().getProxy().getPlayer(playerId);
        if(player != null) {
            return new ResourcepacksPlayer(player.getName(), player.getUniqueId());
        }
        return null;
    }

    public ResourcepacksPlayer getPlayer(String playerName) {
        ProxiedPlayer player = OmniCord.getInstance().getProxy().getPlayer(playerName);
        if(player != null) {
            return new ResourcepacksPlayer(player.getName(), player.getUniqueId());
        }
        return null;
    }

    public boolean sendMessage(ResourcepacksPlayer player, String message) {
        return sendMessage(player, Level.INFO, message);
    }

    public boolean sendMessage(ResourcepacksPlayer player, Level level, String message) {
        if(player != null) {
            ProxiedPlayer proxyPlayer = OmniCord.getInstance().getProxy().getPlayer(player.getUniqueId());
            if(proxyPlayer != null) {
                proxyPlayer.sendMessage(TextComponent.fromLegacyText(message));
                return true;
            }
        } else {
        }
        return false;
    }

     public boolean checkPermission(ResourcepacksPlayer player, String perm) {
        // Console
        if(player == null)
            return true;
        return checkPermission(player.getUniqueId(), perm);

    }

    public static boolean checkPermission(UUID playerId, String perm) {
        ProxiedPlayer proxiedPlayer = OmniCord.getInstance().getProxy().getPlayer(playerId);
        if(proxiedPlayer != null) {
            return proxiedPlayer.hasPermission(perm);
        }
        return false;

    }

    public static int getPlayerPackFormat(UUID playerId) {
        ProxiedPlayer proxiedPlayer = OmniCord.getInstance().getProxy().getPlayer(playerId);
        if(proxiedPlayer != null) {
            return getPackManager().getPackFormat(proxiedPlayer.getPendingConnection().getVersion());
        }
        return -1;
    }

    public static IResourcePackSelectEvent callPackSelectEvent(UUID playerId, ResourcePack pack, IResourcePackSelectEvent.Status status) {
        ResourcePackSelectEvent selectEvent = new ResourcePackSelectEvent(playerId, pack, status);
        OmniCord.getInstance().getProxy().getPluginManager().callEvent(selectEvent);
        return selectEvent;
    }

    public static IResourcePackSendEvent callPackSendEvent(UUID playerId, ResourcePack pack) {
        ResourcePackSendEvent sendEvent = new ResourcePackSendEvent(playerId, pack);
        OmniCord.getInstance().getProxy().getPluginManager().callEvent(sendEvent);
        return sendEvent;
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
}
