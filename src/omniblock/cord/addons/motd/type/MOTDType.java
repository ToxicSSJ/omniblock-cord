package omniblock.cord.addons.motd.type;

import omniblock.cord.addons.motd.object.MOTDPreset;

public enum MOTDType {

	COMMON_MOTD(new MOTDPreset(
			"            &9&lOmniblock Network &7&m--&r &c&lBETA \n",
			"            &7Fase de Testeos Internos ")),
	PROMOTION_MOTD(new MOTDPreset(
			"            &9&lOmniblock Network &7&m--&r &c&lBETA \n",
			"            &7Rangos al &e&l50%!! ")),
	
	MAINTENACE(new MOTDPreset(
			"            &9&lOmniblock Network &7&m--&r &c&lBETA \n",
			"            &c&lMantenimiento ")),
	
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
