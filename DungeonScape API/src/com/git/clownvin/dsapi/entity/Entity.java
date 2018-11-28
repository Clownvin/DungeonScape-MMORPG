package com.git.clownvin.dsapi.entity;

import com.git.clownvin.dsapi.world.Tile;

public interface Entity {
	
	public float getX();
	
	public float getY();
	
	public Integer getIX();
	
	public Integer getIY();
	
	public default float getActualX() {
		return getX() * Tile.WIDTH;
	}
	
	public default float getActualY() {
		return getY() * Tile.HEIGHT;
	}
	
	public float getWidth();
	
	public float getHeight();
	
	public float getResistance();
	
	public int getSprite();
	
	public void update();
	
	public boolean needsRemoval();

	public int getEID();
	
}
