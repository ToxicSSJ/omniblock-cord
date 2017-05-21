package omniblock.cord.addons.network;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;

import net.md_5.bungee.api.CommandSender;
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

public class PARTYManager implements Listener {
	
	protected static Map<String, List<String>> ACTIVED_PARTIES = new HashMap<String, List<String>>();
	protected static Map<ProxiedPlayer, RequestStatus> ACTIVED_REQUESTS = new HashMap<ProxiedPlayer, RequestStatus>();
	
	public static void start(){
		
		OmniCord.getPlugin().getProxy().getPluginManager().registerListener(OmniCord.getPlugin(), new PARTYManager());
		OmniCord.getPlugin().getProxy().getPluginManager().registerCommand(OmniCord.getPlugin(), new omniblock.cord.addons.network.PARTYManager.PARTYCmds.PartyCMD());
		OmniCord.getPlugin().getProxy().getPluginManager().registerCommand(OmniCord.getPlugin(), new omniblock.cord.addons.network.PARTYManager.PARTYCmds.GrupoCMD());
		
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

	public static class PartyUtils {
		
		public static List<String> getPartyMembers(ProxiedPlayer owner){
			
			if(ACTIVED_PARTIES.containsKey(owner.getName())){
				return ACTIVED_PARTIES.get(owner.getName());
			}
			
			return Lists.newArrayList();
			
		}
		
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
		
		public static void deleteParty(String name){
			
			ProxiedPlayer owner = getPlayer(name);
			if(owner != null) deleteParty(owner);
			
		}
		
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
		
		public static boolean isMember(ProxiedPlayer owner, String name){
			
			if(!isOwner(owner)) return false;
			if(!isMember(name)) return false;
			
			List<String> members = ACTIVED_PARTIES.get(owner.getName());
			if(members.contains(name)){
				return true;
			}
			
			return false;
			
		}
		
		public static boolean isMember(String name){
			
			for(String k : ACTIVED_PARTIES.keySet()){
				
				List<String> members = ACTIVED_PARTIES.get(k);
				if(members.contains(name)){
					return true;
				}
				
			}
			
			return false;
		}
		
		public static boolean isMember(ProxiedPlayer member){
			
			for(String k : ACTIVED_PARTIES.keySet()){
				
				List<String> members = ACTIVED_PARTIES.get(k);
				if(members.contains(member.getName())){
					return true;
				}
				
			}
			
			return false;
			
		}
		
		public static boolean isOwner(String name){
			
			ProxiedPlayer owner = getPlayer(name);
			if(owner != null) return isOwner(owner);
			
			return false;
		}
		
		public static boolean isOwner(ProxiedPlayer owner){
			
			if(ACTIVED_PARTIES.containsKey(owner.getName())) return true;
			return false;
			
		}
		
		public static ProxiedPlayer getPlayer(String name){
			
			for(ProxiedPlayer p : OmniCord.getInstance().getProxy().getPlayers()){
				if(p.getName().startsWith(name)){
					return p;
					
				}
			}
			
			return null;
			
		}
		
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
				
				OmniCord.getInstance().getProxy().getScheduler().schedule(OmniCord.getPlugin(), new Runnable() {
					
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
