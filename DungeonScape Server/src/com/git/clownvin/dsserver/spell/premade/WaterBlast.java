package com.git.clownvin.dsserver.spell.premade;

import com.git.clownvin.dsserver.spell.Channeled;
import com.git.clownvin.dsserver.spell.EnergyComposition;

public class WaterBlast extends Channeled {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3374118826836675289L;

	public WaterBlast() {
		super("Water Blast", TargetingMode.PROJECTILE_HOSTILE, new EnergyComposition(0, 0, 0, 0, 1, 0), 10.0f, 10, 10);
	}

}
