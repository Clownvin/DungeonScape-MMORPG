package com.git.clownvin.dsapi.packet;

import java.util.ArrayList;

import com.git.clownvin.dsapi.world.Chunk;
import com.git.clownvin.dsapi.world.Tile;
import com.git.clownvin.simplepacketframework.packet.Packet;

public class ChunkPacket extends Packet {
	
	//private Chunk chunk;
	private int x, y;
	private ArrayList<Tile> tiles;
	
	public ChunkPacket(Chunk chunk) {
		super(false, toBytes(chunk), -1);
		x = chunk.x;
		y = chunk.y;
		tiles = chunk.getTiles();
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public ArrayList<Tile> getTiles() {
		return tiles;
	}
	
	private static byte[] toBytes(Chunk chunk) {
		int tileCount = chunk.getTiles().size();
		byte[] bytes = new byte[12 + (12 * tileCount)];
		int i = 0;
		//x
		bytes[i++] = (byte) ((chunk.x >> 24) & 0xFF);
		bytes[i++] = (byte) ((chunk.x >> 16) & 0xFF);
		bytes[i++] = (byte) ((chunk.x >> 8) & 0xFF);
		bytes[i++] = (byte) (chunk.x & 0xFF);
		//y
		bytes[i++] = (byte) ((chunk.y >> 24) & 0xFF);
		bytes[i++] = (byte) ((chunk.y >> 16) & 0xFF);
		bytes[i++] = (byte) ((chunk.y >> 8) & 0xFF);
		bytes[i++] = (byte) (chunk.y & 0xFF);
		//tile count
		bytes[i++] = (byte) ((tileCount >> 24) & 0xFF);
		bytes[i++] = (byte) ((tileCount >> 16) & 0xFF);
		bytes[i++] = (byte) ((tileCount >> 8) & 0xFF);
		bytes[i++] = (byte) (tileCount & 0xFF);
		//All them tiles...
		for (Tile t : chunk.getTiles()) {
			//x
			bytes[i++] = (byte) ((t.getIX() >> 24) & 0xFF);
			bytes[i++] = (byte) ((t.getIX() >> 16) & 0xFF);
			bytes[i++] = (byte) ((t.getIX() >> 8) & 0xFF);
			bytes[i++] = (byte) (t.getIX() & 0xFF);
			//y
			bytes[i++] = (byte) ((t.getIY() >> 24) & 0xFF);
			bytes[i++] = (byte) ((t.getIY() >> 16) & 0xFF);
			bytes[i++] = (byte) ((t.getIY() >> 8) & 0xFF);
			bytes[i++] = (byte) (t.getIY() & 0xFF);
			//texture
			bytes[i++] = (byte) ((t.texture >> 24) & 0xFF);
			bytes[i++] = (byte) ((t.texture >> 16) & 0xFF);
			bytes[i++] = (byte) ((t.texture >> 8) & 0xFF);
			bytes[i++] = (byte) (t.texture & 0xFF);
		}
		return bytes;
	}

	public ChunkPacket(boolean construct, byte[] bytes, int length) {
		super(construct, bytes, length);
	}

	@Override
	protected void construct(byte[] bytes, int length) {
		int i = 0;
		x = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
		y = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
		int tileCount = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
		tiles = new ArrayList<>(tileCount);
		for (int j = 0; j < tileCount; j++) {
			int x1 = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
			int y1 = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
			int texture = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
			tiles.add(new Tile(x1, y1, 0.0f, texture));
		}
	}

	@Override
	public boolean shouldEncrypt() {
		return false;
	}

}
