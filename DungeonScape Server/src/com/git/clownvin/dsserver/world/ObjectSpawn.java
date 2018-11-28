package com.git.clownvin.dsserver.world;

import com.git.clownvin.dsserver.Server;
import com.git.clownvin.dsserver.entity.object.ObjectDefinition;
import com.git.clownvin.dsserver.entity.object.ServerGameObject;

public class ObjectSpawn {
	public final ObjectDefinition definition;
	public final float x, y;
	
	public ObjectSpawn(int oid, float x, float y) {
		this.definition = Server.getObjects().getDefinition(oid);
		this.x = x;
		this.y = y;
	}
	
	public void spawn(Instance instance) {
		ServerGameObject obj = new ServerGameObject(definition, x, y, instance);
		instance.addEntity(obj);
		Server.getObjects().add(obj);
	}
}
