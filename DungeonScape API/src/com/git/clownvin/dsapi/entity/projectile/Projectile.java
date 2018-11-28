package com.git.clownvin.dsapi.entity.projectile;

import com.git.clownvin.dsapi.entity.MovableEntity;

public interface Projectile extends MovableEntity{
	
	@Override
	public default long calcTicksForMoveTick() {
		return (long) (1.0 / getMoveSpeed());
	}
	
	public default float getAngle() {
		if (getVelocityX() < 0)
			return (float) (Math.atan(getVelocityY() / getVelocityX()) + Math.toRadians(180));
		return (float) Math.atan(getVelocityY() / getVelocityX());
	}
	
	public byte getAffiliation();
	
	public long getCreationTime();
	
	public int getSourceID();
	
	public float getOriginalX();
	
	public float getOriginalY();
	
}
