package net.omniblock.core.protocol.manager.network.handler.modifier;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * 
 * Esta clase es el objeto serializable y fundamental de los
 * paquetes el cual puede contener cualquier tipo de información
 * primitava guardada en distintas listas y adquiribles por medio
 * de distintas funcionas. Esta clase es frecuentemente usada
 * por los sistemas que manejan los paquetes y se suele enviar
 * desde el sistema de conexión por sockets <strong>OmniCore -> OmniCord</strong> como
 * un objeto no serializado para luego ser procesado por el
 * PacketHandler.
 * 
 * @author zlToxicNetherlz
 * @see omniblock.cord.network.core.handler.PacketHandler#readPacket(String)
 */
public class PacketModifier implements Serializable {

	private static final long serialVersionUID = -2156916617122303270L;

	public static int PACKET_MODIFIER_ID = -1;
	
	private int id;
	
	protected List<Integer> Integers = new ArrayList<Integer>();
	protected List<Boolean> Booleans = new ArrayList<Boolean>();
	
	protected List<Short> Shorts = new ArrayList<Short>();
	protected List<Byte> Bytes = new ArrayList<Byte>();
	protected List<Double> Doubles = new ArrayList<Double>();
	protected List<Long> Longs = new ArrayList<Long>();
	
	protected List<String> Strings = new ArrayList<String>();
	protected List<Character> Chars = new ArrayList<Character>();
	
	public PacketModifier() {
		
		PACKET_MODIFIER_ID++;
		this.id = new Integer(PACKET_MODIFIER_ID);
		
	}

	public int getIdentity() {
		return id;
	}

	public void setIdentity(int id) {
		this.id = id;
	}
	
	public PacketModifier addInteger(Integer arg) {
		
		Integers.add(arg);
		return this;
		
	}
	
	public PacketModifier addBoolean(Boolean arg) {
		
		Booleans.add(arg);
		return this;
		
	}
	
	public PacketModifier addShort(Short arg) {
		
		Shorts.add(arg);
		return this;
		
	}
	
	public PacketModifier addByte(Byte arg) {
		
		Bytes.add(arg);
		return this;
		
	}
	
	public PacketModifier addDoubles(Double arg) {
		
		Doubles.add(arg);
		return this;
		
	}
	
	public PacketModifier addLong(Long arg) {
		
		Longs.add(arg);
		return this;
		
	}
	
	public PacketModifier addString(String arg) {
		
		Strings.add(arg);
		return this;
		
	}
	
	public PacketModifier addChar(Character arg) {
		
		Chars.add(arg);
		return this;
		
	}
	
public Integer getInteger(int arg) {
		
		return Integers.get(arg);
		
	}
	
	public Boolean getBoolean(int arg) {
		
		return Booleans.get(arg);
		
	}
	
	public Short getShort(int arg) {
		
		return Shorts.get(arg);
		
	}
	
	public Byte getByte(int arg) {
		
		return Bytes.get(arg);
		
	}
	
	public Double getDouble(int arg) {
		
		return Doubles.get(arg);
		
	}
	
	public Long getLong(int arg) {
		
		return Longs.get(arg);
		
	}
	
	public String getString(int arg) {
		
		return Strings.get(arg);
		
	}
	
	public Character getChar(int arg) {
		
		return Chars.get(arg);
		
	}
	
	@Override
	public String toString() {
		
		return "PacketModifier" +
				"{" +
				"id=" + id +
				",Integers=" + Integers.toString() + 
				",Booleans=" + Booleans.toString() + 
				",Shorts=" + Shorts.toString() + 
				",Bytes=" + Bytes.toString() + 
				",Doubles=" + Doubles.toString() + 
				",Longs=" + Longs.toString() + 
				",Strings=" + Strings.toString() +
				",Chars=" + Chars.toString() + 
				"}";
		
	}
	
	public static class PacketModifierHandler {
		
		public static PacketModifier deserialize(String str) {
			try {
				
				byte [] data = Base64.getDecoder().decode(str);
		        ObjectInputStream ois = new ObjectInputStream( 
		                                        new ByteArrayInputStream(data));
		        Object o  = ois.readObject();
		        ois.close();
		        return (PacketModifier) o;
			    
			} catch (Exception e) {
				
				e.printStackTrace();
				return new PacketModifier();
				
			}
	    }

		public static String serialize(PacketModifier obj) {
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
