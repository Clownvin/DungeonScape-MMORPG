package com.git.clownvin.dsclient.world;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.git.clownvin.dsapi.packet.ChunkRequest;
import com.git.clownvin.dsapi.world.Chunk;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.dsclient.entity.character.ClientCharacter;

public class Chunks {
	
	private final DSGame game;
	
	private final LinkedList<ClientChunk> clientChunks = new LinkedList<>();
	
	public Chunks(DSGame game) {
		this.game = game;
	}
	
	public synchronized void requestChunks() {
		ClientCharacter sc = game.getCharacters().getCharacter(game.getSelfID());
		int cx = Chunk.toChunkX(sc.getX());
		int cy = Chunk.toChunkY(sc.getY());
		ArrayList<ClientChunk> toKeep = new ArrayList<>();
		//for (int x = cx - (20 * Chunk.WIDTH); x <= cx + (20 * Chunk.WIDTH); x += Chunk.WIDTH) {
		//	for (int y = cy - (20 * Chunk.HEIGHT); y <= cy + (20 * Chunk.HEIGHT); y += Chunk.HEIGHT) {
		for (int x = cx - (2 * Chunk.WIDTH); x <= cx + (2 * Chunk.WIDTH); x += Chunk.WIDTH) {
			for (int y = cy - (1 * Chunk.HEIGHT); y <= cy + (1 * Chunk.HEIGHT); y += Chunk.HEIGHT) {
				ClientChunk chunk = getChunk(x, y);
				if (chunk == null) {
					//System.out.println("requesting chunk: "+x+", "+y);
					game.getClient().getConnection().send(new ChunkRequest(x, y));
				} else {
					toKeep.add(chunk);
				}
			}
		}
		//TODO remove bullets from non-retained chunks
		clientChunks.retainAll(toKeep);
	}
	
	public synchronized void clearAll() {
		clientChunks.clear();
	}
	
	public synchronized ClientChunk getChunk(int x, int y) {
		for (ClientChunk c : clientChunks) {
			if (c.x == x && c.y == y) {
				return c;
			}
		}
		return null;
	}
	
	public synchronized void addChunk(ClientChunk chunk) {
		if (clientChunks.size() >= 2500) {
			//System.out.println("removing chunk");
			clientChunks.removeFirst();
		}
		//System.out.println("Adding chunk "+chunk);
		clientChunks.add(chunk);
	}
	
	public synchronized void render(SpriteBatch batch, float rX, float rY) {
		for (ClientChunk c : clientChunks) {
			c.render(batch, rX, rY);
		}
	}
	
}
