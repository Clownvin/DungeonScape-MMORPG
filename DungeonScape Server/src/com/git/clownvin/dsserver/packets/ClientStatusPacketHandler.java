package com.git.clownvin.dsserver.packets;

import com.git.clownvin.dsapi.packet.ClientStatusPacket;
import com.git.clownvin.dsapi.packet.MessagePacket;
import com.git.clownvin.dsserver.connection.UserConnection;
import com.git.clownvin.simplepacketframework.packet.AbstractPacketHandler;

public class ClientStatusPacketHandler extends AbstractPacketHandler<UserConnection, ClientStatusPacket> {

	@Override
	public boolean handlePacket(UserConnection source, ClientStatusPacket packet) {
		switch (packet.getStatus()) {
		case ClientStatusPacket.READY:
			source.send(new MessagePacket("Welcome to DungeonScape!", MessagePacket.SYSTEM, MessagePacket.WHITE));
			break;
		default:
			System.out.println("Unknown client status: "+packet.getStatus());
		}
		return true;
	}

}
