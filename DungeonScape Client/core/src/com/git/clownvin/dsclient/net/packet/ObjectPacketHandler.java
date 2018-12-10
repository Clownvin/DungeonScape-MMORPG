package com.git.clownvin.dsclient.net.packet;

import com.git.clownvin.dsapi.packet.ObjectPacket;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.dsclient.entity.object.ClientGameObject;
import com.git.clownvin.simplepacketframework.connection.Connection;
import com.git.clownvin.simplepacketframework.packet.AbstractPacketHandler;

public class ObjectPacketHandler extends AbstractPacketHandler<Connection, ObjectPacket> {
	
	private final DSGame game;
	
	public ObjectPacketHandler(DSGame game) {
		this.game = game;
	}

	@Override
	public boolean handlePacket(Connection source, ObjectPacket packet) {
		game.getObjects().addObject(new ClientGameObject(game, packet.getID(), packet.getX(), packet.getY(), packet.getWidth(), packet.getHeight(), packet.getSprite(), packet.getSpriteWidth(), packet.getSpriteHeight()));
		return true;
	}

}
