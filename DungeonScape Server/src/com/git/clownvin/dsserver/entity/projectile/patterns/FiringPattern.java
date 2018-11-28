package com.git.clownvin.dsserver.entity.projectile.patterns;

import java.io.Serializable;

import com.git.clownvin.dsapi.entity.projectile.ProjectileSource;

public abstract class FiringPattern implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5012119102932060174L;
	
	protected float radius, speed, damage;
	protected int duration, sprite;
	
	public FiringPattern(float radius, float speed, int duration, int sprite, float damage) {
		this.radius = radius;
		this.speed = speed;
		this.duration = duration;
		this.sprite = sprite;
		this.damage = damage;
	}

	public abstract void update(ProjectileSource source);
}
