package com.git.clownvin.dsclient.net.packet;

import com.git.clownvin.dsapi.packet.RemoveCharacterPacket;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.dsclient.entity.character.ClientCharacter;
import com.git.clownvin.simplepacketframework.connection.Connection;
import com.git.clownvin.simplepacketframework.packet.AbstractPacketHandler;

public class RemoveCharacterPacketHandler extends AbstractPacketHandler<Connection, RemoveCharacterPacket> {

	private final DSGame game;
	
	public RemoveCharacterPacketHandler(DSGame game) {
		this.game = game;
	}
	
	@Override
	public boolean handlePacket(Connection source, RemoveCharacterPacket packet) {
		ClientCharacter character = game.getCharacters().getCharacter(packet.getID());
		if (character.getEID() == game.getSelfID())
			return true; // We're not removing ourself lol
		game.getCharacters().removeCharacter(character);
		return true;
	}

}
