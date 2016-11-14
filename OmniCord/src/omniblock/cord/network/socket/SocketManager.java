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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import omniblock.cord.network.socket.SocketDatacenter;

public class SocketManager
extends Thread {
    public ServerSocket socket = null;
    public static final String socketkey = "DFB588DB6FEF2BAB4E2F1723DFD32";

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            try {
                Socket client = this.socket.accept();
                DataInputStream dis = new DataInputStream(client.getInputStream());
                String data = dis.readUTF();
                SocketDatacenter.handleData(data);
                dis.close();
                continue;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    public static void sendDataToAllServersBySocket(String channel, String data) {
        try {
            for (Map.Entry<String, ServerInfo> k : ProxyServer.getInstance().getServers().entrySet()) {
                ServerInfo si = (ServerInfo)k.getValue();
                if (!SocketDatacenter.socketports.containsKey(si.getName())) continue;
                InetSocketAddress isa = si.getAddress();
                isa.getAddress();
                String ip4 = InetAddress.getLocalHost().getHostAddress();
                System.out.println("SENDING TO:      IP-" + ip4 + " |||||    PORT-" + SocketDatacenter.socketports.get(si.getName()));
                Socket client = new Socket(ip4, (int)SocketDatacenter.socketports.get(si.getName()));
                DataOutputStream ds = new DataOutputStream(client.getOutputStream());
                ds.writeUTF("DFB588DB6FEF2BAB4E2F1723DFD32" + channel + "!k\u00a1" + data);
                ds.close();
                client.close();
            }
        }
        catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
