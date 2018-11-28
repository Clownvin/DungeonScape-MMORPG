package com.git.clownvin.dsserver.entity.projectile.patterns;

import com.git.clownvin.dsapi.entity.projectile.ProjectileSource;
import com.git.clownvin.dsserver.entity.projectile.BasicBullet;

public class ConePattern extends FiringPattern {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8422218486904419650L;
	protected final int projectileCount;
	protected final float degrees;
	
	public ConePattern(int projectileCount, float degrees, float radius, float speed, int duration, int sprite, float damage) {
		super(radius, speed, duration, sprite, damage);
		this.projectileCount = projectileCount;
		this.degrees = degrees;
	}

	@Override
	public void update(ProjectileSource source) {
		float dX = source.getShootX();
		float dY = source.getShootY();
		float hyp = (float) Math.sqrt((dX * dX) + (dY * dY));
		dX /= hyp;
		dY /= hyp;
		double deg = Math.atan(dY/dX);
		if (dX < 0)
			deg += Math.toRadians(180);
		//System.out.println(deg+", "+Math.toDegrees(deg)+", ("+(float) Math.cos(deg)+", "+(float) Math.sin(deg)+"), ("+dX+", "+dY+")");
		//BasicBullet bullet = new BasicBullet(getID(), getInstanceNumber(), getX(), getY(), getZ(), dX, dY);
		//bullet.fire();
		float intervalDeg = (float) Math.toRadians(degrees / projectileCount);
		for (int i = 0; i < projectileCount; i++) {
			float j = (i - (projectileCount / 2.0f));
			double deg2 = deg + (j * intervalDeg);
			//System.out.println((float) Math.cos(deg2)+", "+(float) Math.sin(deg2));
			new BasicBullet(source.getEID(), source.getInstanceNumber(), source.getX(), source.getY(), radius * 2, radius * 2, (float) Math.cos(deg2), (float) Math.sin(deg2), speed, duration, sprite, damage, source.getAffiliation()).fire();
			//bullet.fire();
		}
	}

}
