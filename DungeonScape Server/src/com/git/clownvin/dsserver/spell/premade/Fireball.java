package com.git.clownvin.dsserver.spell.premade;

import com.git.clownvin.dsapi.config.Config;
import com.git.clownvin.dsserver.spell.EnergyComposition;
import com.git.clownvin.dsserver.spell.Instant;

public class Fireball extends Instant {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1818853087736653375L;

	public Fireball() {
		super("Fireball", TargetingMode.PROJECTILE_HOSTILE, new EnergyComposition(0, 0, 0, 1, 0, 0), 10.0f, Config.getTicksForMS(5000));
	}

}
