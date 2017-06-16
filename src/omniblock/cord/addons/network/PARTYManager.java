package omniblock.cord.addons.network;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import omniblock.cord.OmniCord;
import omniblock.cord.addons.network.PARTYManager.PartyUtils.RequestStatus;
import omniblock.cord.util.TextUtil;

/**
 * 
 * Esta clase es la encargada de manejar todos los metodos
 * y funciones para el sistema de parties.
 * 
 * @author zlToxicNetherlz
 *
 */
public class PARTYManager implements Listener {
	
	protected static Map<String, List<String>> ACTIVED_PARTIES = new HashMap<String, List<String>>();
	protected static Map<ProxiedPlayer, RequestStatus> ACTIVED_REQUESTS = new HashMap<ProxiedPlayer, RequestStatus>();
	
	/**
	 * 
	 * Con este metodo se inicializa el sistema de parties
	 * registrando los eventos y los comandos respectivamente, para
	 * luego poder ejercer las funciones correctamente.
	 * 
	 */
	public static void start(){
		
		ProxyServer.getInstance().getPluginManager().registerListener(OmniCord.getInstance(), new PARTYManager());
		ProxyServer.getInstance().getPluginManager().registerCommand(OmniCord.getInstance(), new omniblock.cord.addons.network.PARTYManager.PARTYCmds.PartyCMD());
		ProxyServer.getInstance().getPluginManager().registerCommand(OmniCord.getInstance(), new omniblock.cord.addons.network.PARTYManager.PARTYCmds.GrupoCMD());
		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onLogin(PlayerDisconnectEvent e){
		
		if(PartyUtils.isMember(e.getPlayer())){
			
			ProxiedPlayer owner = PartyUtils.getPlayer(PartyUtils.getOwner(e.getPlayer().getName()));
			
			if(owner != null){
				
				if(PartyUtils.isOwner(owner) && ACTIVED_PARTIES.containsKey(owner.getName())){
					
					for(String k : ACTIVED_PARTIES.get(owner.getName())){
						
						ProxiedPlayer member = PartyUtils.getPlayer(k);
						
						if(member != null){
							if(member.isConnected()){
								
								member.sendMessage(TextUtil.format("&8&lP&8arty &9&l» &7El jugador &c" + e.getPlayer().getName() + " &7se ha desconectado."));
								continue;
								
							}
						}
						
					}
					
					return;
					
				}
				
			}
			
			return;
			
		}
		
		if(PartyUtils.isOwner(e.getPlayer()) && ACTIVED_PARTIES.containsKey(e.getPlayer().getName())){
			
			for(String k : ACTIVED_PARTIES.get(e.getPlayer().getName())){
				
				ProxiedPlayer member = PartyUtils.getPlayer(k);
				
				if(member != null){
					if(member.isConnected()){
						
						member.sendMessage(TextUtil.format("&8&lP&8arty &9&l» &7Tu party se ha &cborrado &7porque el lider de esta se ha desconectado."));
						continue;
						
					}
				}
				
			}
			
			PartyUtils.deleteParty(e.getPlayer().getName());
			
		}
		
	}
	
	/**
	 * 
	 * Este metodo funciona como un "manejador" de los argumentos base o default
	 * de un comando para ser procesados y darles funciones especificas.
	 * 
	 * @param sender Quién envió el comando.
	 * @param args Los argumentos del comando.
	 */
	@SuppressWarnings("deprecation")
	public static void handle(CommandSender sender, String[] args) {
		
		if(!(sender instanceof ProxiedPlayer)) return;
		
		if(args.length >= 1){
			
			String action = args[0];
			ProxiedPlayer player = args.length >= 2 ? null : PartyUtils.getPlayer(action);
			
			if(PartyUtils.isOwner(sender.getName())){
				
				if(		   action.equalsIgnoreCase("delete") || action.equalsIgnoreCase("remove")
						|| action.equalsIgnoreCase("disolve") || action.equalsIgnoreCase("borrar")
						|| action.equalsIgnoreCase("destruir")){
					
					PartyUtils.deleteParty(sender.getName());
					return;
					
				}
				
				if(		   action.equalsIgnoreCase("key") || action.equalsIgnoreCase("llave")){
					
					sender.sendMessage(TextUtil.format("&6Proximamente..."));
					return;
					
				}
				
				if(		   action.equalsIgnoreCase("members") || action.equalsIgnoreCase("miembros")){
					
					PartyUtils.displayMembers((ProxiedPlayer) sender, sender.getName());
					return;
					
				}
				
				if(player != null){
					
					if(player.getName() == sender.getName()){
						
						sender.sendMessage(TextUtil.format("&cYa te encuentras en esta party!"));
						return;
						
					}
					
					if(PartyUtils.isMember(player) || PartyUtils.isOwner(player)){
						
						sender.sendMessage(TextUtil.format("&cEl jugador &7" + player.getName() + " &7ya pertenece a otra party!"));
						return;
						
					}
					
					PartyUtils.addMember((ProxiedPlayer) sender, player);
					return;
					
				}
				
				if(args.length >= 2){
					
					if(action.equalsIgnoreCase("add") || action.equalsIgnoreCase("añadir")){
						
						player = PartyUtils.getPlayer(args[1]);
						
						if(player != null){
							
							if(PartyUtils.isMember((ProxiedPlayer) sender, player.getName())){
								
								sender.sendMessage(TextUtil.format("&6El jugador &7" + args[1] + " &6ya pertenece a tu party!"));
								return;
								
							}
							
							PartyUtils.addMember((ProxiedPlayer) sender, player);
							return;
							
						}
						
						sender.sendMessage(TextUtil.format("&cEl jugador &7" + args[1] + " &cno está online!"));
						return;
						
					}
					
					if(action.equalsIgnoreCase("kick") || action.equalsIgnoreCase("expulsar") || action.equalsIgnoreCase("kickear")){
						
						if(!PartyUtils.isMember((ProxiedPlayer) sender, args[1])){
							
							sender.sendMessage(TextUtil.format("&cEl jugador &7" + args[1] + " &cno es miembro de tu party!"));
							return;
							
						}
						
						PartyUtils.kickParty((ProxiedPlayer) sender, args[1]);
						return;
						
					}
					
				}
				
				sender.sendMessage(TextUtil.BAR);
				sender.sendMessage(TextUtil.format(action.equalsIgnoreCase("ayuda") || action.equalsIgnoreCase("help") ?
						"&7&8&l(&6&l!&8&l) &7Tus comandos disponibles como lider de una Party son:" :
						"&7&8&l(&6&l!&8&l) &cUps, No se ha reconocido ese comando! &7Tus comandos disponibles como lider de una Party son:"));
				
				sender.sendMessage("");
				sender.sendMessage(TextUtil.format(" &e» /party (jugador) &6- &7Añade a un jugador a la party."));
				sender.sendMessage(TextUtil.format(" &e» /party miembros &6- &7Revisa los miembros de la party."));
				sender.sendMessage(TextUtil.format(" &e» /party borrar &6- &7Borra tu party y expulsa a todos los miembros."));
				sender.sendMessage(TextUtil.format(" &e» /party añadir (jugador) &6- &7Añade a un jugador a la party."));
				sender.sendMessage(TextUtil.format(" &e» /party expulsar (jugador) &6- &7Expulsa a un jugador de la party."));
				sender.sendMessage(TextUtil.format(" &e» /party key &6- &7Te devolverá el codigo de la party."));
				sender.sendMessage("");
				sender.sendMessage(TextUtil.BAR);
				
				return;
				
			}
			
			if(PartyUtils.isMember(sender.getName())){
				
				if(		   action.equalsIgnoreCase("salir") || action.equalsIgnoreCase("leave")){
					
					PartyUtils.leaveParty((ProxiedPlayer) sender);
					return;
					
				}
				
				if(		   action.equalsIgnoreCase("key") || action.equalsIgnoreCase("llave")){
					
					sender.sendMessage(TextUtil.format("&6Proximamente..."));
					return;
					
				}
				
				if(		   action.equalsIgnoreCase("members") || action.equalsIgnoreCase("miembros")){
					
					PartyUtils.displayMembers((ProxiedPlayer) sender, PartyUtils.getOwner(sender.getName()));
					return;
					
				}
				
				sender.sendMessage(TextUtil.BAR);
				sender.sendMessage(TextUtil.format(action.equalsIgnoreCase("ayuda") || action.equalsIgnoreCase("help") ?
						"&7&8&l(&6&l!&8&l) &7Tus comandos disponibles como miembro de una Party son:" :
						"&7&8&l(&6&l!&8&l) &cUps, No se ha reconocido ese comando! &7Tus comandos disponibles como miembro de una Party son:"));
				
				sender.sendMessage("");
				sender.sendMessage(TextUtil.format(" &e» /party miembros &6- &7Revisa los miembros de la party en la que estás."));
				sender.sendMessage(TextUtil.format(" &e» /party salir &6- &7Salte de la party en la que estás."));
				sender.sendMessage(TextUtil.format(" &e» /party key &6- &7Te devolverá el codigo de la party."));
				sender.sendMessage("");
				sender.sendMessage(TextUtil.BAR);
				
				return;
				
			}
			
			if(player != null){
				
				if(player.getName() == sender.getName()){
					
					sender.sendMessage(TextUtil.format("&cNo te puedes crear una party invitandote a ti mismo!"));
					return;
					
				}
				
				if(PartyUtils.isMember(player) || PartyUtils.isOwner(player)){
					
					sender.sendMessage(TextUtil.format("&cEl jugador &7" + player.getName() + " &7ya pertenece a otra party!"));
					return;
					
				}
				
				if(PartyUtils.isMember(sender.getName()) || PartyUtils.isOwner(sender.getName())){
					
					sender.sendMessage(TextUtil.format("&cYa perteneces a una party!"));
					return;
					
				}
				
				PartyUtils.makeParty((ProxiedPlayer) sender);
				PartyUtils.addMember((ProxiedPlayer) sender, player);
				return;
				
			}
			
			if(action.equalsIgnoreCase("create") || action.equalsIgnoreCase("crear")){
				
				if(!PartyUtils.isMember(sender.getName()) && !PartyUtils.isOwner(sender.getName())){
					
					PartyUtils.makeParty((ProxiedPlayer) sender);
					return;
					
				}
				
				sender.sendMessage(TextUtil.format("&cNo puedes crear una party si perteneces o eres dueño de una!"));
				return;
				
			}
			
			if(action.equalsIgnoreCase("aceptar") || action.equalsIgnoreCase("accept")){
				
				if(ACTIVED_REQUESTS.containsKey((ProxiedPlayer) sender)){
					
					if(ACTIVED_REQUESTS.get((ProxiedPlayer) sender).getStatus() == RequestStatus.Status.WAITING){
						
						ACTIVED_REQUESTS.get((ProxiedPlayer) sender).accept();
						return;
						
					}
					
					ACTIVED_REQUESTS.remove((ProxiedPlayer) sender);
					sender.sendMessage(TextUtil.format("&cEstá petición ya a expirado."));
					return;
					
				}
				
				sender.sendMessage(TextUtil.format("&cNo tienes ninguna invitación de party pendiente!"));
				return;
				
			}
			
			if(action.equalsIgnoreCase("denegar") || action.equalsIgnoreCase("declinar") || action.equalsIgnoreCase("decline") || action.equalsIgnoreCase("cancel")){
				
				if(ACTIVED_REQUESTS.containsKey((ProxiedPlayer) sender)){
					
					if(ACTIVED_REQUESTS.get((ProxiedPlayer) sender).getStatus() == RequestStatus.Status.WAITING){
						
						ACTIVED_REQUESTS.get((ProxiedPlayer) sender).decline();
						return;
						
					}
					
					ACTIVED_REQUESTS.remove((ProxiedPlayer) sender);
					sender.sendMessage(TextUtil.format("&cEstá petición ya a expirado."));
					return;
					
				}
				
				sender.sendMessage(TextUtil.format("&cNo tienes ninguna invitación de party pendiente!"));
				return;
				
			}
			
			sender.sendMessage(TextUtil.BAR);
			sender.sendMessage(TextUtil.format(action.equalsIgnoreCase("ayuda") || action.equalsIgnoreCase("help") ?
					"&7&8&l(&6&l!&8&l) &7Tus comandos disponibles para las parties son:" :
					"&7&8&l(&6&l!&8&l) &cUps, No se ha reconocido ese comando! &7Tus comandos disponibles para las parties son:"));
			
			sender.sendMessage("");
			sender.sendMessage(TextUtil.format(" &e» /party (jugador) &6- &7Crea tu propia party y enviar una petición de miembro al jugador seleccionado."));
			sender.sendMessage(TextUtil.format(" &e» /party crear &6- &7Crea tu propia party."));
			if(ACTIVED_REQUESTS.containsKey((ProxiedPlayer) sender)){
				sender.sendMessage(TextUtil.format(" &e» /party aceptar &6- &7Acepta la petición de party que te han enviado."));
				sender.sendMessage(TextUtil.format(" &e» /party denegar &6- &7Cancela la petición de party que te han enviado."));
			}
			sender.sendMessage("");
			sender.sendMessage(TextUtil.BAR);
			
			return;
			
		}
		
		TextComponent message = new TextComponent(TextUtil.format(" &8» &7Para obtener toda la información de comandos sobre las &7parties por favor ejecuta el siguiente comando: "));
		TextComponent command = new TextComponent(TextUtil.format("&b/party ayuda"));
		
		command.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(TextUtil.format("&6&lClick&7 para ejecutar el comando!")).create()));
		command.setClickEvent(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/party ayuda" ));
		
		message.addExtra(command);
		sender.sendMessage(message);
		return;
		
	}

