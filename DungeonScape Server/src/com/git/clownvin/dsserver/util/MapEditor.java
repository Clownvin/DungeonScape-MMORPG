package com.git.clownvin.dsserver.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import com.git.clownvin.dsapi.packet.ChunkPacket;
import com.git.clownvin.dsapi.world.Tile;
import com.git.clownvin.dsserver.connection.UserConnection;
import com.git.clownvin.dsserver.world.Map;
import com.git.clownvin.dsserver.world.NPCSpawn;
import com.git.clownvin.dsserver.world.ObjectSpawn;
import com.git.clownvin.dsserver.world.ServerChunk;

public class MapEditor {
	private Map map;
	private UserConnection user;
	
	public MapEditor(Map map, UserConnection user) {
		this.map = map;
		this.user = user;
	}
	
	public void addObject(int oid, int x, int y) {
		map.getObjectSpawns().add(new ObjectSpawn(oid, x, y));
	}
	
	public void addNPC(int nid, int x, int y, int x2, int y2, int rate) {
		map.getNPCSpawns().add(new NPCSpawn(nid, x, y, x2, y2, rate));
	}
	
	public void replaceTile(int x, int y, Tile newTile) {
		ArrayList<Tile> tiles = map.getTiles();
		boolean set = false;
		for (int i = 0; i < tiles.size(); i++) {
			Tile t = tiles.get(i);
			if (t.x != x || t.y != y)
				continue;
			if (set) {
				tiles.remove(i);
				continue;
			}
			tiles.set(i, newTile);
			set = true;
		}
		set = false;
		ServerChunk c = user.getCharacter().getInstance().getChunk(x, y);
		tiles = c.getTiles();
		for (int i = 0; i < tiles.size(); i++) {
			Tile t = tiles.get(i);
			if (t.x != x || t.y != y)
				continue;
			if (set) {
				tiles.remove(i);
				continue;
			}
			tiles.set(i, newTile);
			set = true;
		}
		//user.send(new ChunkPacket(c));
	}
	
	public void saveMap() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("./data/maps/"+map.name+"/"+map.name+".map"))) {
			writer.write("#x "+map.x+"\n");
			writer.write("#y "+map.y+"\n");
			writer.write("#enty "+map.entY+"\n");
			writer.write("#entx "+map.entX+"\n");
			writer.write("#width "+map.width+"\n");
			writer.write("#height "+map.height+"\n");
			writer.write("#def water 3\n");
			writer.write("#def wall 4\n");
			writer.write("#def wall2 13\n");
			writer.write("#def sand 24\n");
			writer.write("#def dirt 2\n");
			writer.write("#def grass 1\n");
			writer.write("#def gravel 25\n");
			writer.write("#def tgrass 21\n");
			writer.write("#def flowers1 22\n");
			for (Tile t : map.getTiles()) {
				writer.write(t.texture+" "+(int)t.x+" "+(int)t.y+" "+t.resistance+"\n");
			}
			for (ObjectSpawn object : map.getObjectSpawns()) {
				writer.write("#object "+object.definition.oid+" "+(int)object.x+" "+(int)object.y+"\n");
			}
			for (NPCSpawn spawn : map.getNPCSpawns()) {
				writer.write("#npc "+spawn.definition.nid+" "+(int)spawn.x+" "+(int)spawn.y+" "+(int)spawn.w+" "+(int)spawn.h+" "+(int)spawn.spawnRate+"\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
