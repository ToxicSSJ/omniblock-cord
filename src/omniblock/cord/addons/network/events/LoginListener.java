package omniblock.cord.addons.network.events;

import java.util.Date;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import omniblock.cord.OmniCord;
import omniblock.cord.addons.network.MaintenanceManager;
import omniblock.cord.addons.phase.PhaseManager;
import omniblock.cord.addons.phase.PhaseManager.PhaseType;
import omniblock.cord.database.base.RankBase;
import omniblock.cord.database.sql.type.RankType;
import omniblock.cord.database.sql.util.Resolver;
import omniblock.cord.network.core.io.MSGPatcher;
import omniblock.cord.util.TextUtil;

public class LoginListener implements Listener {

	public static void setup() {
		
		ProxyServer.getInstance().getPluginManager().registerListener(OmniCord.getInstance(), new LoginListener());
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerNetworkJoin(PreLoginEvent e) {
		
		if(!Resolver.hasLastName(e.getConnection().getName()))
			return;
		
		Date end = RankBase.getTempRankExpireDate(e.getConnection().getName());
		
		if(end != null) {
			
			if(!end.after(new Date())) {
				
				RankBase.setRank(e.getConnection().getName(), RankType.USER);
				RankBase.removeTemporalMembership(e.getConnection().getName());
				
			}
			
		}
		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerNetworkJoin(PostLoginEvent e) {
		
		if(MaintenanceManager.maintenance) {
			
			if(Resolver.hasLastName(e.getPlayer().getName()))
				if(RankBase.getRank(e.getPlayer().getName()).isStaff()) {
					
					e.getPlayer().sendMessage(TextUtil.format("&8&lM&8antenimiento &b&l» &7Mientras se produce el mantenimiento pueden ocurrir muchos reinicios y muchos errores!"));
					return;
					
				}
					
			
			e.getPlayer().disconnect(MSGPatcher.MAINTENANCE_JOIN);
			return;
			
		}
		
		if(PhaseManager.getPhase() == PhaseType.KEY_BETA) {
			
			if(PhaseManager.getBetaKey(e.getPlayer().getName(), false).equalsIgnoreCase("NONE")) {
				
				e.getPlayer().disconnect(MSGPatcher.BETA_JOIN);
				return;
				
			}
			
		}
		
	}
	
}
