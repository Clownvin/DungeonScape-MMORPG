package com.git.clownvin.dsserver.world;

import java.util.ArrayList;
import java.util.Hashtable;

import com.git.clownvin.dsapi.world.Chunk;
import com.git.clownvin.dsapi.world.Tile;
import com.git.clownvin.dsserver.entity.MovableServerEntity;
import com.git.clownvin.dsserver.entity.ServerEntity;
import com.git.clownvin.math.MathUtil;
import com.git.clownvin.simplepacketframework.packet.Packet;

public class Instance {
	
	public final Map map;
	public final int instanceNumber;
	private Hashtable<Integer, Hashtable<Integer, ServerChunk>> chunkTable;
	private ArrayList<NPCSpawn> npcSpawns;
	
	public Instance(final Map map, final int instanceNumber, ArrayList<NPCSpawn> npcSpawns) {
		this.map = map;
		this.instanceNumber = instanceNumber;
		chunkTable = new Hashtable<Integer, Hashtable<Integer, ServerChunk>>();
		this.npcSpawns = npcSpawns;
	}
	
	public void spawnObjects() {
		for (ObjectSpawn spawn : map.getObjectSpawns()) {
			spawn.spawn(this);
			//System.out.println("spawnd");
		}
	}
	
	public void update() {
		for (NPCSpawn spawn : npcSpawns) {
			if (spawn.active() || !spawn.shouldSpawn())
				continue;
			spawn.spawn(instanceNumber);
		}
	}
	
	public void addEntity(ServerEntity entity) {
		//System.out.println("Trying to add entity: "+entity);
		ServerChunk chunk = getChunk(entity.getIX(), entity.getIY());
		//System.out.println("Trying to add entity: "+entity);
		synchronized (chunk) {
			chunk.addEntity(entity, entity.getIX(), entity.getIY());
		}
		sendPacketToChunks(chunk.x, chunk.y, entity.toPacket());
	}
	
	public ServerChunk getChunk(Integer x, Integer y) {
		Integer x1 = Chunk.toChunkX(x);
		Integer y1 = Chunk.toChunkY(y);
		//System.out.println("Chunk at: "+x1+", "+y1+", "+z);
		//System.out.println("Getting chunk from "+x1+"("+x+"), "+y1+"("+y+")");
		Hashtable<Integer, ServerChunk> yTable = chunkTable.get(x1);
		if (yTable == null) {
			yTable = new Hashtable<Integer, ServerChunk>();
			chunkTable.put(x1, yTable);
		}
		ServerChunk chunk = yTable.get(y1);
		if (chunk == null) {
			chunk = new ServerChunk(x1, y1, new ArrayList<Tile>());
			yTable.put(chunk.y, chunk);
		}
		return chunk;
	}
	
	public void moveEntity(MovableServerEntity entity) {
		ServerChunk lastChunk = getChunk(entity.getLastIX(), entity.getLastIY());
		ServerChunk chunk = getChunk(entity.getIX(), entity.getIY());
		if (lastChunk != chunk) {
			lastChunk.removeEntity(entity, entity.getLastIX(), entity.getLastIY());
			synchronized (chunk) {
				chunk.addEntity(entity, entity.getIX(), entity.getIY());
			}
			if (MathUtil.distanceNoRoot(chunk.x, chunk.y, lastChunk.x, lastChunk.y) <= 256.0f) {
				sendPacketToNearbyChunks(chunk.x, chunk.y, entity.toRemovePacket());
			} else {
				sendPacketToChunks(lastChunk.x, lastChunk.y, entity.toRemovePacket());
				sendPacketToNearbyChunks(lastChunk.x, lastChunk.y, entity.toRemovePacket());
			}
			sendPacketToChunks(chunk.x, chunk.y, entity.toPacket());
		} else {
			synchronized (chunk) {
				chunk.moveEntity(entity, entity.getLastIX(), entity.getLastIY(), entity.getIX(), entity.getIY());
			}
		}
		if (entity.hasMovePacket())
			sendPacketToChunks(chunk.x, chunk.y, entity.getMovePacket());
	}
	
	void putChunk(ServerChunk chunk) {
		//System.out.println("Adding chunk: "+chunk);
		Hashtable<Integer, ServerChunk> yTable = chunkTable.get(chunk.x);
		if (yTable == null) {
			yTable = new Hashtable<Integer, ServerChunk>();
			chunkTable.put(chunk.x, yTable);
		}
		yTable.put(chunk.y, chunk);
	}

	public void removeEntity(ServerEntity entity, boolean currentPosition) {
		//TODO what about players in chunks further away that have seen entity?
		//It's a very cheap packet, maybe just send to all connections?
		ServerChunk chunk;
		if (entity instanceof MovableServerEntity && !currentPosition) {
			//System.out.println("Last position: "+entity.getLastIX()+", "+entity.getLastIY()+", "+entity.getLastIZ());
			chunk = getChunk(((MovableServerEntity)entity).getLastIX(), ((MovableServerEntity)entity).getLastIY());
			synchronized (chunk) {
				chunk.removeEntity(entity, ((MovableServerEntity)entity).getLastIX(), ((MovableServerEntity)entity).getLastIY());
			}
		} else {
			//System.out.println("Current position: "+entity.getIX()+", "+entity.getIY()+", "+entity.getIZ());
			chunk = getChunk(entity.getIX(), entity.getIY());
			synchronized (chunk) {
				chunk.removeEntity(entity, entity.getIX(), entity.getIY());
			}
		}
		sendPacketToChunks(chunk.x, chunk.y, entity.toRemovePacket());
	}
	
	public void sendPacketToChunks(int cx, int cy, Packet p) {
		int x1 = Chunk.toChunkX(cx);
		int y1 = Chunk.toChunkY(cy);
		int width = x1 + (1 * Chunk.WIDTH);
		int height = y1 + (1 * Chunk.HEIGHT);
		for (int x = x1 - (1 * Chunk.WIDTH); x <= width; x += Chunk.WIDTH) {
			for (int y = y1 - (1 * Chunk.HEIGHT); y <= height; y += Chunk.HEIGHT) {
				ServerChunk chunk = getChunk(x, y);
				if (chunk == null)
					continue;
				//System.out.println("Sending packet from: "+cx+", "+cy+" to: "+x+", "+y);
				chunk.sendPacket(p);
			}
		}
	}
	
	//TODO this is also kind of gross
	public void sendPacketToNearbyChunks(int cx, int cy, Packet p) {
		int x1 = Chunk.toChunkX(cx);
		int y1 = Chunk.toChunkY(cy);
		int width = x1 + (2 * Chunk.WIDTH);
		int height = y1 + (2 * Chunk.HEIGHT);
		int x2 = x1 - (1 * Chunk.WIDTH);
		int x3 = x1 + (1 * Chunk.WIDTH);
		int y2 = y1 - (1 * Chunk.HEIGHT);
		int y3 = y1 + (1 * Chunk.HEIGHT);
		for (int x = x1 - (2 * Chunk.WIDTH); x <= width; x += Chunk.WIDTH) {
			for (int y = y1 - (2 * Chunk.HEIGHT); y <= height; y += Chunk.HEIGHT) {
				if (MathUtil.inside(x, y, x2, y2, x3, y3))
					continue;
				ServerChunk chunk = getChunk(x, y);
				if (chunk == null)
					continue;
				//System.out.println("Sending packet from: "+cx+", "+cy+" to: "+x+", "+y);
				chunk.sendPacket(p);
			}
		}
	}
	
}
