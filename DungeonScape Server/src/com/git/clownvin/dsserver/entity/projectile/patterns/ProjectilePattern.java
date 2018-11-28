package com.git.clownvin.dsserver.entity.projectile.patterns;

import java.io.Serializable;

import com.git.clownvin.dsserver.entity.projectile.ServerProjectile;

public abstract class ProjectilePattern implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7443463172915505300L;

	public abstract void update(ServerProjectile p);
}
