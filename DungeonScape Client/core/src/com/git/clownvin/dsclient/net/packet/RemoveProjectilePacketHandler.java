package com.git.clownvin.dsclient.net.packet;

import com.git.clownvin.dsapi.packet.RemoveProjectilePacket;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.simplepacketframework.connection.Connection;
import com.git.clownvin.simplepacketframework.packet.AbstractPacketHandler;

public class RemoveProjectilePacketHandler extends AbstractPacketHandler<Connection, RemoveProjectilePacket> {
	
	private final DSGame game;
	
	public RemoveProjectilePacketHandler(DSGame game) {
		this.game = game;
	}

	@Override
	public boolean handlePacket(Connection source, RemoveProjectilePacket packet) {
		game.getProjectiles().removeProjectile(packet.getID());
		return true;
	}

}
