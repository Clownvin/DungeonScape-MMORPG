package com.git.clownvin.dsapi.packet;

import com.git.clownvin.simplepacketframework.packet.PacketSystem;

public final class MMOPacketSystem extends PacketSystem {

	@Override
	public void setupPackets() {
		setPacketDefinition(1, LoginRequest.class);
		setPacketDefinition(2, LoginResponse.class);
		setPacketDefinition(3, MessagePacket.class);
		setPacketDefinition(4, ClientStatusPacket.class);
		setPacketDefinition(5, ChunkPacket.class);
		setPacketDefinition(6, ChunkRequest.class);
		setPacketDefinition(7, CharacterPacket.class);
		setPacketDefinition(8, SelfPacket.class);
		setPacketDefinition(9, VelocityPacket.class);
		setPacketDefinition(10, MoveCharacterPacket.class);
		setPacketDefinition(11, RemoveCharacterPacket.class);
		setPacketDefinition(12, ServerTimePacket.class);
		setPacketDefinition(13, ProjectilePacket.class);
		setPacketDefinition(14, RemoveProjectilePacket.class);
		setPacketDefinition(15, ActionPacket.class);
		setPacketDefinition(16, CharacterStatusPacket.class);
		setPacketDefinition(17, ObjectPacket.class);
		setPacketDefinition(18, RemoveObjectPacket.class);
		setPacketDefinition(19, InventoryPacket.class);
	}
	
}
