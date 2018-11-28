package com.git.clownvin.dsserver.entity.projectile.patterns;

import java.util.Hashtable;

import com.git.clownvin.dsapi.config.Config;
import com.git.clownvin.dsapi.entity.projectile.ProjectileSource;
import com.git.clownvin.dsserver.Server;
import com.git.clownvin.dsserver.entity.projectile.BasicBullet;

public class FiringPatterns {
	private final Hashtable<String, FiringPattern> patterns = new Hashtable<>();
	
	public FiringPatterns() {
		createPatterns();
	}
	
	private void createPatterns() {
		System.out.println("Creating firing patterns...");
		//GHOST_PATTERN
		patterns.put("ghost_pattern", new BeamPattern(4, Config.DEFAULT_MOVE_SPEED * 3, 5000, Server.getSprite("fireball"), 1.0f));
		//GHOST_BOSS
		patterns.put("haunting_ghost_pattern", new FiringPattern(24, Config.DEFAULT_MOVE_SPEED * 1.5f, 5000, Server.getSprite("fireball"), 15.0f) {

			/**
			 * 
			 */
			private static final long serialVersionUID = -8537151818459576591L;

			@Override
			public void update(ProjectileSource source) {
				float dX = source.getShootX();
				float dY = source.getShootY();
				float hyp = (float) Math.sqrt((dX * dX) + (dY * dY));
				dX /= hyp;
				dY /= hyp;
				new BasicBullet(source.getEID(), source.getInstanceNumber(), source.getX(), source.getY(), radius * 2.5f, radius * 2.5f, dX, dY, speed, duration, sprite, damage, source.getAffiliation()).fire();
				if (source.getFireCount() % 2 == 0) {
					double deg = Math.atan(dY/dX);
					if (dX < 0)
						deg += Math.toRadians(180);
					//System.out.println(deg+", "+Math.toDegrees(deg)+", ("+(float) Math.cos(deg)+", "+(float) Math.sin(deg)+"), ("+dX+", "+dY+")");
					//BasicBullet bullet = new BasicBullet(getID(), getInstanceNumber(), getX(), getY(), getZ(), dX, dY);
					//bullet.fire();
					float intervalDeg = (float) Math.toRadians(360.0f / 10);
					for (int i = 0; i < 10; i++) {
						float j = (i - (10 / 2.0f));
						double deg2 = deg + (j * intervalDeg);
						//System.out.println((float) Math.cos(deg2)+", "+(float) Math.sin(deg2));
						new BasicBullet(source.getEID(), source.getInstanceNumber(), source.getX(), source.getY(), radius * 2.5f, radius * 2.5f, (float) Math.cos(deg2), (float) Math.sin(deg2), speed, duration, sprite, damage, source.getAffiliation()).fire();
						//bullet.fire();
					}
				}
			}
			
		});
		patterns.put("crab_pattern", new ConePattern(3, 90, 3, Config.DEFAULT_MOVE_SPEED, 9000, 5, 0.5f));
		patterns.put("legendary_crab_pattern", new ConePattern(9, 360, 6, Config.DEFAULT_MOVE_SPEED * 1.5f, 7000, 5, 0.5f));
		System.out.println("Finished creating firing patterns");
	}
	
	public FiringPattern getPattern(String name) {
		return patterns.get(name);
	}
	
}
