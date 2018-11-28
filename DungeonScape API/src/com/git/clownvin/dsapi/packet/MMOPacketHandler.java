package com.git.clownvin.dsapi.packet;

import com.git.clownvin.simplepacketframework.connection.Connection;
import com.git.clownvin.simplepacketframework.packet.AbstractPacketHandler;
import com.git.clownvin.simplepacketframework.packet.Packets;

public abstract class MMOPacketHandler<T extends Connection> extends AbstractPacketHandler<T> {
	
	public MMOPacketHandler() {
		Packets.setPacketDefinition(1, LoginRequest.class);
		Packets.setPacketDefinition(2, LoginResponse.class);
		Packets.setPacketDefinition(3, MessagePacket.class);
		Packets.setPacketDefinition(4, ClientStatusPacket.class);
		Packets.setPacketDefinition(5, ChunkPacket.class);
		Packets.setPacketDefinition(6, ChunkRequest.class);
		Packets.setPacketDefinition(7, CharacterPacket.class);
		Packets.setPacketDefinition(8, SelfPacket.class);
		Packets.setPacketDefinition(9, VelocityPacket.class);
		Packets.setPacketDefinition(10, MoveCharacterPacket.class);
		Packets.setPacketDefinition(11, RemoveCharacterPacket.class);
		Packets.setPacketDefinition(12, ServerTimePacket.class);
		Packets.setPacketDefinition(13, ProjectilePacket.class);
		Packets.setPacketDefinition(14, RemoveProjectilePacket.class);
		Packets.setPacketDefinition(15, ActionPacket.class);
		Packets.setPacketDefinition(16, CharacterStatusPacket.class);
		Packets.setPacketDefinition(17, ObjectPacket.class);
		Packets.setPacketDefinition(18, RemoveObjectPacket.class);
		Packets.setPacketDefinition(19, InventoryPacket.class);
	}
	
}
