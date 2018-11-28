package com.git.clownvin.dsapi.packet;

import com.git.clownvin.simplepacketframework.packet.Packet;

public class MoveCharacterPacket extends Packet {
	
	private int id;
	private float x, y, moveSpeed;
	
	public MoveCharacterPacket(int id, float moveSpeed, float x, float y) {
		this(false, toBytes(id, moveSpeed, x, y), -1);
		this.id = id;
		this.moveSpeed = moveSpeed;
		this.x = x;
		this.y = y;
	}
	
	public int getID() {
		return id;
	}
	
	public float getMoveSpeed() {
		return moveSpeed;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}

	public MoveCharacterPacket(boolean construct, byte[] bytes, int length) {
		super(construct, bytes, length);
	}
	
	private static byte[] toBytes(int id, float moveSpeed, float x, float y) {
		byte[] bytes = new byte[16];
		int i = 0, j;
		//ID
		bytes[i++] = (byte) ((id >> 24) & 0xFF);
		bytes[i++] = (byte) ((id >> 16) & 0xFF);
		bytes[i++] = (byte) ((id >> 8) & 0xFF);
		bytes[i++] = (byte) (id & 0xFF);
		//moveSpeed
		j = Float.floatToIntBits(moveSpeed);
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//x
		j = Float.floatToIntBits(x);
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//y
		j = Float.floatToIntBits(y);
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		return bytes;
	}

	@Override
	protected void construct(byte[] bytes, int length) {
		int i = 0;
		id = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
		moveSpeed = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		x = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		y = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
	}

	@Override
	public boolean shouldEncrypt() {
		// TODO Auto-generated method stub
		return false;
	}

}
