package com.git.clownvin.dsclient.net.packet;

import com.git.clownvin.dsapi.packet.RemoveObjectPacket;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.simplepacketframework.connection.Connection;
import com.git.clownvin.simplepacketframework.packet.AbstractPacketHandler;

public class RemoveObjectPacketHandler extends AbstractPacketHandler<Connection, RemoveObjectPacket> {
	
	private final DSGame game;
	
	public RemoveObjectPacketHandler(DSGame game) {
		this.game = game;
	}
	
	@Override
	public boolean handlePacket(Connection source, RemoveObjectPacket packet) {
		game.getObjects().removeObject(packet.getID());
		return true;
	}

}
