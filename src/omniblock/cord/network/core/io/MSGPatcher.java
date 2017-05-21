package omniblock.cord.network.core.io;

import omniblock.cord.util.TextUtil;

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
	
}
