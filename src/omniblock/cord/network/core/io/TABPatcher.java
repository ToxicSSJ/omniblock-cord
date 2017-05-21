package omniblock.cord.network.core.io;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import omniblock.cord.OmniCord;
import omniblock.cord.util.TextUtil;

public class TABPatcher implements Listener {
	
	public static void setup(){
		
		OmniCord.getPlugin().getProxy().getPluginManager().registerListener(OmniCord.getPlugin(), new TABPatcher());
		
	}
	
	@EventHandler
	public void onLogin(PostLoginEvent e){
		
		TextComponent header = new TextComponent(TextUtil.format("&bEstás jugando en &9&lMC.OMNIBLOCK.NET &8&l: &a&l" + "#"));
		TextComponent footer = new TextComponent(TextUtil.format("&bOmniblock Network (c) &8| &bParche: &71.12 &8| &bwww.omniblock.net"));
		
		e.getPlayer().setTabHeader(header, footer);
		
	}
	
	@EventHandler
	public void onSwitch(ServerSwitchEvent e){
		
		TextComponent header = new TextComponent(TextUtil.format("&bEstás jugando en &9&lMC.OMNIBLOCK.NET &8&l: &a" + e.getPlayer().getServer().getInfo().getName()));
		TextComponent footer = new TextComponent(TextUtil.format("&bOmniblock Network (c) &8| &bParche: &71.12 &8| &bwww.omniblock.net"));
		
		e.getPlayer().setTabHeader(header, footer);
		
	}
	
}
