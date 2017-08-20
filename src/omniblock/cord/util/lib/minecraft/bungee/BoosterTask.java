package omniblock.cord.util.lib.minecraft.bungee;

import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.scheduler.ScheduledTask;
import omniblock.cord.OmniCord;
import omniblock.cord.network.packets.PacketsTools;

public class BoosterTask {

	public ScheduledTask task;
	
	public String gametype;
	public String player;
	
	public BoosterTask(String player, String gametype, int duration){
		
		this.player = player;
		this.gametype = gametype;
		
		task = OmniCord.getInstance().getProxy().getScheduler().schedule(OmniCord.getInstance(), new Runnable(){

			int rest = duration;
			
			@Override
			public void run() {
				
				if(rest <= 0){
            		
            		PacketsTools.NETWORK_BOOSTERS.put(gametype, null);
            		task.cancel();
            		return;
            		
            	}
            	
            	rest--;
				
			}
			
		}, 1, 1, TimeUnit.SECONDS);
		
	}
	
	public String getGameType(){
		return gametype;
	}
	
	public String getPlayer(){
		return player;
	}
	
	public ScheduledTask getScheduledTask(){
		return task;
	}
	
}
