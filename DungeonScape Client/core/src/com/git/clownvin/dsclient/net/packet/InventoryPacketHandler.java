package com.git.clownvin.dsclient.net.packet;

import com.git.clownvin.dsapi.packet.InventoryPacket;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.simplepacketframework.connection.Connection;
import com.git.clownvin.simplepacketframework.packet.AbstractPacketHandler;

public class InventoryPacketHandler extends AbstractPacketHandler<Connection, InventoryPacket> {

	private final DSGame game;
	
	public InventoryPacketHandler(DSGame game) {
		this.game = game;
	}
	
	@Override
	public boolean handlePacket(Connection source, InventoryPacket packet) {
		game.getGameScreen().setInventory(packet.getIDs(), packet.getNames(), packet.getAmounts());
		return true;
	}

}
