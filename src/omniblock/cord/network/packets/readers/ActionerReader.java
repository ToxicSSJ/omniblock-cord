package omniblock.cord.network.packets.readers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.math.NumberUtils;

import net.md_5.bungee.api.ProxyServer;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.data.PacketStructure;
import net.omniblock.packets.network.structure.data.PacketStructure.DataType;
import net.omniblock.packets.network.structure.packet.RequestActionExecutorPacket;
import net.omniblock.packets.network.structure.packet.ResposeActionExecutorPacket;
import net.omniblock.packets.network.tool.object.PacketReader;
import omniblock.cord.addons.network.MaintenanceManager;
import omniblock.cord.addons.phase.PhaseManager;
import omniblock.cord.database.base.BanBase;
import omniblock.cord.database.base.BankBase;
import omniblock.cord.network.packets.PacketsTools;
import omniblock.cord.database.sql.type.RankType;
import omniblock.cord.database.sql.util.Resolver;
import omniblock.cord.database.base.helpers.BanHelper;

import omniblock.cord.database.base.RankBase;
import omniblock.cord.database.base.SkywarsBase;

public class ActionerReader {
	
	public static void start() {
		
		Packets.READER.registerReader(new PacketReader<RequestActionExecutorPacket>(){

			@Override
			public void readPacket(PacketSocketData<RequestActionExecutorPacket> packetsocketdata) {
				
				PacketStructure structure = packetsocketdata.getStructure();
				
				String requestaction = structure.get(DataType.STRINGS, "requestaction");
				String[] args = ((String) structure.get(DataType.STRINGS, "args")).split(",");
				
				Integer requesterport = structure.get(DataType.INTEGERS, "requesterport");
				
				for(ActionExecutorType type : ActionExecutorType.values()) {
					
					if(type.getRequest().equalsIgnoreCase(requestaction)) {
						
						Packets.STREAMER.streamPacket(
								type.getExecutor().execute(args).build()
									.setPacketUUID(packetsocketdata.getPacketUUID())
									.setReceiver(requesterport));
						return;
						
					}
					
				}
				
				return;
				
			}

			@Override
			public Class<RequestActionExecutorPacket> getAttachedPacketClass() {
				return RequestActionExecutorPacket.class;
			}
			
		});
		
	}
	
	public static enum ActionExecutorType {
		
		BAN_EXECUTOR("banrequest", 4, new ActionExecutor() {

			@SuppressWarnings("deprecation")
			@Override
			public ResposeActionExecutorPacket execute(String[] args) {
				
				String playername = args[0];
				String days = args[1];
				
				String moderatorname = args[2];
				String reason = args[3];
				
				RankType rank = RankBase.getRank(playername);
				
				if(rank == RankType.CEO || rank == RankType.ADMIN) {
					
					return new ResposeActionExecutorPacket()
							.setResponse("No se puede banear a un miembro del equipo Staff de mayor nivel!");
					
				}
				
				if(!Resolver.hasLastName(playername)) {
					
					return new ResposeActionExecutorPacket()
							.setResponse("El jugador " + playername + " nunca ha ingresado a Omniblock Network o su nombre cambió!");
					
				}
				
				if(playername == moderatorname.split("#")[0]) {
					
					return new ResposeActionExecutorPacket()
							.setResponse("No te puedes banear a tí mismo!");
					
				}
				
				if(!NumberUtils.isNumber(days)) {
					
					return new ResposeActionExecutorPacket()
							.setResponse("El formato de días no es valido!");
					
				}
				
				Date date = new Date();
				String hash = UUID.randomUUID().toString().toString().substring(1, 10);

				String banned_id = Resolver.getNetworkIDByName(playername);
				UUID offlineUUID = Resolver.getOfflineUUIDByNetworkID(banned_id);

				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.add(Calendar.DAY_OF_YEAR, NumberUtils.toInt(days));

				String from_str = format.format(date);
				String to_str = format.format(calendar.getTime());

				if (playername == null || days == null || reason == null || offlineUUID == null || hash == null
						|| banned_id == null || moderatorname == null) {
					
					return new ResposeActionExecutorPacket()
							.setResponse("Ha ocurrido un error mientras se ejecutaba el baneo, El sistema esta deshabilitado!");
					
				}

				String[] data = new String[] { hash, "Discord:@" + moderatorname, banned_id, reason, from_str, to_str };
				String insert = BanHelper.getInsertSQL(data);

				if (insert == null)
					return new ResposeActionExecutorPacket()
							.setResponse("Ha ocurrido un error mientras se ejecutaba el baneo, El sistema esta deshabilitado!");
				
				PacketsTools.sendBan2Player(playername);
				
				BanBase.setBanStatus(playername, true);
				BanBase.insertBanRegistry(insert);
				
				return new ResposeActionExecutorPacket()
						.setResponse("El baneo al jugador " + playername + " se ha efectuado correctamente!");
				
			}
			
		}),
		
