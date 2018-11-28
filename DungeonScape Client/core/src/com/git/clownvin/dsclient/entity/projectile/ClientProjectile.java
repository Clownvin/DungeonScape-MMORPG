package com.git.clownvin.dsclient.entity.projectile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.git.clownvin.dsapi.config.Config;
import com.git.clownvin.dsapi.entity.projectile.Projectile;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.dsclient.entity.MovableClientEntity;
import com.git.clownvin.dsclient.texture.Textures;

public class ClientProjectile extends MovableClientEntity implements Projectile {
	
	protected final float velocityX, velocityY;
	protected final long creationTime;
	protected final int sourceID, id;
	protected float radius;
	protected float width, height;
	protected byte affiliation;
	
	public ClientProjectile(DSGame game, float x, float y, float width, float height, int sprite, float moveSpeed, float velocityX, float velocityY, long creationTime, int sourceID, int id, byte affiliation) {
		super(game, id, x, y, width, height, sprite, x, y, moveSpeed);
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.width = width;
		this.height = height;
		this.radius = width > height ? width / 2 : height / 2;
		this.creationTime = creationTime;
		this.sourceID = sourceID;
		this.id = id;
		this.affiliation = affiliation;
	}
	
	public byte getAffiliation() {
		return affiliation;
	}
	
	@Override
	public float getX() {
		return x + (((System.currentTimeMillis() - creationTime) / Config.TICK_RATE) * velocityX * (moveSpeed));
	}

	@Override
	public float getY() {
		return y + (((System.currentTimeMillis() - creationTime) / Config.TICK_RATE) * velocityY * (moveSpeed));
	}
	
	@Override
	public float getVelocityX() {
		return velocityX;
	}

	@Override
	public float getVelocityY() {
		return velocityY;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getSourceID() {
		return sourceID;
	}

	@Override
	public long getCreationTime() {
		return creationTime;
	}

	@Override
	public float getOriginalX() {
		return x;
	}

	@Override
	public float getOriginalY() {
		return y;
	}

	@Override
	public void render(SpriteBatch batch, float selfX, float selfY) {
		float x = 0, y = 0, oX = 0, oY = 0;
		x = (getRenderX() - selfX);
		y = (getRenderY() - selfY);
		//System.out.println(getRenderX() +", "+selfX+" = ");
		oX = (getWidth() / 2);
		oY = (getHeight() / 2);
		if (Textures.getTextureRegion(getSprite()) != null) {
			TextureRegion reg = Textures.getTextureRegion(getSprite());
			//batch.setColor(Color.PURPLE);
			batch.draw(reg, x - 8, y - 8, oX, oY, getWidth(), getWidth(), 1.0f, 1.0f, (float) Math.toDegrees(getAngle()));
			//batch.setColor(Color.WHITE);
			//batch.draw(reg, oX, oY, getWidth() / 2, getHeight() / 2, getWidth(), getHeight(), 1f, 1f, (float) Math.toDegrees(getAngle() + Math.toRadians(90)), 0, 0, (int) Textures.get(getSprite()).getWidth(), (int) Textures.get(getSprite()).getHeight(), false, false);
		} else {
			//System.out.println("Drawing at "+oX+", "+oY);
			batch.draw(Textures.get(getSprite()), oX, oY, getWidth() / 2, getHeight() / 2, getWidth(), getHeight(), 1f, 1f, (float) Math.toDegrees(getAngle() + Math.toRadians(90)), 0, 0, (int) Textures.get(getSprite()).getWidth(), (int) Textures.get(getSprite()).getHeight(), false, false);
		}
	}

}
