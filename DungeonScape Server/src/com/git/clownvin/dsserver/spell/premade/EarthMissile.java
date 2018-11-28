package com.git.clownvin.dsserver.spell.premade;

import com.git.clownvin.dsserver.spell.EnergyComposition;
import com.git.clownvin.dsserver.spell.Instant;

public class EarthMissile extends Instant {

	public EarthMissile(String name, TargetingMode mode, EnergyComposition composition, float manaCost,
			float cooldown) {
		super(name, mode, composition, manaCost, cooldown);
	}

}