		PARDON_EXECUTOR("pardonrequest", 1, new ActionExecutor() {
			
			@Override
			public ResposeActionExecutorPacket execute(String[] args) {
				
				String playername = args[0];
				
				if(!Resolver.hasLastName(playername)) {
					
					return new ResposeActionExecutorPacket()
							.setResponse("El jugador " + playername + " nunca ha ingresado a Omniblock Network o su nombre cambió!");
					
				}
				
				if(!BanBase.isBanned(playername)) {
					
					return new ResposeActionExecutorPacket()
							.setResponse("El jugador " + playername + " no está baneado!");
					
				}
				
				BanBase.setBanStatus(playername, false);
				
				return new ResposeActionExecutorPacket()
						.setResponse("El jugador " + playername + " ha sido perdonado correctamente!");
				
			}
			
		}),
		
		KICK_EXECUTOR("kickrequest", 3, new ActionExecutor() {

			@Override
			public ResposeActionExecutorPacket execute(String[] args) {
				
				String playername = args[0];
				
				String moderatorname = args[1];
				String reason = args[2];
				
				RankType rank = RankBase.getRank(playername);
				
				if(rank == RankType.CEO || rank == RankType.ADMIN) {
					
					return new ResposeActionExecutorPacket()
							.setResponse("No se puede kickear a un miembro del equipo Staff de mayor nivel!");
					
				}
				
				if(ProxyServer.getInstance().getPlayer(playername) != null && ProxyServer.getInstance().getPlayer(playername).isConnected()) {
					
					PacketsTools.sendKick2Player(playername, moderatorname, reason);
					
					return new ResposeActionExecutorPacket()
							.setResponse("Se ha realizado la expulsión del jugador " + playername + " correctamente!");
					
				}
				
				return new ResposeActionExecutorPacket()
						.setResponse("El jugador " + playername + " no está conectado en Omniblock Network.");
				
			}
			
		}),
		
		ONLINE_EXECUTOR("onlinerequest", 1, new ActionExecutor() {

			@Override
			public ResposeActionExecutorPacket execute(String[] args) {
				
				String playername = args[0];
				
				if(ProxyServer.getInstance().getPlayer(playername) != null && ProxyServer.getInstance().getPlayer(playername).isConnected()) {
					
					return new ResposeActionExecutorPacket()
							.setResponse("El jugador " + playername + " está __Online__!");
					
				}
				
				return new ResposeActionExecutorPacket()
						.setResponse("El jugador " + playername + " está __Offline__!");
				
			}
			
		}),
		
		NETWORKID_EXECUTOR("networkidrequest", 1, new ActionExecutor() {

			@Override
			public ResposeActionExecutorPacket execute(String[] args) {
				
				String playername = args[0];
				
				if(!Resolver.hasLastName(playername)) {
					
					return new ResposeActionExecutorPacket()
							.setResponse("El jugador " + playername + " nunca ha ingresado a Omniblock Network o su nombre cambió!");
					
				}
				
				return new ResposeActionExecutorPacket()
						.setResponse(Resolver.getNetworkIDByName(playername));
				
			}
			
		}),
		
