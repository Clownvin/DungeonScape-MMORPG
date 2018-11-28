package com.git.clownvin.dsserver.world;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import com.git.clownvin.dsapi.world.Chunk;
import com.git.clownvin.dsapi.world.Tile;

public final class Maps {
	
	private static Hashtable<String, Map> mapTable;
	
	public static Map getMap(String map) {
		return mapTable.get(map);
	}
	
	private static Map loadMap(File mapFile) {
		BufferedReader reader = null;
		String line = "";
		int lineCount = 0;
		Hashtable<String, Integer> textures = new Hashtable<>();
		int _x = 0, _y = 0;
		int entX = 0, entY = 0;
		boolean setX = false, setY = false;
		int width = 100, height = 100;
		boolean[][] fill = null;
		ArrayList<Tile> tileSet = new ArrayList<>(1000);
		ArrayList<NPCSpawn> npcSpawns = new ArrayList<>();
		ArrayList<ObjectSpawn> objectSpawns = new ArrayList<>();
		try {
			reader = new BufferedReader(new FileReader(mapFile));
			while ((line = reader.readLine()) != null) {
				lineCount++;
				String[] tokens = line.split(" ");
				try {
				if (tokens[0].equalsIgnoreCase("#x")) {
					_x = Chunk.toChunkX(Integer.parseInt(tokens[1]));
					setX = true;
				} else if (tokens[0].equalsIgnoreCase("#y")) {
					_y = Chunk.toChunkY(Integer.parseInt(tokens[1]));
					setY = true;
				} else if (tokens[0].equalsIgnoreCase("#entx")) {
					entX = Integer.parseInt(tokens[1]);
				} else if (tokens[0].equalsIgnoreCase("#enty")) {
					entY = Integer.parseInt(tokens[1]);
				} else if (tokens[0].equalsIgnoreCase("#width")) {
					width = Integer.parseInt(tokens[1]);
				} else if (tokens[0].equalsIgnoreCase("#npc")) {
					int nid = Integer.parseInt(tokens[1]);
					float x = Float.parseFloat(tokens[2]);
					float y = Float.parseFloat(tokens[3]);
					float w = Float.parseFloat(tokens[4]);
					float h = Float.parseFloat(tokens[5]);
					int rate = Integer.parseInt(tokens[6]);
					npcSpawns.add(new NPCSpawn(nid, x, y, w, h, rate));
				} else if (tokens[0].equalsIgnoreCase("#object")) {
					int oid = Integer.parseInt(tokens[1]);
					float x = Float.parseFloat(tokens[2]);
					float y = Float.parseFloat(tokens[3]);
					objectSpawns.add(new ObjectSpawn(oid, x, y));
				} else if (tokens[0].equalsIgnoreCase("#height")) {
					height = Integer.parseInt(tokens[1]);
					fill = new boolean[width][height];
					for (int i = 0; i < width; i++) {
						for (int j = 0; j < height; j++) {
							fill[i][j] = true;
						}
					}
				} else if (tokens[0].equalsIgnoreCase("#def") && tokens.length == 3) {
					textures.put(tokens[1], Integer.parseInt(tokens[2]));
				} else if (textures.containsKey(tokens[0]) && tokens.length <= 4) {
					int x = (int) Float.parseFloat(tokens[1]);
					int y = (int) Float.parseFloat(tokens[2]);
					float r = 0.0f;
					if (tokens.length >= 4)
						r = Float.parseFloat(tokens[3]);
					tileSet.add(new Tile(x, y, r, textures.get(tokens[0])));
					fill[x - _x][y - _y] = false;
				} else if (textures.contains(Integer.parseInt(tokens[0])) && tokens.length <= 4) {
					int x = (int) Float.parseFloat(tokens[1]);
					int y = (int) Float.parseFloat(tokens[2]);
					float r = 0.0f;
					if (tokens.length >= 4)
						r = Float.parseFloat(tokens[3]);
					tileSet.add(new Tile(x, y, r, Integer.parseInt(tokens[0])));
					fill[x - _x][y - _y] = false;
				} else if (textures.containsKey(tokens[0]) && tokens.length >= 5) {
					int x1 = Integer.parseInt(tokens[1]);
					int y1 = Integer.parseInt(tokens[2]);
					int x2 = Integer.parseInt(tokens[3]);
					int y2 = Integer.parseInt(tokens[4]);
					if (x1 > x2) {
						int i = x1;
						x1 = x2;
						x2 = i;
					}
					if (y1 > y2) {
						int i = y1;
						y1 = y2;
						y2 = i;
					}
					float r = 0.0f;
					if (tokens.length > 5)
						r = Float.parseFloat(tokens[5]);
					if (tokens[0].equals("wall") && r == 0.0f) {
						System.out.println("Err on line: "+line);
					}
					for (int x = x1; x <= x2; x += 1) {
						for (int y = y1; y <= y2; y += 1) {
							if (x < _x || x >= _x + width || y < _y || y >= _y + height)
								continue;
							//System.out.println("Adding "+tokens[0]+" at "+x+", "+y);
							tileSet.add(new Tile(x, y, r, textures.get(tokens[0])));
							fill[x - _x][y - _y] = false;
						}
					}
				} else if (tokens[0].equalsIgnoreCase("#fill") && textures.containsKey(tokens[1])) {
					float resistance = 0.0f;
					if (tokens.length > 2) {
						resistance = Float.parseFloat(tokens[2]);
					}
					for (int x = _x; x < _x + width; x++) {
						for (int y = _y; y < _y + height; y++) {
							if (!fill[x - _x][y - _y])
								continue;
							tileSet.add(new Tile(x, y, resistance, textures.get(tokens[1])));
							fill[x - _x][y - _y] = false;
						}
					}
				}
				} catch (NumberFormatException e) {
					System.out.println("Error on line: "+lineCount);
					System.out.println(line);
					System.exit(2);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		if (!setX || !setY) {
			throw new RuntimeException("Cannot create map without setting x and y!");
		}
		System.out.println("Loaded map from: "+mapFile.getName());
		return new Map(mapFile.getName().replace(".map", ""), _x, _y, entX, entY, width, height, tileSet, npcSpawns, objectSpawns);
//		Chunk world = new Chunk(worldName, width, height);
//		world.setTileSet(tileSet);
//		currentWorld = world;
//		return true;
	}
	
	public static void loadMap(String name) {
		mapTable.put(name, loadMap(new File("./data/maps/"+name+".map")));
	}
	
	public static void loadMaps() {
		System.out.println("Loading maps...");
		File maps = new File("./data/maps/");
		File[] files = maps.listFiles();
		mapTable = new Hashtable<>(files.length);
		for (File file : files) {
			if (!file.getName().endsWith(".map"))
				continue;
			mapTable.put(file.getName().replaceAll(".map", ""), loadMap(file));
		}
		System.out.println("Finished loading maps");
	}
	
	public static int toChunkX(int x) {
		return ((x < 0 ? x - 16 : x) / Chunk.WIDTH) * Chunk.WIDTH;
	}
	
	public static int toChunkY(int y) {
		return ((y < 0 ? y - 16 : y) / Chunk.HEIGHT) * Chunk.HEIGHT;
	}
}
