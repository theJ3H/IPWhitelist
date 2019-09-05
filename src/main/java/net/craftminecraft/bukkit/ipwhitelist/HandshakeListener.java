package net.craftminecraft.bukkit.ipwhitelist;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class HandshakeListener {

	public HandshakeListener(IPWhitelist list) {
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(list, PacketType.Handshake.Client.SET_PROTOCOL) {
			@Override
			public void onPacketReceiving(PacketEvent event) {
				String data = event.getPacket().getStrings().getValues().get(0);
				if(data.contains("\000")) {
					String hostname = data.split("\000")[0];
					InetAddress address;
					try {
						address = InetAddress.getByName(hostname);
												
						if(!list.allow(address)) {
							System.out.println("Dropped connection from "+address+" ("+data+")");
							event.setCancelled(true);
						}
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} 
				}
			}
		});
			
		
	}
	
}