	/**
	 * 
	 * Esta clase es la encargada de tener todas las utilidades
	 * y funciones para las Parties con el fin de que sea mas facil
	 * su manejo y administración. 
	 * 
	 * @author zlToxicNetherlz
	 *
	 */
	public static class PartyUtils {
		
		/**
		 * 
		 * Con este metodo se puede recibir la lista con los nombres de los
		 * miembros de una party a partir del dueño de la party.
		 * 
		 * @param owner El dueño de la party.
		 * @return La lista con los nombres de los miembros de la party.
		 */
		public static List<String> getPartyMembers(ProxiedPlayer owner){
			
			if(ACTIVED_PARTIES.containsKey(owner.getName())){
				return ACTIVED_PARTIES.get(owner.getName());
			}
			
			return Lists.newArrayList();
			
		}
		
		/**
		 * 
		 * Con este metodo se le envia la información de la
		 * party a un jugador.
		 * 
		 * @param player El nombre del jugador al cual se le enviará la información.
		 * @param owner El dueño de la party de la cual se sacará la información.
		 */
		@SuppressWarnings("deprecation")
		public static void displayMembers(ProxiedPlayer player, String owner){
			
			if(getPlayer(owner) == null) return;
			if(!isOwner(owner)) return;
			
			player.sendMessage("");
			player.sendMessage(TextUtil.format("&8Información de tu party:"));
			player.sendMessage(TextUtil.format("   &bDueño: &7" + owner));
			player.sendMessage(TextUtil.format("   &bMiembros: "));
			
			for(String k : ACTIVED_PARTIES.get(owner)){

				ProxiedPlayer cache = getPlayer(k);
				player.sendMessage(TextUtil.format(cache != null ? "      &8&l(&a&lON&8&l)&7 "  + k : "      &8&l(&c&lOFF&8&l)&7 " + k));
				continue;
				
			}
			
		}
		
