package com.git.clownvin.dsclient.entity.character;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ObjectMap;
import com.git.clownvin.dsclient.DSGame;

public class Characters {
	
	private final DSGame game;
	private final ObjectMap<Integer, ClientCharacter> characters = new ObjectMap<>();
	
	public Characters(DSGame game) {
		this.game = game;
	}
	
	public final synchronized void addCharacter(ClientCharacter character) {
		characters.put(character.getEID(), character);
	}
	
	public synchronized void removeCharacter(int id) {
		characters.remove(id);
	}
	
	public final synchronized void removeCharacter(ClientCharacter character) {
		if (character == null)
			return;
		characters.remove(character.getEID());
	}
	
	public final synchronized ClientCharacter getCharacter(int id) {
		return characters.get(id);
	}
	
	public final int size() {
		return characters.size;
	}
	
	public final Object synchronizeObject() {
		return characters;
	}
	
	public final synchronized void render(SpriteBatch batch, float selfX, float selfY) {
		synchronized (characters) {
			for (Integer k : characters.keys()) {
				characters.get(k).render(batch, selfX, selfY);
			}
		}
	}
	
	public final synchronized void renderNames(SpriteBatch batch, float selfX, float selfY) {
		synchronized (characters) {
			for (Integer k : characters.keys()) {
				characters.get(k).renderName(batch, selfX, selfY);
			}
		}
	}
}
