package com.git.clownvin.dsapi.world;

import com.git.clownvin.dsapi.entity.Entity;
import com.git.clownvin.math.MathUtil;

public class Tile implements Entity {
	
	public static final float WIDTH = 32, HEIGHT = 32, HALF_WIDTH = 16, HALF_HEIGHT = 16;
	public final int texture;
	public final float x, y;
	public final float x2, y2;
	public final float resistance;
	private final int eid;
	
	public Tile(float x, float y, float resistance, int texture) {
		this.x = x;
		this.y = y;
		x2 = getActualX() + WIDTH;
		y2 = getActualY() + HEIGHT;
		this.resistance = resistance;
		this.texture = texture;
		eid = (int) (System.nanoTime() % Integer.MAX_VALUE);
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
	public float getWidth() {
		return WIDTH;
	}

	@Override
	public float getHeight() {
		return HEIGHT;
	}

	@Override
	public float getResistance() {
		return resistance;
	}

	@Override
	public int getSprite() {
		return texture;
	}

	@Override
	public void update() {
	}

	@Override
	public boolean needsRemoval() {
		return false;
	}

	@Override
	public int getEID() {
		return eid;
	}

	@Override
	public Integer getIX() {
		return MathUtil.ard(x);
	}

	@Override
	public Integer getIY() {
		return MathUtil.ard(y);
	}
}
