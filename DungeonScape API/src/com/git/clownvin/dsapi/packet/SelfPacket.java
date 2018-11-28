package com.git.clownvin.dsapi.packet;

import com.git.clownvin.simplepacketframework.packet.Packet;

public class SelfPacket extends Packet {
	
	private int selfID;
	
	public SelfPacket(int selfID) {
		this(false, toBytes(selfID), -1);
		this.selfID = selfID;
	}
	
	public int getSelfID() {
		return selfID;
	}

	public SelfPacket(boolean construct, byte[] bytes, int length) {
		super(construct, bytes, length);
	}
	
	private static byte[] toBytes(int seldID) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) ((seldID >> 24) & 0xFF);
		bytes[1] = (byte) ((seldID >> 16) & 0xFF);
		bytes[2] = (byte) ((seldID >> 8) & 0xFF);
		bytes[3] = (byte) (seldID & 0xFF);
		return bytes;
	}

	@Override
	protected void construct(byte[] bytes, int length) {
		selfID = ((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) << 16) | ((bytes[2] & 0xFF) << 8) | (bytes[3] & 0xFF);
	}

	@Override
	public boolean shouldEncrypt() {
		return false;
	}

}
