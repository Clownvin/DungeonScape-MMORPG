package com.git.clownvin.dsserver.spell;

public class EnergyComposition {
	//elemental
	public final float death;
	public final float life;
	public final float air;
	public final float fire;
	public final float water;
	public final float earth;
	
	public EnergyComposition(float death, float life, float air, float fire, float water, float earth) {
		float total = death + life + air + fire + water + earth;
		this.death = death / total;
		this.life = life / total;
		this.air = air / total;
		this.fire = fire / total;
		this.water = water / total;
		this.earth = earth / total;
	}
}
