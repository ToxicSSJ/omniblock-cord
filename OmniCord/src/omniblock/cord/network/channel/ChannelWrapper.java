/*
 *  Omniblock Developers Team - Copyright (C) 2016
 *
 *  This program is not a free software; you cannot redistribute it and/or modify it.
 *
 *  Only this enabled the editing and writing by the members of the team. 
 *  No third party is allowed to modification of the code.
 *
 */

package omniblock.cord.network.channel;

import com.sun.istack.internal.NotNull;
import java.util.HashMap;
import java.util.Map;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import omniblock.cord.OmniCord;

public class ChannelWrapper
implements Listener {
    private boolean isEnabled = false;
    private String name = "Bungeecord";
    private static Map<ProxiedPlayer, String> datainputstream = new HashMap<ProxiedPlayer, String>();

    public ChannelWrapper(@NotNull String name) {
        this.setName(name);
        try {
            System.out.println("bungee2");
            ProxyServer.getInstance().registerChannel("Bungeecord");
            OmniCord.plugin.getProxy().getPluginManager().registerListener(OmniCord.plugin, (Listener)this);
            this.setEnabled(true);
        }
        catch (Exception e) {
            System.out.println("bungee2_error_channelwrapper");
            e.printStackTrace();
        }
    }

    public void onPluginMessage(PluginMessageEvent e) {
        if (e.getTag().equals("Bungeecord")) {
            
        }
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Map<ProxiedPlayer, String> getDatainputstream() {
        return datainputstream;
    }

    public static void setDatainputstream(Map<ProxiedPlayer, String> datainputstream) {
        ChannelWrapper.datainputstream = datainputstream;
    }
}
