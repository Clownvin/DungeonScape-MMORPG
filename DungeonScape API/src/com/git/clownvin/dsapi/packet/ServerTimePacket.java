package com.git.clownvin.dsapi.packet;

import com.git.clownvin.simplepacketframework.packet.Packet;

public class ServerTimePacket extends Packet {
	
	private long serverTime;
	
	public ServerTimePacket(long serverTime) {
		this(false, toBytes(serverTime), -1);
		this.serverTime = serverTime;
	}

	public ServerTimePacket(boolean construct, byte[] bytes, int length) {
		super(construct, bytes, length);
	}
	
	public long getServerTime() {
		return serverTime;
	}
	
	private static byte[] toBytes(long serverTime) {
		byte[] bytes = new byte[8];
		bytes[0] = (byte) ((serverTime >> 56) & 0xFF);
		bytes[1] = (byte) ((serverTime >> 48) & 0xFF);
		bytes[2] = (byte) ((serverTime >> 40) & 0xFF);
		bytes[3] = (byte) ((serverTime >> 32) & 0xFF);
		bytes[4] = (byte) ((serverTime >> 24) & 0xFF);
		bytes[5] = (byte) ((serverTime >> 16) & 0xFF);
		bytes[6] = (byte) ((serverTime >> 8) & 0xFF);
		bytes[7] = (byte) (serverTime & 0xFF);
		return bytes;
	}

	@Override
	protected void construct(byte[] bytes, int length) {
		serverTime = ((bytes[0] & 0xFFL) << 56) | ((bytes[1] & 0xFFL) << 48) | ((bytes[2] & 0xFFL) << 40) | ((bytes[3] & 0xFFL) << 32) | ((bytes[4] & 0xFFL) << 24) | ((bytes[5] & 0xFFL) << 16) | ((bytes[6] & 0xFFL) << 8) | (bytes[7] & 0xFFL);
	}

	@Override
	public boolean shouldEncrypt() {
		return false;
	}

}