		/**
		 * 
		 * Con este metodo se saca a un jugador de una party por medio del
		 * dueño de la party y el nombre del miembro que se desea expulsar.
		 * 
		 * @param owner El dueño de la party.
		 * @param member Nombre del miembro que se quiere expulsar.
		 */
		@SuppressWarnings("deprecation")
		public static void kickParty(ProxiedPlayer owner, String member){
			
			if(!ACTIVED_PARTIES.containsKey(owner.getName())) return;
			
			List<String> members = ACTIVED_PARTIES.get(owner.getName());
			
			if(members.contains(member)){
				
				ProxiedPlayer player = getPlayer(member);
				owner.sendMessage(TextUtil.format("&6Has expulsado a &7" + member + " &6correctamente!"));
				
				if(player != null){
					player.sendMessage(TextUtil.format("&6Has sido expulsado de la party en la que estabas!"));
				}
				
				ACTIVED_PARTIES.get(owner.getName()).remove(members.indexOf(member));
				
				for(String k : ACTIVED_PARTIES.get(owner)){
					
					ProxiedPlayer v = getPlayer(k);
					if(v != null) v.sendMessage(TextUtil.format("&8&lP&8arty &9&l» &7El jugador &c" + member + " &7ha sido expulsado de la party."));
					continue;
					
				}
				
				return;
				
			}
			
		}
		
