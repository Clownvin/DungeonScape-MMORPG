package com.git.clownvin.dsserver.world;

import java.util.ArrayList;
import java.util.LinkedList;

import com.git.clownvin.dsapi.entity.Entity;
import com.git.clownvin.dsapi.world.Chunk;
import com.git.clownvin.dsapi.world.Tile;
import com.git.clownvin.dsserver.connection.UserConnection;
import com.git.clownvin.dsserver.entity.ServerEntity;
import com.git.clownvin.dsserver.entity.character.PlayerCharacter;
import com.git.clownvin.dsserver.entity.character.ServerCharacter;
import com.git.clownvin.dsserver.entity.object.ServerGameObject;
import com.git.clownvin.math.MathUtil;
import com.git.clownvin.simplepacketframework.packet.Packet;

public class ServerChunk extends Chunk {
	
	private LinkedList<UserConnection> connections;
	private LinkedList<ServerEntity> entities;
	private float[][] resistances;

	public ServerChunk(int x, int y, ArrayList<Tile> tiles) {
		super(x, y, tiles);
	}
	
	@Override
	public void addEntity(Entity e, Integer x, Integer y) {
		if (e instanceof PlayerCharacter) {
			synchronized (connections) {
				connections.add(((PlayerCharacter) e).getConnection());
			}
		}
		super.addEntity(e, x, y);
		synchronized (resistances) {
			resistances[x - this.x][y - this.y] += e.getResistance();
		}
		if (e instanceof ServerEntity)
			entities.add((ServerEntity)e);
	}
	
	public LinkedList<UserConnection> getConnections() {
		return connections;
	}
	
	public LinkedList<ServerEntity> getEntities() {
		return entities;
	}
	
	public float getResistance(Integer x, Integer y) {
		synchronized (resistances) {
			return resistances[x - this.x][y - this.y];
		}
	}
	
	@Override
	public void moveEntity(Entity e, Integer x1, Integer y1, Integer x2, Integer y2) {
		if (x1 == x2 && y1 == y2) {
			//System.out.println("Wtf is goin on here nigga lol");
			return;
		}
		super.moveEntity(e, x1, y1, x2, y2);
		synchronized (resistances) {
			resistances[x1 - this.x][y1 - this.y] -= e.getResistance();
			resistances[x2 - this.x][y2 - this.y] += e.getResistance();
		}
	}
	
	@Override
	public Entity removeEntity(Entity e, Integer x, Integer y) {
		//System.out.println("Chunk removing entity "+e+" at "+x+", "+y);
		if (e instanceof PlayerCharacter) {
			synchronized (connections) {
				connections.remove(((PlayerCharacter) e).getConnection());
			}
		}
		if (e instanceof ServerEntity)
			entities.remove((ServerEntity)e);
		super.removeEntity(e, x, y);
		synchronized (resistances) {
			resistances[x - this.x][y - this.y] -= e.getResistance();
		}
		return e;
	}
	
	public void sendPacket(Packet p) {
		//System.out.println("Sending packet");
		synchronized (connections) {
			for (var connection : connections) {
				connection.send(p);
			}
		}
	}
	
	protected void setup() {
		entities = new LinkedList<>();
		connections = new LinkedList<UserConnection>();
		resistances = new float[WIDTH][HEIGHT];
	}

}
