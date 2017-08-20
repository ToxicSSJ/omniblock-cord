package net.omniblock.packets;

import net.omniblock.packets.object.external.SystemType;

/**
 * 
 * Clase principal del sistema de paquetes
 * en la cual se debe ejecutar el metodo 
 * principal con sus argumentos para comprobar
 * el estado y el uso de esta libreria.
 * 
 * @author zlToxicNetherlz
 *
 */
public class OmniPackets {

	protected static SystemType SYSTEM_TYPE = null;
	protected static boolean STARTED = false;
	
	/**
	 * 
	 * Con este metodo se inicializa el sistema de desbloqueo
	 * de paquetes con el fin de que dichos paquetes se puedan
	 * empezar a utilizar tanto el lector, como el escritor.
	 * 
	 * @param type El tipo de sistema sobre el cual corre la libreria.
	 */
	public static void setupSystem(SystemType type){
		
		STARTED = true;
		SYSTEM_TYPE = type;
		return;
		
	}
	
	/**
	 * 
	 * ¿Se inicializó el sistema correctamente?
	 * 
	 * @return <strong>true</strong> si el sistema se inicializó correctamente.
	 */
	public static boolean isStarted(){
		return STARTED;
	}
	
}