		/**
		 * 
		 * Este metodo debe ser llamado cuando un miembro quiera
		 * abandonar su party, y se encargará de sacarlo de la party
		 * en la cual está.
		 * 
		 * @param member El miembro que quiere salir de su party.
		 */
		@SuppressWarnings("deprecation")
		public static void leaveParty(ProxiedPlayer member){
			
			member.sendMessage(TextUtil.centerAndFormat("&6¡Has abandonado la party correctamente!"));
			
			int index = 0;
			String owner = null;
			
			for(String k : ACTIVED_PARTIES.keySet()){
				
				List<String> members = ACTIVED_PARTIES.get(k);
				
				if(members.contains(member.getName())){
					
					index = members.indexOf(member.getName());
					owner = k;
					break;
					
				}
				
			}
			
			if(owner != null){
				
				for(String k : ACTIVED_PARTIES.get(owner)){
					
					ProxiedPlayer v = getPlayer(k);
					if(v != null) v.sendMessage(TextUtil.format("&8&lP&8arty &9&l» &7El jugador &c" + member.getName() + " &7ha abandonado la party."));
					continue;
					
				}
				
				ProxiedPlayer player = getPlayer(owner);
				if(player != null) player.sendMessage(TextUtil.format("&8&lP&8arty &9&l» &7El jugador &c" + member.getName() + " &7ha abandonado la party."));
				
				ACTIVED_PARTIES.get(owner).remove(index);
				return;
				
			}
			
		}
		
