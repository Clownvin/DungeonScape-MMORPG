package com.git.clownvin.dsclient.net.packet;

import com.git.clownvin.dsapi.packet.SelfPacket;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.simplepacketframework.connection.Connection;
import com.git.clownvin.simplepacketframework.packet.AbstractPacketHandler;

public class SelfPacketHandler extends AbstractPacketHandler<Connection, SelfPacket> {

	private final DSGame game;
	
	public SelfPacketHandler(DSGame game) {
		this.game = game;
	}
	
	@Override
	public boolean handlePacket(Connection source, SelfPacket packet) {
		game.setSelfID(packet.getSelfID());
		return true;
	}

}
