package com.git.clownvin.dsapi.packet;

import com.git.clownvin.dsapi.entity.character.Character;
import com.git.clownvin.simplepacketframework.packet.Packet;

public class CharacterPacket extends Packet {
	
	private float x, y, lastX, lastY, hp, lookX, lookY, width, height;
	private int id, sprite;
	private String name;
	private byte affiliation;
	
	public CharacterPacket(Character character) {
		this(false, toBytes(character), -1);
		x = character.getX();
		y = character.getY();
		lastX = character.getLastX();
		lastY = character.getLastY();
		width = character.getWidth();
		height = character.getHeight();
		id = character.getEID();
		hp = character.getHP();
		sprite = character.getSprite();
		name = character.getName();
		affiliation = character.getAffiliation();
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getLastX() {
		return lastX;
	}
	
	public float getLastY() {
		return lastY;
	}
	
	public int getID() {
		return id;
	}
	
	public float getHP() {
		return hp;
	}
	
	public int getSprite() {
		return sprite;
	}
	
	public float getLookX() {
		return lookX;
	}
	
	public float getLookY() {
		return lookY;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public String getName() {
		return name;
	}
	
	public byte getAffiliation() {
		return affiliation;
	}

	public CharacterPacket(boolean construct, byte[] bytes, int length) {
		super(construct, bytes, length);
	}

	private static byte[] toBytes(Character character) {
		byte[] bytes = new byte[45 + character.getName().length()];
		int i = 0, j;
		//x
		j = Float.floatToIntBits(character.getX());
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//y
		j = Float.floatToIntBits(character.getY());
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//lastX
		j = Float.floatToIntBits(character.getLastX());
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//lastY
		j = Float.floatToIntBits(character.getLastY());
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//eid
		j = character.getEID();
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//hp
		j = Float.floatToIntBits(character.getHP());
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//sprite
		j = character.getSprite();
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//lookX
		j = Float.floatToIntBits(character.getLookX());
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//lookY
		j = Float.floatToIntBits(character.getLookY());
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//width
		j = Float.floatToIntBits(character.getWidth());
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//height
		j = Float.floatToIntBits(character.getHeight());
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//aff
		bytes[i++] = character.getAffiliation();
		for (char c : character.getName().toCharArray()) {
			bytes[i++] = (byte) c;
		}
		return bytes;
	}

	@Override
	protected void construct(byte[] bytes, int length) {
		int i = 0;
		//x
		x = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		//y
		y = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		//lastX
		lastX = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		//lastY
		lastY = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		//eid
		id = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
		//hp
		hp = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		//sprite
		sprite = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
		//lookX
		lookX = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		//lookY
		lookY = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		//width
		width = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		//height
		height = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		//aff
		affiliation = bytes[i++];
		StringBuilder builder = new StringBuilder(length - 45);
		for (; i < length; i++) {
			builder.append((char) bytes[i]);
		}
		name = builder.toString();
	}

	@Override
	public boolean shouldEncrypt() {
		// TODO Auto-generated method stub
		return false;
	}

}
