package omniblock.cord.addons.network;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import omniblock.cord.OmniCord;

public class TABManager implements Listener {

	public static void start(){
		
		OmniCord.getPlugin().getProxy().getPluginManager().registerListener(OmniCord.getPlugin(), new TABManager());
		
	}
	
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTabComplete(TabCompleteEvent ev) {
    	
    	ev.setCancelled(true);
    	ev.getSuggestions().clear();
    	
        String partialPlayerName = ev.getCursor().toLowerCase();

        int lastSpaceIndex = partialPlayerName.lastIndexOf(' ');
        if (lastSpaceIndex >= 0) {
            partialPlayerName = partialPlayerName.substring(lastSpaceIndex + 1);
        }

        for (ProxiedPlayer p : OmniCord.getInstance().getProxy().getPlayers()) {
            if (p.getName().toLowerCase().startsWith(partialPlayerName)) {
                ev.getSuggestions().add(p.getName());
            }
        }
        
    }
	
}
