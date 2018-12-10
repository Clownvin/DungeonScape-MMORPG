package com.git.clownvin.dsserver.packets;

import com.git.clownvin.dsapi.packet.ActionPacket;
import com.git.clownvin.dsserver.connection.UserConnection;
import com.git.clownvin.simplepacketframework.packet.AbstractPacketHandler;

public class ActionPacketHandler extends AbstractPacketHandler<UserConnection, ActionPacket> {

	@Override
	public boolean handlePacket(UserConnection source, ActionPacket packet) {
		switch (packet.getAction()) {
		case ActionPacket.FIRE_PRIMARY:
			//System.out.println("Recieved packet w/ velocity: "+ap.getDirectionX() +", "+ ap.getDirectionY());
			source.getCharacter().firePrimary(packet.getDirectionX(), packet.getDirectionY());
			source.getCharacter().setLookX(packet.getDirectionX());
			source.getCharacter().setLookY(packet.getDirectionY());
			break;
		case ActionPacket.FIRE_SECONDARY:
			source.getCharacter().fireSecondary(packet.getDirectionX(), packet.getDirectionY());
			source.getCharacter().setLookX(packet.getDirectionX());
			source.getCharacter().setLookY(packet.getDirectionY());
			break;
		case ActionPacket.LOOK_AT:
			source.getCharacter().setLookX(packet.getDirectionX());
			source.getCharacter().setLookY(packet.getDirectionY());
			break;
		default:
			System.out.println("Unknown action: "+packet.getAction());
		}
		return true;
	}

}
