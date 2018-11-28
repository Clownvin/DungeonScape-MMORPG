package com.git.clownvin.dsclient.entity.projectile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.git.clownvin.dsapi.world.Chunk;
import com.git.clownvin.dsapi.world.Tile;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.dsclient.entity.character.ClientCharacter;
import com.git.clownvin.dsclient.texture.Textures;
import com.git.clownvin.dsclient.world.ClientChunk;
import com.git.clownvin.math.MathUtil;

public class Projectiles {
	
	private final DSGame game;
	private final ObjectMap<Integer, ClientProjectile> projectiles = new ObjectMap<>();
	
	public Projectiles(DSGame game) {
		this.game = game;
	}
	
	public synchronized void addProjectile(ClientProjectile projectile) {
		projectiles.put(projectile.getEID(), projectile);
	}
	
	public synchronized void removeProjectile(int id) {
		projectiles.remove(id);
	}
	
	public synchronized void removeProjectile(ClientProjectile projectile) {
		if (projectile == null)
			return;
		//System.out.println("Removing projectile");
		projectiles.remove(projectile.getEID());
	}
	
	public synchronized ClientProjectile getProjectile(int id) {
		return projectiles.get(id);
	}
	
	public synchronized void removeProjectilesFromChunk(ClientChunk chunk) {
		synchronized (projectiles) {
			Array<Integer> toRemove = new Array<>();
			for (Integer k : projectiles.keys()) {
				ClientProjectile projectile = projectiles.get(k);
				if (MathUtil.inside(projectile.getX(), projectile.getY(), chunk.x, chunk.y, chunk.x + Chunk.WIDTH, chunk.y + Chunk.WIDTH)) {
					toRemove.add(k);
				}
			}
			for (Integer k : toRemove) {
				removeProjectile(k);
			}
		}
	}
	
	public final synchronized void render(SpriteBatch batch, float selfX, float selfY) {
		ClientCharacter self = game.getCharacters().getCharacter(game.getSelfID());
		if (self == null)
			return;
		ClientProjectile p;
		Array<Integer> toRemove = new Array<>();
		synchronized (projectiles) {
			for (Integer k : projectiles.keys()) {
				p = projectiles.get(k);
				if (System.currentTimeMillis() - p.getCreationTime() > 60000) {
					toRemove.add(k);
					continue;
				}
				p.render(batch, selfX, selfY);
			}
		}
		for (Integer k : toRemove) {
			removeProjectile(k);
		}
	}
	
}
