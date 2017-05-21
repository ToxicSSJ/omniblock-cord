package omniblock.cord.addons.motd.object;

public class MOTDPreset {
	
	private String line1;
	private String line2;
	
	public MOTDPreset(String line1, String line2){
		
		this.line1 = line1;
		this.line2 = line2;
		
	}
	
	public String getLine(int number) {
		
		if(number == 1) {
			
			return line1;
			
		} else if(number == 2) {
			
			return line2;
			
		}
		
		return line1;
		
	}
	
}
