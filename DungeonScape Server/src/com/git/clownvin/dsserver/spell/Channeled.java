package com.git.clownvin.dsserver.spell;

public class Channeled extends Spell {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4111777298576131503L;

	private final float maxChannelDuration;
	
	public Channeled(String name, TargetingMode mode, EnergyComposition composition, float manaCost, float cooldown, float maxChannelDuration) {
		super(name, mode, composition, manaCost, cooldown);
		this.maxChannelDuration = maxChannelDuration;
	}
	
	public float getMaxChannelDuration() {
		return maxChannelDuration;
	}

}
