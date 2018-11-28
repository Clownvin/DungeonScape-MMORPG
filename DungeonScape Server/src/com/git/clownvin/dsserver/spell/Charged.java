package com.git.clownvin.dsserver.spell;

public class Charged extends Spell {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1239232448167633045L;
	
	public static final byte CHARGE_EFFECT = 1;
	public static final byte CHARGE_RADIUS = 2;
	
	private final float maxChargeTime;
	private final byte chargeType;
	
	public Charged(String name, TargetingMode mode, EnergyComposition composition, float manaCost, float cooldown, float maxChargeTime, byte chargeType) {
		super(name, mode, composition, manaCost, cooldown);
		this.maxChargeTime = maxChargeTime;
		this.chargeType = chargeType;
	}
	
	public float getMaxChargeTime() {
		return maxChargeTime;
	}
	
	public byte getChargeType() {
		return chargeType;
	}
}
