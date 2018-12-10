package com.git.clownvin.dsclient.net.packet;

import com.badlogic.gdx.graphics.Color;
import com.git.clownvin.dsapi.packet.MessagePacket;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.simplepacketframework.connection.Connection;
import com.git.clownvin.simplepacketframework.packet.AbstractPacketHandler;

public class MessagePacketHandler extends AbstractPacketHandler<Connection, MessagePacket> {
	
	private final DSGame game;
	
	public MessagePacketHandler(DSGame game) {
		this.game = game;
	}
	
	@Override
	public boolean handlePacket(Connection source, MessagePacket packet) {
		Color color = Color.YELLOW;
		switch (packet.getColor()) {
		case MessagePacket.BLACK:
			color = Color.BLACK;
			break;
		case MessagePacket.WHITE:
			color = Color.WHITE;
			break;
		case MessagePacket.YELLOW:
			color = Color.YELLOW;
			break;
		case MessagePacket.BLUE:
			color = Color.BLUE;
			break;
		case MessagePacket.CYAN:
			color = Color.CYAN;
			break;
		case MessagePacket.GREEN:
			color = Color.GREEN;
			break;
		case MessagePacket.MAGENTA:
			color = Color.MAGENTA;
			break;
		case MessagePacket.PURPLE:
			color = Color.PURPLE;
			break;
		case MessagePacket.ORANGE:
			color = Color.ORANGE;
			break;
		case MessagePacket.RED:
			color = Color.RED;
			break;
		default: 
			System.out.println("No case for color code: "+packet.getColor());
		}
		if (packet.getSenderID() == MessagePacket.SYSTEM) {
			game.getGameScreen().addChatMessage(packet.getMessage(), color);
		} else {
			game.getGameScreen().addChatMessage(game.getCharacters().getCharacter(packet.getSenderID()).getName()+": "+packet.getMessage(), color);
			game.getCharacters().getCharacter(packet.getSenderID()).setMessage(packet.getMessage(), color);
		}
		return true;
	}

}
