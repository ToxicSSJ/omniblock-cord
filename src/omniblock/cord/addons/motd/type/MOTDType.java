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
			"    &8✯ &9&lOmniblock Network &8✯ VAR_PHASE_NAME   \n",
			"        &61.9, 1.10, 1.11, 1.12 &8&l| &aAbierto!")),
	
	PROMOTION_MOTD(new MOTDPreset(
			"    &8✯ &9&lOmniblock Network &8✯ VAR_PHASE_NAME   \n",
			"        &61.9, 1.10, 1.11, 1.12 &8&l| &aAbierto!")),
	
	MAINTENACE(new MOTDPreset(
			"   &8✯  &9&lOmniblock Network &8✯  &c&lMANTENIMIENTO   \n",
			"      &61.9, 1.10, 1.11, 1.12 &8&l| &b&l@&bomniblockmc!")),
	
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
