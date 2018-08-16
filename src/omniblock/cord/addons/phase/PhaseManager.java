package omniblock.cord.addons.phase;

import omniblock.cord.database.base.BetaKeyBase;
import omniblock.cord.database.sql.util.Resolver;
import omniblock.cord.util.TextUtil;

public class PhaseManager {
	
	protected static PhaseType actualType = PhaseType.OPEN_BETA;
	
	public static PhaseType getPhase() {
		return actualType;
	}
	
	/**
	 * 
	 * Este metodo deberá ser unicamente utilizado durante la
	 * fase KEY_BETA, debido a que es de gran importancia al
	 * momento de recibir o generar una key y registrarla en la 
	 * base de datos.
	 * 
	 * @param playername El nombre del jugador al cual se le
	 * registrará la KEY
	 * @param generate Si es true en caso de que el jugador no
	 * tenga una KEY generarla.
	 * @return La KEY o también podrá retornar 'NONE' si no
	 * existe alguna key.
	 */
	public static String getBetaKey(String playername, boolean generate) {
		
		if(!Resolver.hasLastName(playername))
			return "NONE";
		
		String key = BetaKeyBase.getKey(playername);
		
		if(key.equalsIgnoreCase("NONE") && generate)
			return BetaKeyBase.insertKey(playername);
		
		return key;
		
	}
	
	/**
	 * 
	 * Este metodo es importante al momento de removerle
	 * la beta key a un jugador en caso de que exista.
	 * 
	 * @param playername El nombre del jugador.
	 */
	public static void removeBetaKey(String playername) {
		
		BetaKeyBase.removeKey(playername);
		return;
		
	}
	
	public static enum PhaseType {
		
		CLOSED_BETA(0, TextUtil.format("&c&lBETA CERRADA")),
		KEY_BETA(1, TextUtil.format("&6&lBETA KEYS")),
		OPEN_BETA(2, TextUtil.format("&a&lBETA ABIERTA")),
		V1_38(3, TextUtil.format("&9&lv1.38")),
		
		;
		
		private String motd;
		protected int version;
		
		PhaseType(int version, String motd){
			
			this.motd = motd;
			this.version = version;
			
		}

		public int getVersion() {
			return version;
		}
		
		public String getMotd() {
			return motd;
		}
		
	}
	
}
