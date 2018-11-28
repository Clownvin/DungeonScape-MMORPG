package com.git.clownvin.dsapi.packet;

import com.git.clownvin.dsapi.entity.projectile.Projectile;
import com.git.clownvin.simplepacketframework.packet.Packet;

public class ProjectilePacket extends Packet {

	private float velocityX, velocityY, x, y, moveSpeed;
	private long creationTime;
	private int sourceID, id, sprite;
	private float width, height;
	private byte affiliation;
	
	public float getVelocityX() {
		return velocityX;
	}
	
	public float getVelocityY() {
		return velocityY;
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
	
	public long getCreationTime() {
		return creationTime;
	}
	
	public int getSourceID() {
		return sourceID;
	}
	
	public int getID() {
		return id;
	}
	
	public int getSprite() {
		return sprite;
	}
	
	public float getMoveSpeed() {
		return moveSpeed;
	}
	
	public byte getAffiliation() {
		return affiliation;
	}
	
	public ProjectilePacket(Projectile projectile) {
		this(false, toBytes(projectile), -1);
		this.velocityX = projectile.getVelocityX();
		this.velocityY = projectile.getVelocityY();
		this.x = projectile.getOriginalX();
		this.y = projectile.getOriginalY();
		this.width = projectile.getWidth();
		this.height = projectile.getHeight();
		this.creationTime = projectile.getCreationTime();
		this.sourceID = projectile.getSourceID();
		this.id = projectile.getEID();
		this.sprite = projectile.getSprite();
		this.moveSpeed = projectile.getMoveSpeed();
		this.affiliation = projectile.getAffiliation();
	}
	
	protected static byte[] toBytes(Projectile p) {
		byte[] bytes = new byte[49];
		int i = 0, j;
		//velX
		j = Float.floatToIntBits(p.getVelocityX());
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//velY
		j = Float.floatToIntBits(p.getVelocityY());
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//originalX
		j = Float.floatToIntBits(p.getOriginalX());
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//originalY
		j = Float.floatToIntBits(p.getOriginalY());
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//width
		j = Float.floatToIntBits(p.getWidth());
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//height
		j = Float.floatToIntBits(p.getHeight());
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//creationTime
		long l = p.getCreationTime();
		bytes[i++] = (byte) ((l >> 56) & 0xFF);
		bytes[i++] = (byte) ((l >> 48) & 0xFF);
		bytes[i++] = (byte) ((l >> 40) & 0xFF);
		bytes[i++] = (byte) ((l >> 32) & 0xFF);
		bytes[i++] = (byte) ((l >> 24) & 0xFF);
		bytes[i++] = (byte) ((l >> 16) & 0xFF);
		bytes[i++] = (byte) ((l >> 8) & 0xFF);
		bytes[i++] = (byte) (l & 0xFF);
		//sourceid
		j = p.getSourceID();
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//eid
		j = p.getEID();
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//sprite
		j = p.getSprite();
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//Move speed
		j = Float.floatToIntBits(p.getMoveSpeed());
		bytes[i++] = (byte) ((j >> 24) & 0xFF);
		bytes[i++] = (byte) ((j >> 16) & 0xFF);
		bytes[i++] = (byte) ((j >> 8) & 0xFF);
		bytes[i++] = (byte) (j & 0xFF);
		//aff
		bytes[i++] = p.getAffiliation();
		return bytes;
	}
	
	public ProjectilePacket(boolean construct, byte[] bytes, int length) {
		super(construct, bytes, length);
	}

	@Override
	protected void construct(byte[] bytes, int length) {
		int i = 0;
		//velX
		velocityX = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		//velY
		velocityY = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		//originalX
		x = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		//originalY
		y = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		//width
		width = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		//height
		height = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		//creationTime
		creationTime = ((bytes[i++] & 0xFFL) << 56) | ((bytes[i++] & 0xFFL) << 48) | ((bytes[i++] & 0xFFL) << 40) | ((bytes[i++] & 0xFFL) << 32) | ((bytes[i++] & 0xFFL) << 24) | ((bytes[i++] & 0xFFL) << 16) | ((bytes[i++] & 0xFFL) << 8) | (bytes[i++] & 0xFFL);
		//sourceid
		sourceID = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
		//eid
		id = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
		//sprite
		sprite = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
		//Move speed
		moveSpeed = Float.intBitsToFloat(((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF));
		//aff
		affiliation = bytes[i++];
	}

	@Override
	public boolean shouldEncrypt() {
		// TODO Auto-generated method stub
		return false;
	}

}
