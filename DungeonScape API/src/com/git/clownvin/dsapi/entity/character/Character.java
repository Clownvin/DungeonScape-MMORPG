package com.git.clownvin.dsapi.entity.character;

import com.git.clownvin.dsapi.entity.MovableEntity;
import com.git.clownvin.dsapi.entity.projectile.ProjectileSource;

public interface Character extends MovableEntity, ProjectileSource {
	
	public String getName();
	
	public float getHP();
	
	public void setHP(float hp);
	
	public float getLookX();
	
	public float getLookY();
	
	public void setLookX(float x);
	
	public void setLookY(float y);
	
	public static final byte GOOD = 0;
	
	public static final byte ENEMY = 1;
	
	public static final byte TEAM1 = 2;
	
	public static final byte TEAM2 = 3;
	
	public static final byte TEAM3 = 4;
	
	public byte getAffiliation();
	
}