		/**
		 * 
		 * Este metodo se encarga de borrar una party por medio del nombre
		 * del dueño en formato String.
		 * 
		 * @param name Nombre del dueño de la party.
		 * @see PartyUtils#deleteParty(ProxiedPlayer)
		 */
		public static void deleteParty(String name){
			
			ProxiedPlayer owner = getPlayer(name);
			if(owner != null) deleteParty(owner);
			
		}
		
		/**
		 * 
		 * Este metodo borra una party por medio del dueño de la misma,
		 * una vez se borre dicha party todos los miembros son expulsados
		 * y la party se disuelve.
		 * 
		 * @param owner El dueño de la party.
		 */
		@SuppressWarnings("deprecation")
		public static void deleteParty(ProxiedPlayer owner){
			
			if(ACTIVED_PARTIES.containsKey(owner.getName())){
				
				owner.sendMessage("");
				owner.sendMessage(TextUtil.getCenteredMessage("&a¡Has borrado tu party correctamente!"));
				owner.sendMessage("");
				
				for(String k : ACTIVED_PARTIES.get(owner.getName())){
					
					ProxiedPlayer p = getPlayer(k);

					if(p != null){
						
						p.sendMessage("");
						p.sendMessage(TextUtil.format("&7La &6&lparty &7en la que estabas ha sido borrada " +
												      "&7por el lider y has sido expulsado de ella."));
						p.sendMessage("");
						continue;
						
					}
					
				}
				
				ACTIVED_PARTIES.remove(owner.getName());
				
			}
			
		}
		
