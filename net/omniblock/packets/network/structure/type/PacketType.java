package net.omniblock.packets.network.structure.type;

/**
 * 
 * Esta clase es un enumerador que tiene
 * como función proveer en formato (enumerador)
 * atributos y el nombre (toString()) designado
 * para cada paquete, indispensable para la
 * creación de un MessagePacket.
 * 
 * @author zlToxicNetherlz
 * @see MessagePacket
 */
public enum PacketType {
	
	WELCOME_PROXY(PacketShowType.ACTION, PacketSenderType.OMNICORE),
	BYE_PROXY(PacketShowType.ACTION, PacketSenderType.OMNICORE),
	
	REQUEST_BOOSTED_GAMES(PacketShowType.RESPOSE, PacketSenderType.ANY),
	REQUEST_GAMEPRESET_PLAYER_COUNT(PacketShowType.RESPOSE, PacketSenderType.ANY),
	REQUEST_PLAYER_GAME_LOBBY_SERVERS(PacketShowType.RESPOSE, PacketSenderType.ANY),
	REQUEST_PLAYER_START_NETWORK_BOOSTER(PacketShowType.RESPOSE, PacketSenderType.ANY),
	
	SERVER_STRUCTURE_INFO(PacketShowType.ACTION, PacketSenderType.OMNICORE),
	GAME_STRUCTURE_INFO(PacketShowType.ACTION, PacketSenderType.OMNICORE),
	
	SERVER_SOCKET_INFO(PacketShowType.ACTION, PacketSenderType.OMNINETWORK),
	SERVER_REMOVE_INFO(PacketShowType.ACTION, PacketSenderType.OMNINETWORK),
	SERVER_RELOAD_INFO(PacketShowType.ACTION, PacketSenderType.ANY),
	
	PLAYER_SEND_TO_NAMED_SERVER(PacketShowType.ACTION, PacketSenderType.OMNINETWORK, PacketSenderType.OMNICORE),
	PLAYER_SEND_TO_SERVER(PacketShowType.ACTION, PacketSenderType.OMNINETWORK, PacketSenderType.OMNICORE),
	PLAYER_SEND_TO_GAME(PacketShowType.ACTION, PacketSenderType.OMNINETWORK, PacketSenderType.OMNICORE),
	PLAYER_SEND_MESSAGE(PacketShowType.ACTION, PacketSenderType.OMNINETWORK, PacketSenderType.OMNICORE),
	PLAYER_SEND_TEXTUREPACK(PacketShowType.ACTION, PacketSenderType.OMNINETWORK, PacketSenderType.OMNICORE),
	
	RESPOSE_RELOAD_INFO(PacketShowType.ACTION, PacketSenderType.OMNINETWORK),
	
	RESPOSE_GAME_PARTY_INFO(PacketShowType.ACTION, PacketSenderType.OMNINETWORK),
	RESPOSE_GAME_ONLINE_INFO(PacketShowType.ACTION, PacketSenderType.OMNINETWORK),
	RESPOSE_GAME_INITIALIZER(PacketShowType.ACTION, PacketSenderType.OMNINETWORK),
	
	RESPOSE_AUTH_EVALUATE(PacketShowType.ACTION, PacketSenderType.OMNINETWORK),
	RESPOSE_BOOSTED_GAMES(PacketShowType.ACTION, PacketSenderType.OMNINETWORK),
	
	RESPOSE_PLAYER_BAN(PacketShowType.ACTION, PacketSenderType.OMNINETWORK),
	RESPOSE_PLAYER_GAME_LOBBIES(PacketShowType.ACTION, PacketSenderType.OMNINETWORK),
	RESPOSE_PLAYER_NETWORK_BOOSTER(PacketShowType.ACTION, PacketSenderType.OMNINETWORK),
	
	RESPOSE_GAMEPRESET_PLAYER_COUNT(PacketShowType.ACTION, PacketSenderType.OMNINETWORK),
	
	PLAYER_LOGIN_EVALUATE(PacketShowType.ACTION, PacketSenderType.ANY),
	PLAYER_LOGIN_SUCESS(PacketShowType.ACTION, PacketSenderType.ANY),
	
	PLAYER_SEND_KICK(PacketShowType.ACTION, PacketSenderType.ANY),
	PLAYER_SEND_BAN(PacketShowType.ACTION, PacketSenderType.ANY),

	GAME_LOCK_STATUS(PacketShowType.ACTION, PacketSenderType.OMNINETWORK),
	SERVER_FULL_STATUS(PacketShowType.ACTION, PacketSenderType.OMNINETWORK),
	
	GAME_ONLINE_INFO(PacketShowType.ACTION, PacketSenderType.OMNINETWORK),
	
	INITIALIZE_GAME(PacketShowType.ACTION, PacketSenderType.OMNICORE),
	
	;
	
	private PacketShowType show;
	private PacketSenderType[] senders;
	
	/**
	 * 
	 * Constructor principal de la clase.
	 * 
	 * @param show Que función maneja este paquete.
	 * @param senders A quien se le puede enviar este paquete.
	 */
	PacketType(PacketShowType show, PacketSenderType... senders){
		
		this.setShow(show);
		this.setSenders(senders);
		
	}

	public PacketShowType getShow() {
		return show;
	}

	public void setShow(PacketShowType show) {
		this.show = show;
	}

	public PacketSenderType[] getSenders() {
		return senders;
	}

	public void setSenders(PacketSenderType[] senders) {
		this.senders = senders;
	}
	
}