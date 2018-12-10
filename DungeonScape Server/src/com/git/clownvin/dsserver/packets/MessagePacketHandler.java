package com.git.clownvin.dsserver.packets;

import com.git.clownvin.dsapi.packet.MessagePacket;
import com.git.clownvin.dsapi.world.Chunk;
import com.git.clownvin.dsserver.commands.Commands;
import com.git.clownvin.dsserver.connection.UserConnection;
import com.git.clownvin.dsserver.world.Instances;
import com.git.clownvin.simplepacketframework.packet.AbstractPacketHandler;

public class MessagePacketHandler extends AbstractPacketHandler<UserConnection, MessagePacket> {

	@Override
	public boolean handlePacket(UserConnection source, MessagePacket packet) {
		String messageString = packet.getMessage();
		if (messageString.startsWith("/")) {
			Commands.handleCommand(messageString.substring(1), source);
		} else {
			MessagePacket message = new MessagePacket(messageString, source.getCharacter().getEID(), MessagePacket.WHITE);
			Chunk chunk = Instances.get(source.getProfile().instanceNumber).getChunk(source.getCharacter().getIX(), source.getCharacter().getIX());
			Instances.get(source.getProfile().instanceNumber).sendPacketToChunks(chunk.x, chunk.y, message);
		}
		return true;
	}

}
