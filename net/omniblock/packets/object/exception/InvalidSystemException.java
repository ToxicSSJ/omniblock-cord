package net.omniblock.packets.object.exception;

/**
 * 
 * Este error se utiliza a la hora de crear
 * y utilizar el sistema como un atributo
 * para darle validez (desbloquear) el sistema
 * de tál manera que no se deba utilizar sobre
 * cualquier otro sistema no valido.
 * 
 * @author zlToxicNetherlz
 *
 */
public class InvalidSystemException extends RuntimeException {

	/**
	 * 
	 * Constructor principal de la clase.
	 * 
	 * @param string Descripción del error.
	 */
	public InvalidSystemException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 7582638904374929032L;

}