		/**
		 * 
		 * Con este metodo se crea una party en base a un jugador
		 * el cual será definido como el dueño de la misma, este metodo
		 * solo creará la party sin ningún miembro.
		 * 
		 * @param owner El jugador el cual será el creador y dueño de la party.
		 */
		@SuppressWarnings("deprecation")
		public static void makeParty(ProxiedPlayer owner){
			
			if(!isOwner(owner) && !isMember(owner)){
				
				owner.sendMessage("");
				owner.sendMessage(TextUtil.getCenteredMessage("&a¡Has creado una party correctamente!"));
				owner.sendMessage("");
				
				ACTIVED_PARTIES.put(owner.getName(), Lists.newArrayList());
				return;
				
			}
			
		}
		
		/**
		 * 
		 * Con este metodo se puede enviar una petición a un jugador
		 * para que este se una a la respectiva party de la cual se
		 * le envió dicha petición.
		 * 
		 * @param owner El dueño de la party.
		 * @param member El jugador el cual se le enviará la petición.
		 */
		@SuppressWarnings("deprecation")
		public static void addMember(ProxiedPlayer owner, ProxiedPlayer member){
			
			if(isOwner(owner)){
				
				if(!isMember(member.getName()) && !isOwner(member.getName())){
					
					owner.sendMessage(TextUtil.format("&aLe has enviado la petición de miembro a &7" + member.getName() + " &acorrectamente!"));
					sendRequest(owner, member);
					return;
					
				}
				
				owner.sendMessage(TextUtil.format("&cEl jugador &7" + member.getName() + " &cya pertenece a otra party."));
				
			}
			
		}
		
		/**
		 * 
		 * Con este metodo se verifica si un jugador es miembro de cierta
		 * party basandose en el dueño de la party del cual se verificará
		 * está afirmación y el jugador.
		 * 
		 * @param owner El dueño de la party.
		 * @param name El nombre del jugador del cual se verificará si
		 * pertenece o no a la party del dueño.
		 * @return <strong>true</strong> si el jugador pertenece a la party.
		 */
		public static boolean isMember(ProxiedPlayer owner, String name){
			
			if(!isOwner(owner)) return false;
			if(!isMember(name)) return false;
			
			List<String> members = ACTIVED_PARTIES.get(owner.getName());
			if(members.contains(name)){
				return true;
			}
			
			return false;
			
		}
		
		/**
		 * 
		 * Con este metodo se verifica si un jugador especificado
		 * por su nombre pertenece a una party.
		 * 
		 * @param name El nombre del jugador.
		 * @return <strong>true</strong> si el jugador pertenece a una party.
		 * @see PartyUtils#isMember(ProxiedPlayer)
		 */
		public static boolean isMember(String name){
			
			for(String k : ACTIVED_PARTIES.keySet()){
				
				List<String> members = ACTIVED_PARTIES.get(k);
				if(members.contains(name)){
					return true;
				}
				
			}
			
			return false;
		}
		
		/**
		 * 
		 * Con este metodo se verifica si un jugador
		 * pertenece a una party.
		 * 
		 * @param member El jugador del cual se comprobará la información.
		 * @return <strong>true</strong> si el jugador pertenece a una party.
		 */
		public static boolean isMember(ProxiedPlayer member){
			
			for(String k : ACTIVED_PARTIES.keySet()){
				
				List<String> members = ACTIVED_PARTIES.get(k);
				if(members.contains(member.getName())){
					return true;
				}
				
			}
			
			return false;
			
		}
		
		/**
		 * 
		 * Con este metodo se verifica si un jugador especificado por
		 * su nombre es el dueño de una party.
		 * 
		 * @param name El nombre del jugador del cual se quiere verificar si es
		 * dueño de una party.
		 * @return <strong>true</strong> si el jugador es dueño de la party.
		 * @see PartyUtils#isOwner(ProxiedPlayer)
		 */
		public static boolean isOwner(String name){
			
			ProxiedPlayer owner = getPlayer(name);
			if(owner != null) return isOwner(owner);
			
			return false;
		}
		
		/**
		 * 
		 * Con este metodo se verifica si un jugador
		 * es el dueño de una party.
		 * 
		 * @param owner El jugador del cual se quiere verificar si es
		 * dueño de una party.
		 * @return <strong>true</strong> si el jugador es dueño de la party.
		 */
		public static boolean isOwner(ProxiedPlayer owner){
			
			if(ACTIVED_PARTIES.containsKey(owner.getName())) return true;
			return false;
			
		}
		
