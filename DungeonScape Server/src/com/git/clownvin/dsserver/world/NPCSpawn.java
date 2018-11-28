package com.git.clownvin.dsserver.world;

import com.git.clownvin.dsserver.Server;
import com.git.clownvin.dsserver.entity.character.npc.NPC;
import com.git.clownvin.dsserver.entity.character.npc.NPCDefinition;
import com.git.clownvin.math.MathUtil;

public class NPCSpawn {
	public final NPCDefinition definition;
	public final float x, y, w, h;
	public final int spawnRate;
	public NPC npc;
	private boolean active = false;
	public int deactivateTick = 0;
	
	public NPCSpawn(int nid, float x, float y, float w, float h, int spawnRate) {
		this.definition = Server.getCharacters().getDefinition(nid);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.spawnRate = spawnRate;
	}
	
	public NPCSpawn copy() {
		return new NPCSpawn(definition.nid, x, y, w, h, spawnRate);
	}
	
	public void spawn(int instance) {
		float sx = (float) (x != w ? (Math.random() * (w - x)) + x : x);
		float sy = (float) (y != h ? (Math.random() * (h - y)) + y : y);
		Integer iX = MathUtil.ard(sx);
		Integer iY = MathUtil.ard(sy);
		while (Instances.get(instance).getChunk(iX, iY).getResistance(iX, iY) > 1.0f) {
			sx = (float) (x != w ? (Math.random() * (w - x)) + x : x);
			sy = (float) (y != h ? (Math.random() * (h - y)) + y : y);
			iX = MathUtil.ard(sx);
			iY = MathUtil.ard(sy);
		}
		npc = new NPC(definition, this, instance, sx, sy);
		//System.out.println("Spawned instance of "+definition.name+" at "+sx+", "+sy);
		active = true;
	}
	
	public boolean shouldSpawn() {
		return Server.getTickCount() - deactivateTick >= spawnRate;
	}
	
	public boolean active() {
		return active;
	}
	
	public void deactivate() {
		this.npc = null;
		active = false;
		deactivateTick = (int) Server.getTickCount();
	}
}
