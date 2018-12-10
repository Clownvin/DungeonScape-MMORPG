package com.git.clownvin.dsserver.packets;

import com.git.clownvin.dsapi.packet.VelocityPacket;
import com.git.clownvin.dsserver.connection.UserConnection;
import com.git.clownvin.simplepacketframework.packet.AbstractPacketHandler;

public class VelocityPacketHandler extends AbstractPacketHandler<UserConnection, VelocityPacket> {

	@Override
	public boolean handlePacket(UserConnection source, VelocityPacket packet) {
		source.getCharacter().setVelocityX(packet.getVelocityX());
		source.getCharacter().setVelocityY(packet.getVelocityY());
		source.getCharacter().setGear(packet.getSpeed());
		if (!source.getCharacter().isMoving() && (packet.getVelocityX() != 0.0f || packet.getVelocityY() != 0.0f))
			source.getCharacter().startMoving();
		else if (source.getCharacter().isMoving() && (packet.getVelocityX() == 0.0f && packet.getVelocityY() == 0.0f))
			source.getCharacter().stopMoving();
		return true;
	}

}
