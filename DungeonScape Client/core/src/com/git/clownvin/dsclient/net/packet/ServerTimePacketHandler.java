package com.git.clownvin.dsclient.net.packet;

import com.git.clownvin.dsapi.packet.ServerTimePacket;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.simplepacketframework.connection.Connection;
import com.git.clownvin.simplepacketframework.packet.AbstractPacketHandler;

public class ServerTimePacketHandler extends AbstractPacketHandler<Connection, ServerTimePacket> {

	private final DSGame game;
	
	public ServerTimePacketHandler(DSGame game) {
		this.game = game;
	}
	
	@Override
	public boolean handlePacket(Connection source, ServerTimePacket packet) {
		game.setServerTime(packet.getServerTime());
		return true;
	}

}
