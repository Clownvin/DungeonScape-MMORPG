package com.git.clownvin.dsclient.net.packet;

import com.git.clownvin.dsapi.packet.CharacterStatusPacket;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.dsclient.entity.character.ClientCharacter;
import com.git.clownvin.simplepacketframework.connection.Connection;
import com.git.clownvin.simplepacketframework.packet.AbstractPacketHandler;

public class CharacterStatusPacketHandler extends AbstractPacketHandler<Connection, CharacterStatusPacket> {

	private final DSGame game;
	
	public CharacterStatusPacketHandler(DSGame game) {
		this.game = game;
	}
	
	@Override
	public boolean handlePacket(Connection source, CharacterStatusPacket packet) {
		ClientCharacter character = game.getCharacters().getCharacter(packet.getID());
		//	System.out.println("Recieved status packet: " + csp.getHP());
		//System.out.println("Character for id "+csp.getID()+", "+character);
		character.setHP(packet.getHP());
		character.setLookX(packet.getLookX());
		character.setLookY(packet.getLookY());
		return true;
	}

}
