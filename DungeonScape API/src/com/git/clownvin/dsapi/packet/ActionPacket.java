package com.git.clownvin.dsapi.packet;

import com.git.clownvin.simplepacketframework.packet.Packet;

public class ActionPacket extends Packet {
	
	public static final short FIRE_PRIMARY = 1;
	public static final short FIRE_SECONDARY = 2;
	public static final short LOOK_AT = 3;
	
	private float directionX, directionY;
	private short action;
	
	public short getAction() {
		return action;
	}
	
	public float getDirectionX() {
		return directionX;
	}
	
	public float getDirectionY() {
		return directionY;
	}

	public ActionPacket(short action, float actionX, float actionY) {
		this(false, toBytes(action, actionX, actionY), -1);
		this.action = action;
		this.directionX = actionX;
		this.directionY = actionY;
	}
	
	public ActionPacket(boolean construct, byte[] bytes, int length) {
		super(construct, bytes, length);
	}
	
	private static byte[] toBytes(short action, float actionX, float actionY) {
		byte[] bytes = new byte[10];
		int i = 0, j;
		//Action
		bytes[i++] = (byte) ((action >> 8) & 0xFF);
		bytes[i++] = (byte) (action & 0xFF);
		//x
		j = Float.floatToIntBits(actionX);
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//y
		j = Float.floatToIntBits(actionY);
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		return bytes;
	}

	@Override
	protected void construct(byte[] bytes, int length) {
		int i = 0;
		action = (short) (((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		directionX = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		directionY = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
	}

	@Override
	public boolean shouldEncrypt() {
		// TODO Auto-generated method stub
		return false;
	}

}
