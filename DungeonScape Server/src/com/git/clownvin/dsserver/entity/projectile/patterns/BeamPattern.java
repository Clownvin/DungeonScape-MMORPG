package com.git.clownvin.dsserver.entity.projectile.patterns;

import com.git.clownvin.dsapi.entity.projectile.ProjectileSource;
import com.git.clownvin.dsserver.entity.projectile.BasicBullet;

public class BeamPattern extends FiringPattern {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8132917788278159964L;

	public BeamPattern(float radius, float speed, int duration, int sprite, float damage) {
		super(radius, speed, duration, sprite, damage);
	}
	
	@Override
	public void update(ProjectileSource source) {
		float dX = source.getShootX();
		float dY = source.getShootY();
		float hyp = (float) Math.sqrt((dX * dX) + (dY * dY));
		dX /= hyp;
		dY /= hyp;
		new BasicBullet(source.getEID(), source.getInstanceNumber(), source.getX(), source.getY(), radius * 2, radius * 2, dX, dY, speed, duration, sprite, damage, source.getAffiliation()).fire();
	}

}
