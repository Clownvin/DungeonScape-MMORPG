package com.git.clownvin.dsapi.entity.projectile;

import com.git.clownvin.dsapi.entity.Entity;

public interface ProjectileSource extends Entity {
	public float getShootX();
	public float getShootY();
	public int getInstanceNumber();
	public byte getAffiliation();
	public int getFireCount();
}
