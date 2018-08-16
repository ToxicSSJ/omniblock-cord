package omniblock.cord.network.core.io;

import omniblock.cord.util.TextUtil;

/**
 * 
 * Esta clase podrá alamacenar funciones
 * extras en base a mensajes estaticos dentro
 * de sus variables o metodos utiles.
 * 
 * @author zlToxicNetherlz
 *
 */
public class MSGPatcher {

	public static final String YOURE_BANNED_WITHOUT_VARS = TextUtil.format(
			
			"\n&4&l¡Estás baneado!" +
			" \n" +
			"&7Nuestro registro ha indicado que posees un baneo general\n" +
			"&7porque no has cumplido con las normas o politicas de Omniblock Network.\n " +
			" \n" +
			"&7Cualquier duda, queja o pregunta con respecto a la politica\n" +
			"&7o el manejo de sanciones, incluyendo la tuya. Dirigete al\n" +
			"&7siguiente link: &bwww.omniblock.net/baneos/\n"
					
			);
	
	public static final String YOURE_KICKED = TextUtil.format(
			
			"\n&4&l¡Has sido expulsado!" +
			" \n" +
			"&7Se te ha expulsado porque no has cumplido con las normas o politicas\n" +
			"&7de Omniblock Network. Esta expulsión es una advertencia debido a que\n " +
			"&7quedará registrada y podrás recibir una sanción mayor la proxima vez.\n " +
			" \n" +
			"&8Información de la Expulsión:\n" +
			"&6&lEXPULSANTE:&7 VAR_KICK_MOD\n" +
			"&6&lRAZÓN:&7 VAR_KICK_REASON\n" +
			" \n" +
			"&7Cualquier duda, queja o pregunta con respecto a la politica\n" +
			"&7o el manejo de sanciones, incluyendo la tuya. Dirigete al\n" +
			"&7siguiente link: &bwww.omniblock.net/baneos/\n"
					
			);
	
	public static final String MAINTENANCE_KICKED = TextUtil.format(
			
			"\n&c&lMANTENIMIENTO" +
			" \n" +
			"&7Se te ha expulsado mientras efectuamos un mantenimiento programado,\n" +
			"&7recuerda que este tipo de acciones pueden conllevar mucho tiempo\n " +
			"&7pero lo hacemos para añadir o mejorar cosas a la Network.\n " +
			" \n" +
			"&7Para obtener mas información dirigete al siguiente\n" +
			"&7link: &bforo.omniblock.net\n"
					
			);
	
	public static final String MAINTENANCE_JOIN = TextUtil.format(
			
			"\n&c&lMANTENIMIENTO" +
			" \n" +
			"&7No podrás ingresar al servidor mientras se efectua un mantenimiento\n" +
			"&7programado, recuerda que este tipo de acciones pueden conllevar mucho\n " +
			"&7tiempo pero lo hacemos para añadir o mejorar cosas a la Network.\n " +
			" \n" +
			"&7Para obtener mas información dirigete al siguiente\n" +
			"&7link: &bforo.omniblock.net\n"
					
			);
	
}
