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
			"       &8&l『 &9&lOmniblock Network &8&l』 VAR_PHASE_NAME   \n",
			"         &61.9, 1.10, 1.11, 1.12 &8&l| &aProxiamente!")),
	
	PROMOTION_MOTD(new MOTDPreset(
			"       &8&l『 &9&lOmniblock Network &8&l』 VAR_PHASE_NAME   \n",
			"         &61.9, 1.10, 1.11, 1.12 &8&l| &aProxiamente!")),
	
	MAINTENACE(new MOTDPreset(
			"     &8&l『 &9&lOmniblock Network &8&l』 &c&lMANTENIMIENTO   \n",
			"         &61.9, 1.10, 1.11, 1.12 &8&l| &aProxiamente!")),
	
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