		/**
		 * 
		 * Con este metodo se puede obtener un ProxiedPlayer en base al
		 * nombre del jugador, este jugador debe estár conectado en la
		 * Network y su nombre debe ser exactamente el especificado, Aunque
		 * este metodo también puede obtener el nombre del jugador
		 * en base a sus iniciales.
		 * 
		 * @param name Nombre del jugador.
		 * @return El jugador encontrado, si no se encuentra devuelve <strong>null</strong>.
		 */
		public static ProxiedPlayer getPlayer(String name){
			
			for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()){
				if(p.getName().startsWith(name)){
					return p;
					
				}
			}
			
			return null;
			
		}
		
		/**
		 * 
		 * Con este metodo se puede obtener el nombre del dueño de la
		 * party basandose en un miembro de la party.
		 * 
		 * @param member Nombre del miembro de la party.
		 * @return El nombre del dueño de la party, si no se encuentra devuelve <strong>null</strong>.
		 */
		public static String getOwner(String member){
			
			String owner = null;
			
			for(String k : ACTIVED_PARTIES.keySet()){
				
				List<String> members = ACTIVED_PARTIES.get(k);
				
				if(members.contains(member)){
					
					owner = k;
					break;
					
				}
				
			}
			
			return owner;
			
		}
		
		/**
		 * 
		 * Con este metodo se le envia una petición de party a un
		 * jugador basandose en quien esta enviando la petición y a
		 * quien se le enviará.
		 * 
		 * @param owner El jugador que está enviando la petición.
		 * @param member El jugador al cual se le enviará la petición.
		 */
		@SuppressWarnings("deprecation")
		private static void sendRequest(ProxiedPlayer owner, ProxiedPlayer member){
			
			TextComponent message = new TextComponent("                ");
			
			TextComponent accept = new TextComponent(TextUtil.format("&a&lAceptar"));
			TextComponent deny = new TextComponent(TextUtil.format("&c&lDenegar"));
			
			accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(TextUtil.format("&a&l✔  &7Aceptar la invitación de &6&l" + owner.getName())).create()));
			deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(TextUtil.format("&c&l✖  &7Denegar la invitación de &6&l" + owner.getName())).create()));
			
			accept.setClickEvent(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/party accept" ));
			deny.setClickEvent(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/party deny" ));
			
			message.addExtra(accept);
			message.addExtra(TextUtil.format("&8&l|&r        "));
			message.addExtra(deny);
			
			member.sendMessage("");
			member.sendMessage(TextUtil.format(" &8» &eEl jugador &6" + owner.getName() + " &ete ha invitado a unirte a su party."));
			member.sendMessage(message);
			member.sendMessage("");
			
			RequestStatus request = new RequestStatus(owner, member);
			request.listen();
			
			ACTIVED_REQUESTS.put(member, request);
			
		}
		
		public static class RequestStatus {
			
			public static enum Status {
				
				NONE,
				WAITING,
				CANCELED,
				ACCEPTED,
				DECLINED,
				EXPIRES,
				
				;
				
			}
			
			public static final int expire_time = 60;
			
			private Status status = Status.NONE;
			
			private ProxiedPlayer owner;
			private ProxiedPlayer member;
			
			protected boolean expires = false;
			
			public RequestStatus(ProxiedPlayer owner, ProxiedPlayer member){
				
				this.owner = owner;
				this.member = member;
				
			}
			
			public RequestStatus listen(){
				
				status = Status.WAITING;
				
				ProxyServer.getInstance().getScheduler().schedule(OmniCord.getInstance(), new Runnable() {
					
					@Override
		            public void run() {
						
						expires = true;
						status = Status.EXPIRES;
						
						if(member.isConnected()){
							
							if(ACTIVED_REQUESTS.containsKey(member)){
								
								ACTIVED_REQUESTS.remove(member);
								return;
								
							}
							
						}
		            	
		            	return;
		            	
		            }
				}, expire_time, TimeUnit.SECONDS);
				
				return this;
				
			}

			@SuppressWarnings("deprecation")
			public void accept(){
				
				if(expires){
					
					if(member.isConnected()){
						
						ACTIVED_REQUESTS.remove(member);
						member.sendMessage(TextUtil.format("&cLa petición a expirado! &7Pidele al dueño de la party que reenvie la petición."));
						return;
						
					}
					
					return;
					
				} else {
					
					if(owner.isConnected()){
						
						if(!PartyUtils.isOwner(owner)){
							
							ACTIVED_REQUESTS.remove(member);
							
							status = Status.CANCELED;
							expires = true;
							
							member.sendMessage(TextUtil.format("&cLa party a la que intentas entrar a sido eliminada!"));
							return;
							
						}
						
						ACTIVED_REQUESTS.remove(member);
						
						status = Status.ACCEPTED;
						expires = true;
						
						member.sendMessage(TextUtil.format("&aHas aceptado la petición de &7" + owner.getName() + " &acorrectamente!"));
						owner.sendMessage(TextUtil.format("&aEl jugador &2" + member.getName() + " &aa aceptado la petición de party que le has enviado."));
						
						if(ACTIVED_PARTIES.containsKey(owner.getName())){
							if(!ACTIVED_PARTIES.get(owner.getName()).contains(member.getName())){
								ACTIVED_PARTIES.get(owner.getName()).add(member.getName());
							}
						}
						
						return;
						
					} else {
						
						ACTIVED_REQUESTS.remove(member);
						
						status = Status.CANCELED;
						expires = true;
						
						member.sendMessage(TextUtil.format("&cLa party a la que intentas entrar a sido eliminada porque el dueño se ha desconectado!"));
						return;
						
					}
					
				}
				
			}
			
			@SuppressWarnings("deprecation")
			public void decline(){
				
				if(expires){
					
					if(member.isConnected()){
						
						ACTIVED_REQUESTS.remove(member);
						
						member.sendMessage(TextUtil.format("&cLa petición a expirado! &7Pidele al dueño de la party que reenvie la petición."));
						return;
						
					}
					return;
					
				}
				
				if(owner.isConnected()){
			
					if(!PartyUtils.isOwner(owner)){
						
						ACTIVED_REQUESTS.remove(member);
						
						status = Status.DECLINED;
						expires = true;
						
						member.sendMessage(TextUtil.format("&cLa party a la que intentas declinar a sido eliminada!"));
						return;
						
					}
					
					ACTIVED_REQUESTS.remove(member);
					
					status = Status.DECLINED;
					expires = true;
					
					member.sendMessage(TextUtil.format("&cHas cancelado la petición de &7" + owner.getName() + " &ccorrectamente!"));
					owner.sendMessage(TextUtil.format("&6El jugador &7" + member.getName() + " &6a cancelado la petición de party que le has enviado."));
					
					if(ACTIVED_PARTIES.containsKey(owner.getName())){
						if(ACTIVED_PARTIES.get(owner.getName()).contains(member.getName())){
							ACTIVED_PARTIES.get(owner.getName()).remove(member.getName());
						}
					}
					return;
					
				} else {
					
					ACTIVED_REQUESTS.remove(member);
					
					status = Status.DECLINED;
					expires = true;
					
					member.sendMessage(TextUtil.format("&cLa party a la que intentas declinar a sido eliminada porque el dueño se ha desconectado!"));
					return;
					
				}
				
			}
			
			public ProxiedPlayer getOwner() {
				return owner;
			}

			public ProxiedPlayer getMember() {
				return member;
			}

			public Status getStatus() {
				return status;
			}

			public void setStatus(Status status) {
				this.status = status;
			}
			
		}
		
	}
	
	public static class PARTYCmds {
		
		public static class PartyCMD extends Command {
			
			public PartyCMD() {
				super("party");
			}

			@Override
			public void execute(CommandSender sender, String[] args) {
				PARTYManager.handle(sender, args);
			}
			
		}
		
		public static class GrupoCMD extends Command {
			
			public GrupoCMD() {
				super("grupo");
			}

			@Override
			public void execute(CommandSender sender, String[] args) {
				PARTYManager.handle(sender, args);
			}
			
		}
		
	}
	
}
