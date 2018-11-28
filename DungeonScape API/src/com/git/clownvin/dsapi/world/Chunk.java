package com.git.clownvin.dsapi.world;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import com.git.clownvin.dsapi.entity.Entity;
import com.git.clownvin.dsapi.entity.character.Character;
import com.git.clownvin.math.MathUtil;

public abstract class Chunk {
	public static final int WIDTH = 20, HEIGHT = 20;
	public final int x, y;
	//					x						y			e
	public final Hashtable<Integer, Hashtable<Integer, Hashtable<Integer, Entity>>> entities;
	public final Hashtable<Integer, Hashtable<Integer, Hashtable<Integer, Character>>> characters;
	
	protected final ArrayList<Tile> tiles;
	
	public Chunk(final int x, final int y, ArrayList<Tile> tiles) {
		this.x = x;
		this.y = y;
		this.tiles = tiles;
		entities = new Hashtable<Integer, Hashtable<Integer, Hashtable<Integer, Entity>>>();
		characters = new Hashtable<Integer, Hashtable<Integer, Hashtable<Integer, Character>>>();
		setup();
		for (int i = 0; i < WIDTH; i++) {
			Hashtable<Integer, Hashtable<Integer, Character>> cxt = new Hashtable<>();
			Hashtable<Integer, Hashtable<Integer, Entity>> ext = new Hashtable<>();
			characters.put(x + i, cxt);
			entities.put(x + i, ext);
			for (int j = 0; j < HEIGHT; j++) {
				cxt.put(y + j, new Hashtable<Integer, Character>());
				ext.put(y + j, new Hashtable<Integer, Entity>());
			}
		}
		for (Tile t : tiles) {
			addEntity(t, t.getIX(), t.getIY());
		}
		//System.out.println(this);
	}
	
	protected abstract void setup();
	
	public ArrayList<Tile> getTiles() {
		return tiles;
	}
	
	public void addEntity(Entity e, Integer x, Integer y) {
		if (e instanceof Character) {
			characters.get(x).get(y).put(e.getEID(), (Character) e);
		} else {
			entities.get(x).get(y).put(e.getEID(), e);
		}
	}
	
	public  void moveEntity(Entity e, Integer x, Integer y, Integer newX, Integer newY) {
		if (e instanceof Character) {
			characters.get(x).get(y).remove(e.getEID());
			characters.get(newX).get(newY).put(e.getEID(), (Character) e);
		} else {
			entities.get(x).get(y).remove(e.getEID());
			entities.get(newX).get(newY).put(e.getEID(), e);
		}
	}
	
	public Entity removeEntity(Entity e, Integer x, Integer y) {
		if (e instanceof Character) {
			characters.get(x).get(y).remove(e.getEID());
		} else {
			//System.out.println("Before remove: "+entities.get(x).get(y).get(e.getEID()));
			entities.get(x).get(y).remove(e.getEID());
			//System.out.println("After remove: "+entities.get(x).get(y).get(e.getEID()));
		}
		return e;
	}
	
	public Set<Integer> charactersKeySet(Integer x, Integer y) {
		return characters.get(x).get(y).keySet();
	}
	
	public Set<Integer> entitiesKeySet(Integer x, Integer y) {
		return entities.get(x).get(y).keySet();
	}
	
	public Character getCharacter(Integer x, Integer y, Integer eid) {
		return characters.get(x).get(y).get(eid);
	}
	
	public Entity getEntity(Integer x, Integer y, Integer eid) {
		return entities.get(x).get(y).get(eid);
	}
	
	public static Integer toChunkX(float x) {
		return toChunkX(MathUtil.ard(x));
	}
	
	public static Integer toChunkY(float y) {
		return toChunkY(MathUtil.ard(y));
	}
	
	public static Integer toChunkX(int x) {
		if (x % WIDTH == 0)
			return x;
		return ((x < 0 ? x - WIDTH : x) / WIDTH) * WIDTH;
	}
	
	public static Integer toChunkY(int y) {
		if (y % HEIGHT == 0)
			return y;
		return ((y < 0 ? y - HEIGHT : y) / HEIGHT) * HEIGHT;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Chunk: "+x+","+y+", tc: "+tiles.size());
//		for (int i = x; i < x + WIDTH; i++) {
//			builder.append('\n');
//			for (int j = y; j < y + HEIGHT; j++) {
//				builder.append("["+getResistance(i, j)+"]");
//			}
//		}
		return builder.toString();
	}
	
//	public static int toLocalChunkX(float x) {
//	return toLocalChunkX(MathUtil.getARDInt(x));
//}
//
//public static int toLocalChunkY(float y) {
//	return toLocalChunkY(MathUtil.getARDInt(y));
//}

//public static int toLocalChunkX(int x) {
//	return (toChunkX(x) - x) + WIDTH - 1;
//}
//
//public static int toLocalChunkY(int y) {
//	return (toChunkY(y) - y) + HEIGHT - 1;
//}
}
