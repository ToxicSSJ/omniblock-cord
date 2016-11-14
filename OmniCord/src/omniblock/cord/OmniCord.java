/*
 *  Omniblock Developers Team - Copyright (C) 2016
 *
 *  This program is not a free software; you cannot redistribute it and/or modify it.
 *
 *  Only this enabled the editing and writing by the members of the team. 
 *  No third party is allowed to modification of the code.
 *
 */

package omniblock.cord;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import omniblock.cord.network.channel.ChannelWrapper;
import omniblock.cord.network.socket.SocketDatacenter;
import omniblock.cord.network.socket.SocketManager;

public class OmniCord
extends Plugin {
    public static Plugin plugin;
    public static Configuration configuration;
    private static ChannelWrapper cw;
    private static SocketManager sm;

    @SuppressWarnings("deprecation")
	public void onEnable() {
        plugin = this;
        this.Commands();
        this.loadConfig();
        this.Implements();
        OmniCord.setChannelWrapper(new ChannelWrapper("Bungeecord"));
        System.out.println("PORT LISTENING: " + ((ListenerInfo)this.getProxy().getConfig().getListeners().iterator().next()).getQueryPort());
        sm = new SocketManager();
        SocketDatacenter.setSocketPorts();
        try {
            OmniCord.sm.socket = new ServerSocket(54545);
            sm.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Implements() {
    }

    public void Commands() {
    }

    @SuppressWarnings("deprecation")
	private void loadConfig() {
        File file;
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }
        if (!(file = new File(this.getDataFolder(), "config.yml")).exists()) {
            try {
			    InputStream in = this.getResourceAsStream("config.yml");
			    try {
			        Files.copy(in, file.toPath(), new CopyOption[0]);
			    }
			    finally {
			        if (in != null) {
			            in.close();
			        }
			    }
			}
			catch (Exception e) {
			   e.printStackTrace();
			}
        }
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), "config.yml"));
        }
        catch (IOException e) {
            plugin.getProxy().getConsole().sendMessage("[OmniCord] [ERROR] Ha ocurrido un error al cargar la configuracion.");
            e.printStackTrace();
        }
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static ChannelWrapper getChannelWrapper() {
        return cw;
    }

    public static void setChannelWrapper(ChannelWrapper cw) {
        OmniCord.cw = cw;
    }
}

