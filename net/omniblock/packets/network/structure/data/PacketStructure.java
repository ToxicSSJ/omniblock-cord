package net.omniblock.packets.network.structure.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Base64;

import net.omniblock.packets.object.PacketObject;

/**
 * 
 * Esta clase es la estructura principal de toda
 * clase PacketSocketData, la cual almacena las
 * variables de los paquetes en un formato amigable
 * e immutable para ser editados y añadidos con mucha
 * facilidad.
 * 
 * @author zlToxicNetherlz
 *
 */
@SuppressWarnings("unused")
public class PacketStructure implements Serializable {
	
	private static final long serialVersionUID = 1626578598217162795L;
	
	private PacketObject<String> Strings = new PacketObject<String>(DataType.STRINGS);
	private PacketObject<Character> Chars = new PacketObject<Character>(DataType.CHARS);
	
	private PacketObject<Integer> Integers = new PacketObject<Integer>(DataType.INTEGERS);
	private PacketObject<Boolean> Booleans = new PacketObject<Boolean>(DataType.BOOLEANS);
	
	private PacketObject<Short> Shorts = new PacketObject<Short>(DataType.SHORTS);
	private PacketObject<Byte> Bytes = new PacketObject<Byte>(DataType.BYTES);
	private PacketObject<Double> Doubles = new PacketObject<Double>(DataType.DOUBLES);
	private PacketObject<Float> Floats = new PacketObject<Float>(DataType.FLOATS);
	private PacketObject<Long> Longs = new PacketObject<Long>(DataType.LONGS);
	
	/**
	 * 
	 * Este metodo permite definir un valor dentro de un
	 * PacketObject (Se basa del tipo de dato del PacketObject)
	 * con el fin de funcionar a base del sistema.
	 * 
	 * @param type El tipo de dato que se va a definir.
	 * @param key La identidad del dato que se va a definir.
	 * @param value El valor (objeto) del dato que se va a definir.
	 * @return La instancia de la estructura para funcionar de manera Immutable.
	 */
	public PacketStructure set(DataType type, String key, Object value) {
		
		try {
			
			for(Field f : this.getClass().getDeclaredFields()){
				
				if(f.getName() == type.getFieldName()){
					
					f.setAccessible(true);
					
					PacketObject<?> object = (PacketObject<?>) f.get(this);
					object.setValue(key, value);
					
					f.set(this, object);
					return this;
					
				}
				
			}
			
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return this;
		
	}
	
	/**
	 * 
	 * Este metodo permite obtener el valor de un objeto
	 * en base a su identidad y el tipo de valor como tál
	 * definido por el enumerador y por el metodo {@link #set(DataType, String, Object)}
	 * donde se predefinin los valores que poseerá este
	 * objeto.
	 * 
	 * @param <T> El tipo de valor que se desea recibir (El definido en el DataType, pero en formato clase como tál)
	 * @param type El tipo de Dato que se va a obtener.
	 * @param key La identidad del valor que se buscará.
	 * @return El objeto como tál cuyo tipo de retorno esta definido por T.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(DataType type, String key) {
		
		try {
			
			Field field = PacketStructure.class.getField(type.getFieldName());
			
			PacketObject<T> object = (PacketObject<T>) field.get(this);
			return object.getValue(key);
			
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
	/**
	 * 
	 * Este enumerador posee todos
	 * los tipos de datos disponibles
	 * para ser serializados por el
	 * PacketStructure.
	 * 
	 * @author zlToxicNetherlz
	 *
	 */
	public static enum DataType {
		
		INTEGERS("Integers", Integer.class),
		BOOLEANS("Booleans", Boolean.class),
		SHORTS("Shorts", Short.class),
		BYTES("Bytes", Byte.class),
		DOUBLES("Doubles", Double.class),
		FLOATS("Floats", Float.class),
		LONGS("Longs", Long.class),
		STRINGS("Strings", String.class),
		CHARS("Chars", Character.class),
		
		;
		
		String fieldName;
		Class<?> clazz;
		
		DataType(String fieldName, Class<?> clazz){
			
			this.fieldName = fieldName;
			this.clazz = clazz;
			
		}
		
		public Class<?> getGenericClass(){
			return clazz;
		}
		
		public String getFieldName(){
			return fieldName;
		}
		
	}
	
	/**
	 * 
	 * Esta clase es el Serializador del objeto
	 * cuyas funciones son utiles a la hora de
	 * leer y encriptar (serializar) la información.
	 * 
	 * @author zlToxicNetherlz
	 *
	 */
	public static class PacketStructureHandler {
		
		public static PacketStructure deserialize(String str) {
			try {
				
				byte [] data = Base64.getDecoder().decode(str);
		        ObjectInputStream ois = new ObjectInputStream( 
		                                        new ByteArrayInputStream(data));
		        Object o  = ois.readObject();
		        ois.close();
		        return (PacketStructure) o;
			    
			} catch (Exception e) {
				
				e.printStackTrace();
				return new PacketStructure();
				
			}
	    }

		public static String serialize(PacketStructure obj) {
			try {
				
				 ByteArrayOutputStream baos = new ByteArrayOutputStream();
			     ObjectOutputStream oos = new ObjectOutputStream( baos );
			     oos.writeObject(obj);
			     oos.close();
			     return Base64.getEncoder().encodeToString(baos.toByteArray());
			     
			 } catch (Exception e) {
				 
			     e.printStackTrace();
			     return null;
			     
			 }
		}
		
	}
	
}
