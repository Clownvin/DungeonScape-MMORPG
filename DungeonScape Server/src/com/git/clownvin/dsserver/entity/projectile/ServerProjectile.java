package com.git.clownvin.dsserver.entity.projectile;

import com.git.clownvin.dsapi.entity.Entity;
import com.git.clownvin.dsapi.entity.character.Character;
import com.git.clownvin.dsapi.entity.projectile.Projectile;
import com.git.clownvin.dsapi.packet.ProjectilePacket;
import com.git.clownvin.dsapi.packet.RemoveProjectilePacket;
import com.git.clownvin.dsapi.world.Tile;
import com.git.clownvin.dsserver.Server;
import com.git.clownvin.dsserver.entity.MovableServerEntity;
import com.git.clownvin.dsserver.entity.character.ServerCharacter;
import com.git.clownvin.dsserver.world.Instance;
import com.git.clownvin.dsserver.world.Instances;
import com.git.clownvin.dsserver.world.ServerChunk;
import com.git.clownvin.math.MathUtil;
import com.git.clownvin.simplepacketframework.packet.Packet;

public abstract class ServerProjectile extends MovableServerEntity implements Projectile {
	
	protected float x = 0, y = 0, lastX, lastY, width, height;
	protected Integer iX = 0, iY = 0, lastIX, lastIY;
	protected final int duration;
	protected int sprite;
	protected float damage;
	protected float radius, originX, originY;
	protected int instanceNumber, sourceID;
	protected Instance instance;
	protected long creationTime = 0L;
//	protected IDTag tag;
	protected float originalX, originalY;
	protected byte affiliation;
	
	public ServerProjectile(int sourceID, int instanceNumber, float startX, float startY, float width, float height, float velocityX, float velocityY, float speed, int duration, int sprite, float damage, byte affiliation) {
		super(speed);
		this.instanceNumber = instanceNumber;
		this.instance = null;
		setX(startX);
		setY(startY);
		this.width = width;
		this.height = height;
		this.radius = width / 2;
		this.originalX = startX;
		this.originalY = startY;
		this.sourceID = sourceID;
		setVelocityX(velocityX);
		setVelocityY(velocityY);
		normalizeVelocity();
		this.sprite = sprite;
		this.duration = duration;
		this.damage = damage;
		this.affiliation = affiliation;
		//System.out.println("Creating bullet with ms per tick: "+(this.getTicksForMoveTick() * Config.TICK_RATE));
		//System.out.println("Creating projectile with velocity: "+getVelocityX()+", "+getVelocityY());
		//this.tag = ClosedIDSystem.getTag();
	}
	
	public byte getAffiliation() {
		return affiliation;
	}
	
	public float getDamage() {
		return damage;
	}

	public int getDuration() {
		return duration;
		//return 2000;
	}

	@Override
	public int getSprite() {
		return sprite;
	}
	
	@Override
	public void dispose() {
		Instance i = Instances.get(getInstanceNumber());
		//System.out.println("Entity disposing at instance "+i);
		i.removeEntity(this, true);
		//System.out.println("Removed entity?");
		//tag.returnTag();
		super.dispose();
	}
	
	public void fire() {
		//System.out.println("Firing");
		creationTime = System.currentTimeMillis();
		Server.getProjectiles().add(this);
		this.startMoving();
		updateLocation();
	}
	
	public long getCreationTime() {
		return creationTime;
	}
	
	public float getRadius() {
		return radius;
	}
	
	@Override
	public float getWidth() {
		return width;
	}
	
	@Override
	public float getHeight() {
		return height;
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
	public Integer getIX() {
		return iX;
	}
	
	@Override
	public Integer getIY() {
		return iY;
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
	public float getLastX() {
		return lastX;
	}

	@Override
	public float getLastY() {
		return lastY;
	}

	@Override
	public Packet getMovePacket() {
		throw new UnsupportedOperationException("There is no move packet, wtf");
		//return new MoveCharacterPacket(getID(), getMoveSpeed(), getX(), getY(), getZ());
	}

	public float getOriginalX() {
		return originalX;
	}

	public float getOriginalY() {
		return originalY;
	}

	@Override
	public float getResistance() {
		return 0.0f;
	}

	@Override
	public int getSourceID() {
		return sourceID;
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
	protected void handleMovement() {
		normalizeVelocity();
		float moveSpeed = getTicksForMoveTick() * getMoveSpeed();
		//float startX = getOriginX();
		//float startY = getOriginX();waad
		setX(getX() + (getVelocityX() * moveSpeed));
		setY(getY() + (getVelocityY() * moveSpeed));
		updateLocation();
		ServerChunk chunk;
		chunk = Instances.get(getInstanceNumber()).getChunk(getIX(), getIY());
		if (chunk.getResistance(getIX(), getIY()) >= 1.0f || System.currentTimeMillis() - creationTime >= getDuration()) {
			flagRemoval();
			return;
		}
	}

	@Override
	public boolean hasMovePacket() {
		return false;
	}

	@Override
	public void setInstance(Instance instance) {
		this.instance = instance;
	}

	@Override
	public void setInstanceNumber(int instanceNumber) {
		throw new UnsupportedOperationException("You cannot change the instance of a projectile, wtf");
	}

	@Override
	public void setX(float x) {
		lastX = this.x;
		this.x = x;
		lastIX = iX;
		iX = MathUtil.ard(x);
	}

	@Override
	public void setY(float y) {
		lastY = this.y;
		this.y = y;
		lastIY = iY;
		iY = MathUtil.ard(y);
	}

	@Override
	public Packet toPacket() {
		return new ProjectilePacket(this);
	}

	@Override
	public Packet toRemovePacket() {
		return new RemoveProjectilePacket(getEID());
	}

	public boolean intersects(Entity o) {
		if (o instanceof Character)
			return MathUtil.doIntersect((getEstX() * Tile.WIDTH), (getEstY() * Tile.HEIGHT), getRadius(), o.getActualX(), o.getActualY(), ((ServerCharacter) o).getRadius());
		throw new RuntimeException("No case for entity: "+o);
	}

}
