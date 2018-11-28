package com.git.clownvin.dsapi.packet;

import com.git.clownvin.dsapi.entity.object.GameObject;
import com.git.clownvin.simplepacketframework.packet.Packet;

public class ObjectPacket extends Packet {
	
	private float x, y, width, height, spriteWidth, spriteHeight;
	private int sprite, id;
	
	public int getSprite() {
		return sprite;
	}
	
	public int getID() {
		return id;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public float getSpriteWidth() {
		return spriteWidth;
	}
	
	public float getSpriteHeight() {
		return spriteHeight;
	}
	
	private static byte[] toBytes(GameObject object) {
		byte[] bytes = new byte[32];
		int i = 0, j;
		//sprite
		bytes[i++] = (byte) ((object.getSprite() >> 24) & 0xFF);
		bytes[i++] = (byte) ((object.getSprite() >> 16) & 0xFF);
		bytes[i++] = (byte) ((object.getSprite() >> 8) & 0xFF);
		bytes[i++] = (byte) (object.getSprite() & 0xFF);
		//id
		bytes[i++] = (byte) ((object.getEID() >> 24) & 0xFF);
		bytes[i++] = (byte) ((object.getEID() >> 16) & 0xFF);
		bytes[i++] = (byte) ((object.getEID() >> 8) & 0xFF);
		bytes[i++] = (byte) (object.getEID() & 0xFF);
		//x
		j = Float.floatToIntBits(object.getX());
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//y
		j = Float.floatToIntBits(object.getY());
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//width
		j = Float.floatToIntBits(object.getWidth());
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//height
		j = Float.floatToIntBits(object.getHeight());
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//sWidth
		j = Float.floatToIntBits(object.getSpriteWidth());
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//sHeight
		j = Float.floatToIntBits(object.getSpriteHeight());
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		return bytes;
	}
	
	public ObjectPacket(GameObject object) {
		this(false, toBytes(object), -1);
	}
	
	public ObjectPacket(boolean construct, byte[] bytes, int length) {
		super(construct, bytes, length);
	}

	@Override
	protected void construct(byte[] bytes, int length) {
		int i = 0;
		sprite = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
		id = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
		x = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		y = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		width = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		height = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		spriteWidth = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		spriteHeight = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
	}

	@Override
	public boolean shouldEncrypt() {
		// TODO Auto-generated method stub
		return false;
	}

}
