package com.git.clownvin.dsapi.packet;

import com.git.clownvin.simplepacketframework.packet.Packet;

public class VelocityPacket extends Packet {
	
	public static final byte MIN = 0;
	public static final byte FIRST_GEAR = 1;
	public static final byte SECOND_GEAR = 2;
	public static final byte THIRD_GEAR = 3;
	public static final byte FOURTH_GEAR = 4;
	public static final byte FIFTH_GEAR = 5;
	
	private byte speed;
	private float velX, velY;
	
	public VelocityPacket(float velX, float velY, byte speed) {
		this(false, toBytes(velX, velY, speed), -1);
		this.velX = velX;
		this.velY = velY;
		this.speed = speed;
	}

	public VelocityPacket(boolean construct, byte[] bytes, int length) {
		super(construct, bytes, length);
	}
	
	public float getVelocityX() {
		return velX;
	}
	
	public float getVelocityY() {
		return velY;
	}
	
	public byte getSpeed() {
		return speed;
	}
	
	private static byte[] toBytes(float velX, float velY, byte speed) {
		byte[] bytes = new byte[9];
		int i = 0, j;
		//velX
		j = Float.floatToIntBits(velX);
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//velY
		j = Float.floatToIntBits(velY);
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		bytes[i++] = speed;
		return bytes;
	}

	@Override
	protected void construct(byte[] bytes, int length) {
		int i = 0;
		velX = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		velY = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		speed = bytes[i++];
	}

	@Override
	public boolean shouldEncrypt() {
		// TODO Auto-generated method stub
		return false;
	}

}
