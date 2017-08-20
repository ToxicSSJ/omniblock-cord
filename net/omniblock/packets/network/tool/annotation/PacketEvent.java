package net.omniblock.packets.network.tool.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.omniblock.packets.network.tool.annotation.type.PacketPriority;

/**
 * 
 * Esta anotación se usa en la clase
 * PacketReader, especificamente en el
 * metodo "readPacket".
 * 
 * @author zlToxicNetherlz
 *
 */
@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PacketEvent {

	/**
	 * @return La prioridad del paquete.
	 */
	PacketPriority priority() default PacketPriority.NORMAL;
	
}
