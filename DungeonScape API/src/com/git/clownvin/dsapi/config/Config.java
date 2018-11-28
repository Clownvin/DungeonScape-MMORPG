package com.git.clownvin.dsapi.config;

public final class Config {
	
	public static final long TICK_RATE = 16; //1 tick every X ms
	
	public static final int INVENTORY_SIZE = 100;
	
	public static final float DEFAULT_MOVE_SPEED = .025f; // of whole tile
	
	public static long getTicksForMS(long ms) {
		return ms / TICK_RATE;
	}
}
