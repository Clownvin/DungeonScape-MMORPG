package com.git.clownvin.dsclient.net.packet;

import com.git.clownvin.dsapi.packet.CharacterPacket;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.dsclient.entity.character.ClientCharacter;
import com.git.clownvin.simplepacketframework.connection.Connection;
import com.git.clownvin.simplepacketframework.packet.AbstractPacketHandler;

public class CharacterPacketHandler extends AbstractPacketHandler<Connection, CharacterPacket> {

	private final DSGame game;
	
	public CharacterPacketHandler(DSGame game) {
		this.game = game;
	}
	
	@Override
	public boolean handlePacket(Connection source, CharacterPacket packet) {
		if (game.getCharacters().getCharacter(packet.getID()) != null)
			return true;
		//System.out.println("recieved char: "+cp2.getName()+", "+cp2.getID());
		ClientCharacter character = new ClientCharacter(game, packet.getX(), packet.getY(), packet.getWidth(), packet.getHeight(), packet.getLastX(), packet.getLastY(), packet.getID(), packet.getHP(), packet.getSprite(), packet.getName(), packet.getAffiliation());
		character.setLookX(packet.getLookX());
		character.setLookY(packet.getLookY());
		game.getCharacters().addCharacter(character);
		return true;
	}

}
