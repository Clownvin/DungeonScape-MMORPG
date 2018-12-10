package com.git.clownvin.dsclient.net.packet;

import com.git.clownvin.dsapi.packet.ProjectilePacket;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.dsclient.entity.projectile.ClientProjectile;
import com.git.clownvin.simplepacketframework.connection.Connection;
import com.git.clownvin.simplepacketframework.packet.AbstractPacketHandler;

public class ProjectilePacketHandler extends AbstractPacketHandler<Connection, ProjectilePacket> {
	
	private final DSGame game;
	
	public ProjectilePacketHandler(DSGame game) {
		this.game = game;
	}

	@Override
	public boolean handlePacket(Connection source, ProjectilePacket packet) {
		game.getProjectiles().addProjectile(new ClientProjectile(game, packet.getX(), packet.getY(), packet.getWidth(), packet.getHeight(), packet.getSprite(), packet.getMoveSpeed(), packet.getVelocityX(), packet.getVelocityY(), packet.getCreationTime(), packet.getSourceID(), packet.getID(), packet.getAffiliation()));
		return true;
	}

}
