package omniblock.cord.network.packets;

import omniblock.cord.network.packets.readers.GameReader;
import omniblock.cord.network.packets.readers.PlayerReader;
import omniblock.cord.network.packets.readers.ProxyReader;
import omniblock.cord.network.packets.readers.RequestReader;
import omniblock.cord.network.packets.readers.ServerReader;

public class PacketsAdapter {

	public static void registerReaders(){
		
		GameReader.start();
		PlayerReader.start();
		ProxyReader.start();
		RequestReader.start();
		ServerReader.start();
		
	}
	
}
