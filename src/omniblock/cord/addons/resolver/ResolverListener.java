package omniblock.cord.addons.resolver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.Date;

import com.google.common.base.Charsets;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import omniblock.cord.OmniCord;
import omniblock.cord.database.Database;

/**
 * @author wirlie
 *
 */
public class ResolverListener implements Listener {

	public static void setup() {
		
		ProxyServer.getInstance().getPluginManager().registerListener(OmniCord.getInstance(), new ResolverListener());
		
	}
	
	@EventHandler
	public void onPlayerNetworkJoin(PostLoginEvent e) {
		ProxiedPlayer player = e.getPlayer();
		PendingConnection connection = player.getPendingConnection();
		boolean premium = connection.isOnlineMode();
		CommandSender console = ProxyServer.getInstance().getConsole();
		
		try {
			console.sendMessage(new ComponentBuilder("[Resolver] ").color(ChatColor.AQUA).append(player.getName()).color(ChatColor.WHITE).append(" ha entrado al servidor, comprobando estado de resolución ...").color(ChatColor.YELLOW).create());
			console.sendMessage(new ComponentBuilder("[Resolver] ").color(ChatColor.AQUA).append("Premium: " + premium).color(ChatColor.WHITE).create());
			console.sendMessage(new ComponentBuilder("[Resolver] ").color(ChatColor.AQUA).append("UUID Online: " + (premium ? player.getUniqueId() : "No es premium (NONE)")).color(ChatColor.WHITE).create());
			console.sendMessage(new ComponentBuilder("[Resolver] ").color(ChatColor.AQUA).append("UUID Offline: " + offlineUUID(player.getName())).color(ChatColor.WHITE).create());
			
			Statement stm = Database.getConnection().createStatement();
			ResultSet result = stm.executeQuery("SELECT * FROM uuid_resolver WHERE " + (premium ? "p_online_uuid" : "p_offline_uuid") + " = '" + player.getUniqueId() + "'");
			
			if(result.next()) {
				//existe
				console.sendMessage(new ComponentBuilder("[Resolver] ").color(ChatColor.AQUA).append("Jugador registrado").color(ChatColor.WHITE).create());
				
				if(premium) {
					console.sendMessage(new ComponentBuilder("[Resolver] ").color(ChatColor.AQUA).append("Comprobando si el jugador ha cambiado su nombre de usuario ...").color(ChatColor.WHITE).create());
					
					String lastName = result.getString("p_last_name");
					if(!lastName.equals(player.getName())) {
						console.sendMessage(new ComponentBuilder("[Resolver] ").color(ChatColor.AQUA).append("Cambio de nombre detectado!!! Actualizando datos ...").color(ChatColor.WHITE).create());
						
						stm.executeUpdate("UPDATE uuid_resolver SET p_offline_uuid = '" + offlineUUID(player.getName()) + "', p_last_name = '" + player.getName() + "' WHERE p_online_uuid = '" + player.getUniqueId() + "'");
					}
				}
			} else {
				console.sendMessage(new ComponentBuilder("[Resolver] ").color(ChatColor.AQUA).append(" Parece que el jugador no está registrado.").color(ChatColor.WHITE).create());
				if(premium) {
					//no existe, pero puede ser que el jugador antes haya sido no premium y entró como premium por primera vez y por esta razon no hay un UUID online registrado
					result = stm.executeQuery("SELECT * FROM uuid_resolver WHERE p_offline_uuid = '" + offlineUUID(player.getName()) + "'");
					
					if(result.next()) {
						console.sendMessage(new ComponentBuilder("[Resolver] ").color(ChatColor.AQUA).append("El jugador si está registrado. Actualizando UUID online. Este jugador es premium pero ya había entrado anteriormente con un cliente no premium y por ello no había un UUID online anteriormente. Corrigiendo ...").color(ChatColor.WHITE).create());
						
						//Si, hay un jugador no premium que ha usado el nombre de este jugador premium. Insertamos UUID Online.
						stm.executeUpdate("UPDATE uuid_resolver SET p_online_uuid = '" + player.getUniqueId() + "' WHERE p_offline_uuid = '" + offlineUUID(player.getName()) + "'");
						return; //terminamos con el proceso de actualizacion
					}
				}
				
				//Solo para mostrar en el mensaje, si no, directamente se podría usar en la consulta sql
				String networkID = generateNetworkID(player.getName());
				
				console.sendMessage(new ComponentBuilder("[Resolver] ").color(ChatColor.AQUA).append("El jugador no está registrado, registrando ...").color(ChatColor.WHITE).create());
				console.sendMessage(new ComponentBuilder("[Resolver] ").color(ChatColor.AQUA).append("Registrando jugador, NetworkID: " + networkID).color(ChatColor.WHITE).create());
				
				//Jugador nuevo
				stm.executeUpdate("INSERT INTO uuid_resolver (p_offline_uuid, p_online_uuid, p_resolver, p_last_name, register_date) VALUES ('" + offlineUUID(player.getName()) + "', '" + (premium ? player.getUniqueId() : "NONE") + "', '" + networkID + "', '" + player.getName() + "', '" + new Timestamp(new Date().getTime()).toString() + "')");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	private static UUID offlineUUID(String name) {
		return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8));
	}
	
	private static String generateNetworkID(String name) {
		UUID randUId = UUID.randomUUID();
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(randUId.toString().getBytes());
			byte[] digest = md.digest();
			
			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				sb.append(String.format("%02x", b & 0xff));
			}
			
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		System.err.println("No se pudo obtener el NetworkID usando MD5!!!! Devolviendo UUID como NetworkID!!!");
		return randUId.toString();
	}

}
