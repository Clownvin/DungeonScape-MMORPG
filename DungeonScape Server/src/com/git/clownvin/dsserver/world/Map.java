package com.git.clownvin.dsserver.world;

import java.util.ArrayList;

import com.git.clownvin.dsapi.world.Chunk;
import com.git.clownvin.dsapi.world.Tile;
import com.git.clownvin.math.MathUtil;

public class Map {
	
	public final int x, y, entX, entY, width, height;
	public final String name;
	private final ArrayList<Tile> tiles;
	private final ArrayList<NPCSpawn> npcSpawns;
	private final ArrayList<ObjectSpawn> objectSpawns;

	public Map(String name, int x, int y, int entX, int entY, int width, int height, ArrayList<Tile> tiles, ArrayList<NPCSpawn> npcSpawns, ArrayList<ObjectSpawn> objectSpawns) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.entX = entX;
		this.entY = entY;
		this.width = width;
		this.height = height;
		this.tiles = tiles;
		this.npcSpawns = npcSpawns;
		this.objectSpawns = objectSpawns;
	}
	
	public ArrayList<Tile> getTiles() {
		return tiles;
	}
	
	private ArrayList<NPCSpawn> copySpawns() {
		ArrayList<NPCSpawn> spawns = new ArrayList<>(npcSpawns.size());
		for (NPCSpawn spawn : npcSpawns) {
			spawns.add(spawn.copy());
		}
		return spawns;
	}
	
	public ArrayList<NPCSpawn> getNPCSpawns() {
		return npcSpawns;
	}
	
	public ArrayList<ObjectSpawn> getObjectSpawns() {
		return objectSpawns;
	}
	
	public Instance createInstance(final int instanceNumber) {
		Instance instance = new Instance(this, instanceNumber, copySpawns());
		System.out.println("Creating instance "+instanceNumber+" from map "+this);
		int totalChunks = (width / Chunk.WIDTH) * (height / Chunk.HEIGHT);
		long time = System.currentTimeMillis();
		float progress = 0;
		for (int i = x; i < x + width; i += Chunk.WIDTH) {
			for (int j = y; j < y + height; j += Chunk.HEIGHT) {
				if (System.currentTimeMillis() - time >= 500) {
					System.out.println(String.format("Progress: %3.0f", ((progress / totalChunks) * 100.0f))+"% complete");
					time = System.currentTimeMillis();
				}
				ArrayList<Tile> tiles = new ArrayList<>(100); 
				for (Tile t : this.tiles) {
					if (t.x >= i && t.x < i + Chunk.WIDTH && t.y >= j && t.y < j + Chunk.HEIGHT) {
					//	System.out.println("Adding "+t.x+", "+t.y+" to "+i+", "+j);
						tiles.add(t);
					}
					//else if (t.texture == 2)
					//	System.out.println("Didn't add road because: "+t.x+" >= "+i+" && "+t.x+" < "+(i + Chunk.WIDTH)+" && "+t.y+" >= "+j+" && "+t.y+" < "+(j + Chunk.HEIGHT));
				}
				instance.putChunk(new ServerChunk(i, j, tiles));
				progress += 1.0f;
				//Chunk chunk = new Chunk();
			}
		}
		instance.spawnObjects();
		System.out.println("Finished creating instance");
		return instance;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
