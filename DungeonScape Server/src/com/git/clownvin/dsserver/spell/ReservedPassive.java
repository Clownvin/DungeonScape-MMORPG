package com.git.clownvin.dsserver.spell;

public class ReservedPassive extends Spell {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8005958252125604387L;
	private final float reserveAmount;
	
	public ReservedPassive(String name, TargetingMode mode, EnergyComposition composition, float manaCost, float cooldown, float reserveAmount) {
		super(name, mode, composition, manaCost, cooldown);
		this.reserveAmount = reserveAmount;
	}
	
	public float getReserveAmount() {
		return reserveAmount;
	}

}
