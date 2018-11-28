package com.git.clownvin.dsapi.packet;

import com.git.clownvin.simplepacketframework.packet.Packet;

public class ChunkRequest extends Packet {
	
	private int x, y;
	
	private static byte[] toBytes(int x, int y) {
		byte[] bytes = new byte[8];
		int i = 0;
		//x
		bytes[i++] = (byte) ((x >> 24) & 0xFF);
		bytes[i++] = (byte) ((x >> 16) & 0xFF);
		bytes[i++] = (byte) ((x >> 8) & 0xFF);
		bytes[i++] = (byte) (x & 0xFF);
		//y
		bytes[i++] = (byte) ((y >> 24) & 0xFF);
		bytes[i++] = (byte) ((y >> 16) & 0xFF);
		bytes[i++] = (byte) ((y >> 8) & 0xFF);
		bytes[i++] = (byte) (y & 0xFF);
		return bytes;
	}
	
	public ChunkRequest(int x, int y) {
		this(false, toBytes(x, y), -1);
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public ChunkRequest(boolean construct, byte[] bytes, int length) {
		super(construct, bytes, length);
	}

	@Override
	protected void construct(byte[] bytes, int length) {
		int i = 0;
		x = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
		y = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
	}

	@Override
	public boolean shouldEncrypt() {
		// TODO Auto-generated method stub
		return false;
	}

}
