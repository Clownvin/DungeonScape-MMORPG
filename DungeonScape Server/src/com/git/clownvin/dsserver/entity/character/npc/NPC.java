package com.git.clownvin.dsserver.entity.character.npc;

import java.util.LinkedList;

import com.git.clownvin.dsapi.config.Config;
import com.git.clownvin.dsapi.world.Chunk;
import com.git.clownvin.dsserver.Server;
import com.git.clownvin.dsserver.connection.UserConnection;
import com.git.clownvin.dsserver.entity.character.ServerCharacter;
import com.git.clownvin.dsserver.entity.projectile.patterns.BeamPattern;
import com.git.clownvin.dsserver.world.Instance;
import com.git.clownvin.dsserver.world.Instances;
import com.git.clownvin.dsserver.world.NPCSpawn;
import com.git.clownvin.dsserver.world.ServerChunk;
import com.git.clownvin.math.MathUtil;

public class NPC extends ServerCharacter {
	public static final int SEARCH_RATE = 64;
	public static final int SEARCH_RATE_VARIATION = 4;
	protected final NPCDefinition definition;
	protected float hp = 100;
	protected float x, y, lastX, lastY;
	protected Integer iX, iY, lastIX, lastIY;
	protected final int instanceNumber;
	protected Instance instance = null;
	protected final NPCSpawn spawn;
	protected boolean aggressive = false;
	protected final int searchRate;
	protected boolean hasTarget = false;
	protected Integer targetEID = -1;

	
	public NPC(NPCDefinition definition, NPCSpawn spawn, int instanceNumber, float x, float y) {
		this.definition = definition;
		//System.out.println("Does null? "+definition == null);
		this.instanceNumber = instanceNumber;
		this.spawn = spawn;
		this.x = x;
		this.y = y;
		setX(x);
		setY(y);
		updateLocation();
		Server.getCharacters().add(this);
		//System.out.println("spawned "+this);
		searchRate = (int) (SEARCH_RATE + ((Math.random() * SEARCH_RATE_VARIATION) - (SEARCH_RATE_VARIATION / 2.0f)));
	}
	
	public NPCDefinition getDefinition() {
		return definition;
	}
	
	@Override
	public float getDamageReduction() {
		return definition.damageReduction;
	}
	
	@Override
	public String getName() {
		return definition.name;
	}

	@Override
	public float getHP() {
		return hp;
	}

	@Override
	public void setHP(float hp) {
		this.hp = hp;
		updateStatus();
	}

	@Override
	public void setX(float x) {
		this.lastX = x;
		this.lastIX = MathUtil.ard(lastX);
		this.x = x;
		this.iX = MathUtil.ard(x);
	}

	@Override
	public void setY(float y) {
		this.lastY = y;
		this.lastIY = MathUtil.ard(lastY);
		this.y = y;
		this.iY = MathUtil.ard(y);
	}

	@Override
	public float getLastX() {
		return lastX;
	}

	@Override
	public float getLastY() {
		return lastY;
	}

	@Override
	public Integer getLastIX() {
		return lastIX;
	}

	@Override
	public Integer getLastIY() {
		return lastIY;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public Integer getIX() {
		return iX;
	}

	@Override
	public Integer getIY() {
		return iY;
	}

	@Override
	public float getWidth() {
		return definition.width;
	}

	@Override
	public float getHeight() {
		return definition.height;
	}

	@Override
	public float getResistance() {
		return 0;
	}

	@Override
	public int getSprite() {
		return definition.sprite;
	}

	@Override
	public int getFireRate() {
		return definition.fireRate;
	}

	@Override
	public float getRadius() {
		return definition.radius;
	}
	
	@Override
	public void dispose() {
		Instance i = Instances.get(getInstanceNumber());
		i.removeEntity(this, true);
		if (getInstanceNumber() == Instances.OVERWORLD) {
			spawn.deactivate();
		}
		super.dispose();
		//Server.getCharacters().remove(this);
	}
	
	@Override
	public void checkSurroundings() {
		super.checkSurroundings();
		if (Server.getTickCount() % searchRate != 0) {
			return;
		}
		float rad2 = definition.aggressionRadius * definition.aggressionRadius;
		ServerCharacter target = Server.getCharacters().get(this.targetEID);
		if (!hasTarget ||  target == null || MathUtil.distanceNoRoot(getX(), getY(), target.getX(), target.getY()) > rad2) {
			hasTarget = false;
			ServerChunk chunk;
			float dist = Float.MAX_VALUE;
			int closestEID = -1;
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					Integer x = getIX() + (i * Chunk.WIDTH);
					Integer y = getIY() + (j * Chunk.HEIGHT);
					chunk = getInstance().getChunk(x, y);
					LinkedList<UserConnection> users = chunk.getConnections();
					synchronized (users) {
						for (UserConnection user : users) {
							if (user.getCharacter() == null)
								continue;
							float d = MathUtil.distanceNoRoot(user.getCharacter().getX(), user.getCharacter().getY(), getX(), getY());
							if (d < dist) {
								dist = d;
								closestEID = user.getCharacter().getEID();
							}
						}
					}
				}
			}
			if (dist < definition.aggressionRadius * definition.aggressionRadius) {
				hasTarget = true;
				targetEID = closestEID;
			}
		}
	}
	
	long lastFireTick = 0L;
	int fireCount = 0;
	
	public int getFireCount() {
		return fireCount;
	}
	
	@Override
	public void update() {
		super.update();
		//System.out.println(this.getTicksForMoveTick());
		if (getHP() <= 0.0f) {
			flagRemoval();
			return;
		}
		if (hasTarget && this.getFireRate() <= Server.getTickCount() - lastFireTick) {
			ServerCharacter target = Server.getCharacters().get(targetEID);
			if (target == null) {
				hasTarget = false;
			} else {
				setLookX(target.getX() - getX());
				setLookY(target.getY() - getY());
				Server.getFiringPattern(definition.pattern).update(this);
				lastFireTick = Server.getTickCount();
				fireCount++;
			}
		}
			
	}

	@Override
	public Instance getInstance() {
		return instance;
	}

	@Override
	public int getInstanceNumber() {
		return instanceNumber;
	}

	@Override
	public void setInstance(Instance instance) {
		this.instance = instance;
	}

	@Override
	public void setInstanceNumber(int instanceNumber) {
		throw new UnsupportedOperationException("Cannot change instance of NPC");
	}

	@Override
	public byte getAffiliation() {
		return definition.affiliation;
	}

}