		RANK_EXECUTOR("rankrequest", 2, new ActionExecutor() {

			@Override
			public ResposeActionExecutorPacket execute(String[] args) {
				
				String playername = args[0];
				String ranktype = args[1];
				
				if(!Resolver.hasLastName(playername)) {
					
					return new ResposeActionExecutorPacket()
							.setResponse("El jugador " + playername + " nunca ha ingresado a Omniblock Network o su nombre cambió!");
					
				}
				
				if(!RankType.exists(ranktype)) {
					
					return new ResposeActionExecutorPacket()
							.setResponse("El rango " + ranktype + " no ha sido reconocido!");
					
				}
				
				RankBase.setRank(playername, RankType.getByName(ranktype));
				
				return new ResposeActionExecutorPacket()
						.setResponse("Se le ha cambiado el rango de " + playername + " a " + ranktype + " correctamente!");
				
			}
			
		}),
		
		MONEY_EXECUTOR("moneyrequest", 3, new ActionExecutor() {

			@SuppressWarnings("deprecation")
			@Override
			public ResposeActionExecutorPacket execute(String[] args) {
				
				String playername = args[0];
				String action = args[1];
				
				String quantity = args[2];
				
				if(!Resolver.hasLastName(playername)) {
					
					return new ResposeActionExecutorPacket()
							.setResponse("El jugador " + playername + " nunca ha ingresado a Omniblock Network o su nombre cambió!");
					
				}
				
				if(!NumberUtils.isNumber(quantity)) {
					
					return new ResposeActionExecutorPacket()
							.setResponse("El formato de la cantidad de dinero no es valido!");
					
				}
				
				if(action.equalsIgnoreCase("set")) {
					
					BankBase.setMoney(playername, NumberUtils.toInt(quantity));
					return new ResposeActionExecutorPacket()
							.setResponse("Se le ha seteado el dinero de " + playername + " a " + quantity + " correctamente!");
					
				}
				
				if(action.equalsIgnoreCase("add")) {
					
					BankBase.addMoney(playername, NumberUtils.toInt(quantity));
					return new ResposeActionExecutorPacket()
							.setResponse("Se le ha añadido " + quantity + " de dinero a " + playername + " correctamente!");
					
				}
				
				if(action.equalsIgnoreCase("remove")) {
					
					BankBase.setMoney(playername, BankBase.getMoney(playername) - NumberUtils.toInt(quantity));
					return new ResposeActionExecutorPacket()
							.setResponse("Se le ha removido " + quantity + " de dinero a " + playername + " correctamente!");
					
				}
				
				return new ResposeActionExecutorPacket()
						.setResponse("La acción " + action + " no es valida para este comando!");
				
			}
			
		}),
		
		NETWORK_BOOSTER_INFO("networkboosterrequest", 1, new ActionExecutor() {

			@Override
			public ResposeActionExecutorPacket execute(String[] args) {
				
				String gametype = args[0];
				String returned = "disabled";
				
				if(!PacketsTools.NETWORK_BOOSTERS.containsKey(gametype))
					return new ResposeActionExecutorPacket()
							.setResponse("invalid");
				
				if(PacketsTools.NETWORK_BOOSTERS.get(gametype) != null)
					return new ResposeActionExecutorPacket()
							.setResponse("enabled#" + PacketsTools.NETWORK_BOOSTERS.get(gametype).getPlayer());
				
				return new ResposeActionExecutorPacket()
						.setResponse(returned);
				
			}
			
		}),
		
