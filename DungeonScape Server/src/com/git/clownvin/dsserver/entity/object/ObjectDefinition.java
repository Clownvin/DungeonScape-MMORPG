package com.git.clownvin.dsserver.entity.object;

import com.git.clownvin.dsserver.Server;

public class ObjectDefinition {
	public final String name;
	public final int oid, sprite;
	public final float width, height, spriteWidth, spriteHeight, resistance;
	
	public ObjectDefinition(String name, int oid, float width, float height, String sprite, float spriteWidth, float spriteHeight, float resistance) {
		this.name = name;
		this.oid = oid;
		this.width = width;
		this.height = height;
		this.sprite = Server.getSprite(sprite);
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
		this.resistance = resistance;
	}
}
