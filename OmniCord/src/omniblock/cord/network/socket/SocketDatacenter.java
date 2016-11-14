/*
 *  Omniblock Developers Team - Copyright (C) 2016
 *
 *  This program is not a free software; you cannot redistribute it and/or modify it.
 *
 *  Only this enabled the editing and writing by the members of the team. 
 *  No third party is allowed to modification of the code.
 *
 */

package omniblock.cord.network.socket;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import omniblock.cord.OmniCord;
import omniblock.cord.network.socket.SocketManager;

public class SocketDatacenter {
    public static Map<String, Integer> socketports = new HashMap<String, Integer>();

    public static void setSocketPorts() {
        for (Map.Entry<String, ServerInfo> k : ProxyServer.getInstance().getServers().entrySet()) {
            ServerInfo si = (ServerInfo)k.getValue();
            String name = si.getName();
            Object obj = OmniCord.configuration.get("socket-ports." + name);
            System.out.println("name = " + name);
            if (obj == null) {
                System.out.println("setting!");
                OmniCord.configuration.set("socket-ports." + name, (Object)23535);
                try {
                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(OmniCord.configuration, new File(OmniCord.plugin.getDataFolder(), "config.yml"));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            int port = OmniCord.configuration.getInt("socket-ports." + name);
            socketports.put(name, port);
        }
    }

    public static void handleData(String data) {
        if (data.contains("DFB588DB6FEF2BAB4E2F1723DFD32")) {
            data = data.replaceAll("DFB588DB6FEF2BAB4E2F1723DFD32", "");
            String[] comp = data.split("!k\u00a1");
            System.out.println("GETTING DATA: " + comp[0] + " | " + comp[1]);
            SocketManager.sendDataToAllServersBySocket(comp[0], comp[1]);
        }
    }
}

