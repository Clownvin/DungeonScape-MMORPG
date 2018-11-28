package com.git.clownvin.dsapi.packet;

import com.git.clownvin.simplepacketframework.packet.Packet;

public class ClientStatusPacket extends Packet {
	
	public static final short READY = 1;
	
	private short status;
	
	public ClientStatusPacket(short status) {
		this(false, toBytes(status), -1);
		this.status = status;
	}

	public ClientStatusPacket(boolean construct, byte[] bytes, int length) {
		super(construct, bytes, length);
	}
	
	public short getStatus() {
		return status;
	}
	
	private static byte[] toBytes(short status) {
		byte[] bytes = new byte[2];
		bytes[0] = (byte) ((status >> 8) & 0xFF);
		bytes[1] = (byte) (status & 0xFF);
		return bytes;
	}

	@Override
	protected void construct(byte[] bytes, int length) {
		status = (short) (((bytes[0] & 0xFF) << 8) | (bytes[1] & 0xFF));
	}

	@Override
	public boolean shouldEncrypt() {
		// TODO Auto-generated method stub
		return false;
	}

}
