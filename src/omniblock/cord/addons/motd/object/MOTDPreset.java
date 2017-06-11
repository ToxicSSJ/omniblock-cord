package omniblock.cord.addons.motd.object;

/**
 * 
 * Esta clase que funciona com constructor
 * puede almacenar las dos lineas que se usan
 * en un MOTD, para luego ser utilizado en la base
 * del sistema.
 * 
 * @author zlToxicNetherlz
 *
 */
public class MOTDPreset {
	
	private String line1;
	private String line2;
	
	public MOTDPreset(String line1, String line2){
		
		this.line1 = line1;
		this.line2 = line2;
		
	}
	
	public String getLine(int number) {
		
		return number == 1 ? line1 : line2;
		
	}
	
}
