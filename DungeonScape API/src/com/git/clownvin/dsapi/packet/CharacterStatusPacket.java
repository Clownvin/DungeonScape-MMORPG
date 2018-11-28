package com.git.clownvin.dsapi.packet;

import com.git.clownvin.dsapi.entity.character.Character;
import com.git.clownvin.simplepacketframework.packet.Packet;

public class CharacterStatusPacket extends Packet {
	
	private int id;
	private float hp, lookX, lookY;
	
	public CharacterStatusPacket(boolean construct, byte[] bytes, int length) {
		super(construct, bytes, length);
	}
	
	private static byte[] toBytes(Character character) {
		byte[] bytes = new byte[16];
		int i = 0;
		int j = character.getEID();
		//eid
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
		return bytes;
	}
	
	public CharacterStatusPacket(Character character) {
		super(false, toBytes(character), -1);
		this.id = character.getEID();
		this.hp = character.getHP();
		this.lookX = character.getLookX();
		this.lookY = character.getLookY();
	}
	
	public int getID() {
		return id;
	}
	
	public float getHP() {
		return hp;
	}
	
	public float getLookX() {
		return lookX;
	}
	
	public float getLookY() {
		return lookY;
	}

	@Override
	protected void construct(byte[] bytes, int length) {
		int i = 0;
		id = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
		hp = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		lookX = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		lookY = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
	}

	@Override
	public boolean shouldEncrypt() {
		// TODO Auto-generated method stub
		return false;
	}
}
