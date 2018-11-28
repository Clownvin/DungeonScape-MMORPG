package com.git.clownvin.dsclient.entity.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ObjectMap;
import com.git.clownvin.dsclient.DSGame;

public class Objects {
	private final DSGame game;
	private final ObjectMap<Integer, ClientGameObject> objects = new ObjectMap<>();
	
	public Objects(DSGame game) {
		this.game = game;
	}
	
	public final synchronized void addObject(ClientGameObject object) {
		objects.put(object.getEID(), object);
	}
	
	public synchronized void removeObject(int id) {
		objects.remove(id);
	}
	
	public final synchronized void removeObject(ClientGameObject object) {
		if (object == null)
			return;
		objects.remove(object.getEID());
	}
	
	public final synchronized ClientGameObject getObject(int id) {
		return objects.get(id);
	}
	
	public final int size() {
		return objects.size;
	}
	
	public final synchronized void render(SpriteBatch batch, float selfX, float selfY) {
		synchronized (objects) {
			for (Integer k : objects.keys()) {
				objects.get(k).render(batch, selfX, selfY);
			}
		}
	}
}
