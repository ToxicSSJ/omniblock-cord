package omniblock.cord.addons.motd.type;

import omniblock.cord.addons.motd.object.MOTDPreset;

/**
 * 
 * Esta clase tiene los diferentes MOTD que maneja
 * el servidor con el fin de que puedan ser cambiados
 * dinamicamente.
 * 
 * @author zlToxicNetherlz
 *
 */
public enum MOTDType {

	COMMON_MOTD(new MOTDPreset(
			"            &9Omniblock Network &7➧ &cBETA CERRADA \n",
			"            &8&l★ &7¡Proximamente fecha de Apertura! &8&l★")),
	PROMOTION_MOTD(new MOTDPreset(
			"            &9Omniblock Network &7➧ &cBETA CERRADA \n",
			"            &8&l★ &7¡Proximamente fecha de Apertura! &8&l★")),
	
	MAINTENACE(new MOTDPreset(
			"            &9Omniblock Network &7➧ &cBETA CERRADA \n",
			"            &8&l★ &7¡Proximamente fecha de Apertura! &8&l★")),
	
	;
	
	private MOTDPreset preset;
	
	MOTDType(MOTDPreset preset){
		
		this.preset = preset;
		
	}

	public MOTDPreset getPreset() {
		return preset;
	}

	public void setPreset(MOTDPreset preset) {
		this.preset = preset;
	}
	
}
