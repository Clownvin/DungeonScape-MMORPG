package com.git.clownvin.dsserver.spell;

public class Instant extends Spell {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8613888883890168164L;

	public Instant(String name, TargetingMode mode, EnergyComposition composition, float manaCost, float cooldown) {
		super(name, mode, composition, manaCost, cooldown);
	}

}
