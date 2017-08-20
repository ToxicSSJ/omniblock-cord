package net.omniblock.packets.object;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.omniblock.packets.network.structure.data.PacketStructure.DataType;

/**
 * 
 * Esta clase es básicamente un almacenamiento de datos
 * para cierto tipo de dato en concreto que viene definido
 * por un sistema generico.
 * 
 * @author zlToxicNetherlz
 *
 * @param <K> El tipo de dato serializable que se guardará.
 */
public class PacketObject<K> implements Serializable {

	private static final long serialVersionUID = -298385103159557337L;

	private DataType type;
	
	protected Map<String, Object> data = new HashMap<String, Object>();
	
	/**
	 * 
	 * Constructor principal de la clase.
	 * 
	 * @param type El tipo de dato que se guardará en este objeto
	 * en base al enumerador DataType.
	 * @see DataType
	 */
	public PacketObject(DataType type){
		
		this.type = type;
		
	}
	
	/**
	 * 
	 * Con este metodo se puede definir un valor de este
	 * objeto accediendo a su Map protegido y seteando el
	 * valor directo.
	 * 
	 * @param key La identidad del valor en el mapa.
	 * @param value El valor como tál (objeto) del tipo definido
	 * por K.
	 */
	public void setValue(String key, Object value){
		
		data.put(key, value);
		return;
		
	}
	
	/**
	 * 
	 * Con este metodo se puede recibir un valor de este objeto
	 * accediendo a su Map protegido y obteniendo el valor como
	 * una key de un mapa.
	 * 
	 * @param key La identidad del valor en el mapa.
	 * @return El valor como tál (objeto) definido en dicha identidad.
	 */
	@SuppressWarnings("unchecked")
	public K getValue(String key) {
		
		if(data.containsKey(key)) throw new RuntimeException("No se pudo encontrar el dato '" + key + "' en los objetos tipo " + type.toString() + " del paquete " + this.hashCode() + "!");
		
		return (K) data.get(key);
		
	}
	
	public DataType getType(){
		return type;
	}
	
}
