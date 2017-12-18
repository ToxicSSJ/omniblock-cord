package omniblock.cord.addons.network;

import omniblock.cord.network.packets.PacketsTools;

public class MaintenanceManager {

	public static boolean maintenance = false;
	
	public static void setMaintenance(boolean status) {
		
		if(!maintenance && status)
			PacketsTools.sendMaintenance();
		
		maintenance = status;
		return;
	}
	
}
