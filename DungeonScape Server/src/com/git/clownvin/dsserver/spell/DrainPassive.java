package com.git.clownvin.dsserver.spell;

public class DrainPassive extends Spell {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1104183764316723188L;
	private final float drainAmount;
	
	public DrainPassive(String name, TargetingMode mode, EnergyComposition composition, float manaCost, float cooldown, float drainAmount) {
		super(name, mode, composition, manaCost, cooldown);
		this.drainAmount = drainAmount;
	}
	
	public float getDrainAmount() {
		return drainAmount;
	}

}
