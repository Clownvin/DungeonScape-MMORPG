package com.git.clownvin.dsclient.net;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.git.clownvin.simplepacketframework.connection.Connection;
import com.git.clownvin.simplepacketframework.packet.PacketSystem;
import com.git.clownvin.simplescframework.AbstractClient;

public class Client extends AbstractClient<Connection> {

	public Client(final String address, final int ip, final PacketSystem packetSystem) throws UnknownHostException, IOException {
		super(new Connection(new Socket(address, ip), packetSystem));
	}

	@Override
	public void atStart() {
		System.out.println("At start");
	}

	@Override
	public void duringLoop() throws InterruptedException {
		//System.out.println("During loop...");
		Thread.sleep(5000);
	}

	@Override
	public void atStop() {
		System.out.println("At stop");
	}

}
