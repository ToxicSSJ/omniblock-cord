/*
 *  Omniblock Developers Team - Copyright (C) 2016
 *
 *  This program is not a free software; you cannot redistribute it and/or modify it.
 *
 *  Only this enabled the editing and writing by the members of the team. 
 *  No third party is allowed to modification of the code.
 *
 */

package omniblock.cord.database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.Connection;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import omniblock.cord.OmniCord;

public class Database {
	
	public static Connection conn = null;
	
	public final static int UUIDHELPER_DATA_BY_NAME = 0x0;
	public final static int UUIDHELPER_DATA_BY_ONLINE_UUID = 0x1;
	public final static int UUIDHELPER_DATA_BY_OFFLINE_UUID = 0x2;

	public static boolean makeConnection(){

		CommandSender console = ProxyServer.getInstance().getConsole();
		console.sendMessage(new ComponentBuilder("------------------[MySQL]--------------------").color(ChatColor.GOLD).create());
		
		String host = OmniCord.getInstance().getConfig().getString("database.mysql.host");
		int port = OmniCord.getInstance().getConfig().getInt("database.mysql.port");
		String user = OmniCord.getInstance().getConfig().getString("database.mysql.user");
		String pass = OmniCord.getInstance().getConfig().getString("database.mysql.pass");
		String database = OmniCord.getInstance().getConfig().getString("database.mysql.database");
		
		if(host == null){
			console.sendMessage(new ComponentBuilder("[!] &fEl host no pudo ser obtenido (null)").color(ChatColor.RED).create());
		}
		
		if(port == 0){
			console.sendMessage(new ComponentBuilder("[!] &fEl puerto no pudo ser obtenido (null)").color(ChatColor.RED).create());
		}
		
		if(user == null){
			console.sendMessage(new ComponentBuilder("[!] &fEl usuario no pudo ser obtenido (null)").color(ChatColor.RED).create());
		}
		
		if(pass == null){
			console.sendMessage(new ComponentBuilder("[!] &fLa contraseña no pudo ser obtenido (null)").color(ChatColor.RED).create());
		}
		
		if(database == null){
			console.sendMessage(new ComponentBuilder("[!] &fLa base de datos no pudo ser obtenido (null)").color(ChatColor.RED).create());
		}
		
		if(host != null && port != 0 && user != null && pass != null && database != null){
			console.sendMessage(new ComponentBuilder("Host: ").color(ChatColor.AQUA).append(host).color(ChatColor.WHITE).create());
			console.sendMessage(new ComponentBuilder("Puerto: ").color(ChatColor.AQUA).append(String.valueOf(port)).color(ChatColor.WHITE).create());
			console.sendMessage(new ComponentBuilder("Base de Datos: ").color(ChatColor.AQUA).append(database).color(ChatColor.WHITE).create());
			console.sendMessage(new ComponentBuilder("Usuario: ").color(ChatColor.AQUA).append(user).color(ChatColor.WHITE).create());
			console.sendMessage(new ComponentBuilder("Contraseña: ").color(ChatColor.AQUA).append("<Por seguridad no se muestra>").color(ChatColor.WHITE).create());
			console.sendMessage(new ComponentBuilder("Estableciendo conexión ... ").color(ChatColor.YELLOW).create());
			
			String URL = "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true";
			
			try {
				
				conn = (Connection) DriverManager.getConnection(URL, user, pass);
				console.sendMessage(new ComponentBuilder("¡Conexión Establecida!").color(ChatColor.GREEN).create());
				console.sendMessage(new ComponentBuilder("Creando tablas en caso de ser necesario ...").color(ChatColor.YELLOW).create());
				console.sendMessage(new ComponentBuilder("---------------------------------------------").color(ChatColor.GOLD).create());
				
				Statement stm = conn.createStatement();
				for(TableType table : TableType.values()){
					table.make(stm);
				}
				
				stm.close();
				return true;
				
			} catch (SQLException e) {
				console.sendMessage(new ComponentBuilder("Error al establecer conexión ...").color(ChatColor.RED).create());
				e.printStackTrace();
				return false;
			}
		}else{
			console.sendMessage(new ComponentBuilder("Error al establecer conexión ... Verifica que todos los datos estén bien configurados.").color(ChatColor.RED).create());			return false;
		}
	}
	
	public static Connection getConnection(){
		return conn;
	}
}