		SET_MAINTENANCE("maintenancerequest", 1, new ActionExecutor() {

			@Override
			public ResposeActionExecutorPacket execute(String[] args) {
				
				String status = args[0];
				
				if(status.equalsIgnoreCase("true") && !MaintenanceManager.maintenance) {
					
					MaintenanceManager.setMaintenance(Boolean.valueOf(status));
					
					return new ResposeActionExecutorPacket()
							.setResponse("Se ha activado el modo mantenimiento correctamente!");
					
				} else if(status.equalsIgnoreCase("false") && MaintenanceManager.maintenance) {
					
					MaintenanceManager.setMaintenance(Boolean.valueOf(status));
					
					return new ResposeActionExecutorPacket()
							.setResponse("Se ha desactivado el modo mantenimiento correctamente!");
					
				}
				
				if(!status.equalsIgnoreCase("true") && !status.equalsIgnoreCase("false"))
					return new ResposeActionExecutorPacket()
							.setResponse("El status '" + status + "' es invalido!");
				
				return new ResposeActionExecutorPacket()
						.setResponse("El status '" + status + "' ya es el actual!");
				
			}
			
		}),
		
		BETAKEY("getbetakeyrequest", 1, new ActionExecutor() {

			@Override
			public ResposeActionExecutorPacket execute(String[] args) {
				
				String playername = args[0];
				
				if(!Resolver.hasLastName(playername))
					return new ResposeActionExecutorPacket()
							.setResponse("El jugador " + playername + " nunca ha ingresado a Omniblock Network o su nombre cambió!");
				
				String generatedKey = PhaseManager.getBetaKey(playername, true);
				
				return new ResposeActionExecutorPacket()
						.setResponse("La key generada es: " + generatedKey);
				
			}
			
		}),
		
		REMOVE_BETAKEY("removebetakeyrequest", 1, new ActionExecutor() {

			@Override
			public ResposeActionExecutorPacket execute(String[] args) {
				
				String playername = args[0];
				
				if(!Resolver.hasLastName(playername))
					return new ResposeActionExecutorPacket()
							.setResponse("El jugador " + playername + " nunca ha ingresado a Omniblock Network o su nombre cambió!");
				
				PhaseManager.removeBetaKey(playername);
				
				return new ResposeActionExecutorPacket()
						.setResponse("Se ha removida la key de " + playername + " correctamente!");
				
			}
			
		}),
		
		ADD_SKYWARS_TAG("addskywarstagrequest", 2, new ActionExecutor() {

			@Override
			public ResposeActionExecutorPacket execute(String[] args) {
				
				String playername = args[0];
				String tag = args[1];
				
				if(!Resolver.hasLastName(playername)) {
					
					return new ResposeActionExecutorPacket()
							.setResponse("El jugador " + playername + " nunca ha ingresado a Omniblock Network o su nombre cambió!");
					
				}
				
				SkywarsBase.addItem(playername, tag);
				
				return new ResposeActionExecutorPacket()
						.setResponse("Se le ha añadido el tag " + tag + " al jugador " + playername + " correctamente!");
				
			}
			
		}),
		
		REMOVE_SKYWARS_TAG("removeskywarstagrequest", 2, new ActionExecutor() {

			@Override
			public ResposeActionExecutorPacket execute(String[] args) {
				
				String playername = args[0];
				String tag = args[1];
				
				if(!Resolver.hasLastName(playername)) {
					
					return new ResposeActionExecutorPacket()
							.setResponse("El jugador " + playername + " nunca ha ingresado a Omniblock Network o su nombre cambió!");
					
				}
				
				SkywarsBase.removeItem(playername, tag);
				
				return new ResposeActionExecutorPacket()
						.setResponse("Se le ha removido el tag " + tag + " al jugador " + playername + " correctamente!");
				
			}
			
		}),
		
		;
		
		private String request;
		private int argslength;
		
		private ActionExecutor executor;
		
		ActionExecutorType(String request, int argslength, ActionExecutor executor){
			
			this.request = request;
			
			this.argslength = argslength;
			this.executor = executor;
			
		}
		
		public ActionExecutor getExecutor() {
			return executor;
		}
		
		public String getRequest() {
			return request;
		}
		
		public int getArgsLength() {
			return argslength;
		}
		
		public static interface ActionExecutor {
			
			public ResposeActionExecutorPacket execute(String[] args);
			
		}
		
	}
	
}
