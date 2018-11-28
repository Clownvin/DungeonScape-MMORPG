package com.git.clownvin.dsclient.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.git.clownvin.dsapi.entity.Entity;
import com.git.clownvin.dsclient.DSGame;

public abstract class ClientEntity implements Entity {
	
	protected final DSGame game;
	
	protected float x, y, oX, oY, width, height, rX, rY;
	
	private final int eid;
	
	protected int sprite;
	protected boolean needsRemoval = false;
	
	@Override
	public boolean needsRemoval() {
		return needsRemoval;
	}
	
	public ClientEntity(DSGame game, int eid, float x, float y, float width, float height, int sprite) {
		//eid = (int) (System.nanoTime() % Integer.MAX_VALUE);
		this.eid = eid;
		this.game = game;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.oX = getActualX() + (getWidth() / 2);
		this.oY = getActualY() + (getHeight() / 2);
		this.sprite = sprite;
	}
	
	public abstract void render(SpriteBatch batch, float selfX, float selfY);
	
	@Override
	public final int getEID() {
		return eid;
	}
	
	public final float getOriginX() {
		return oX;
	}
	
	public final float getOriginY() {
		return oY;
	}
	
	public float getRenderX() {
		return getActualX();//- (Tile.WIDTH / 2)
	}
	
	public float getRenderY() {
		return getActualY();//- (Tile.HEIGHT / 2)
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public final float getWidth() {
		return width;
	}

	@Override
	public final float getHeight() {
		return height;
	}

	@Override
	public final float getResistance() {
		return 0;
	}

	@Override
	public int getSprite() {
		return sprite;
	}
	
	public void setSprite(int sprite) {
		this.sprite = sprite;
	}
	

}
