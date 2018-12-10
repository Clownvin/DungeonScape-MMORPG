package com.git.clownvin.dsclient.net.packet;

import com.git.clownvin.dsapi.packet.MoveCharacterPacket;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.dsclient.entity.character.ClientCharacter;
import com.git.clownvin.simplepacketframework.connection.Connection;
import com.git.clownvin.simplepacketframework.packet.AbstractPacketHandler;

public class MoveCharacterPacketHandler extends AbstractPacketHandler<Connection, MoveCharacterPacket> {

	private final DSGame game;
	
	public MoveCharacterPacketHandler(DSGame game) {
		this.game = game;
	}
	
	@Override
	public boolean handlePacket(Connection source, MoveCharacterPacket packet) {
		ClientCharacter character = game.getCharacters().getCharacter(packet.getID());
		//System.out.println("Character: "+character);
		character.setX(packet.getX());
		character.setY(packet.getY());
		character.setMoveSpeed(packet.getMoveSpeed());
		return true;
	}

}
