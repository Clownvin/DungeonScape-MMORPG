package com.git.clownvin.dsserver.spell;

import java.io.Serializable;

public abstract class Spell implements Serializable {
	
	public static enum TargetingMode {
		TARGETED_SELF, TARGETED_FRIENDLY, TARGETED_HOSTILE, PROJECTILE_FRIENDLY, PROJECTILE_HOSTILE;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5195621623795844256L;
	public final float manaCost;
	public final float cooldown;
	public final String name;
	public final TargetingMode mode;
	public final EnergyComposition composition;
	
	public Spell(String name, TargetingMode mode, EnergyComposition composition, float manaCost, float cooldown) {
		this.name = name;
		this.mode = mode;
		this.composition = composition;
		this.manaCost = manaCost;
		this.cooldown = cooldown;
	}
}
