package com.git.clownvin.dsserver.entity.character.npc;

import com.git.clownvin.dsserver.Server;

public class NPCDefinition {
	public final String name, pattern;
	public final Integer nid;
	public final int fireRate, sprite;
	public final float width, height, radius, damageReduction, aggressionRadius;
	public final byte affiliation;
	
	public NPCDefinition(int nid, String name, String sprite, float width, float height, float radius, float damageReduction, int fireRate, byte affiliation, float aggressionRadius, String pattern) {
		this.name = name;
		this.nid = nid;
		this.sprite = Server.getSprite(sprite);
		this.width = width;
		this.height = height;
		this.radius = radius;
		this.damageReduction = damageReduction;
		this.fireRate = fireRate;
		this.affiliation = affiliation;
		this.aggressionRadius = aggressionRadius;
		this.pattern = pattern;
	}
}
