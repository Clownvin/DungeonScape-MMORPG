package com.git.clownvin.dsserver.entity.projectile;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

import com.git.clownvin.dsapi.entity.projectile.Projectile;
import com.git.clownvin.util.LinkedQueue;

public class Projectiles {
	
	private volatile int count = 0;
	
	public Hashtable<Integer, ServerProjectile> projectiles = new Hashtable<>();
	private LinkedQueue<ServerProjectile> projectileQueue = new LinkedQueue<>();
	
	public Projectiles() {
		
	}
	
	public void add(ServerProjectile projectile) {
		count++;
		synchronized (projectileQueue) {
			projectileQueue.add(projectile);
		}
	}
	
	public ServerProjectile get(int id) {
		return projectiles.get(id);
	}
	
	public int getCount() {
		return count;
	}
	
	private ServerProjectile remove(ServerProjectile projectile) {
		count--;
		return projectiles.remove(projectile.getEID());
	}
	
	public void update() {
		synchronized (projectiles) {
			synchronized (projectileQueue) {
				while (projectileQueue.size() > 0) {
					//TODO Stagger projectiles so they can't all be on one tick
					projectiles.put(projectileQueue.peek().getEID(), projectileQueue.remove());
				}
			}
			ArrayList<ServerProjectile> toRemove = new ArrayList<>();
			Set<Integer> keys = projectiles.keySet();
			ServerProjectile p;
			for (Integer key : keys) {
				p = projectiles.get(key);
				if (p.needsRemoval()) {
					toRemove.add(p);
					continue;
				}
				try {
					p.update();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (p.needsRemoval()) {
					toRemove.add(p);
				}
			}
			for (ServerProjectile projectile : toRemove) {
				remove(projectile);
				//System.out.println("Removing "+projectile.getEID());
				projectile.dispose();
				//System.out.println("Lets see... "+projectile.getInstance().getChunk(projectile.getIX(), projectile.getIY(), projectile.getIZ()).getEntity(projectile.getIX(), projectile.getIY(), projectile.getEID()));
			}
		}
	}
}
