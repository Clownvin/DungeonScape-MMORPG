package com.git.clownvin.dsclient.net.packet;

import com.git.clownvin.dsapi.packet.ChunkPacket;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.dsclient.world.ClientChunk;
import com.git.clownvin.simplepacketframework.connection.Connection;
import com.git.clownvin.simplepacketframework.packet.AbstractPacketHandler;

public class ChunkPacketHandler extends AbstractPacketHandler<Connection, ChunkPacket> {

	private final DSGame game;
	
	public ChunkPacketHandler(DSGame game) {
		this.game = game;
	}
	
	@Override
	public boolean handlePacket(Connection source, ChunkPacket packet) {
		game.getChunks().addChunk(new ClientChunk(game, packet.getX(), packet.getY(), packet.getTiles()));
		return true;
	}

}
