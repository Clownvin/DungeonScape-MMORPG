package com.git.clownvin.dsserver.entity.character;

import java.util.Set;

import com.git.clownvin.dsapi.config.Config;
import com.git.clownvin.dsapi.entity.character.Character;
import com.git.clownvin.dsapi.packet.CharacterPacket;
import com.git.clownvin.dsapi.packet.CharacterStatusPacket;
import com.git.clownvin.dsapi.packet.MoveCharacterPacket;
import com.git.clownvin.dsapi.packet.RemoveCharacterPacket;
import com.git.clownvin.dsapi.world.Tile;
import com.git.clownvin.dsserver.entity.MovableServerEntity;
import com.git.clownvin.dsserver.entity.projectile.ServerProjectile;
import com.git.clownvin.dsserver.world.ServerChunk;
import com.git.clownvin.math.MathUtil;
import com.git.clownvin.simplepacketframework.packet.Packet;

public abstract class ServerCharacter extends MovableServerEntity implements Character {
	
	private float lookX = 0.0f, lookY = 0.0f;
	private boolean updateStatus = false;
	
	public ServerCharacter() {
		super(Config.DEFAULT_MOVE_SPEED);
	}
	
	public boolean shouldUpdateStatus() {
		return updateStatus;
	}
	
	public void updateStatus() {
		updateStatus = true;
	}
	
	@Override
	public float getLookX() {
		return lookX;
	}
	
	@Override
	public float getLookY() {
		return lookY;
	}
	
	@Override
	public void setLookX(float x) {
		this.lookX = x;
		updateStatus();
	}
	
	@Override
	public void setLookY(float y) {
		this.lookY = y;
		updateStatus();
	}
	
	@Override
	public float getShootX() {
		return getLookX();
	}

	@Override
	public float getShootY() {
		return getLookY();
	}
	
	public abstract float getDamageReduction();
	
	public void applyDamage(float damage) {
		setHP(getHP() - (damage * (1 - getDamageReduction())));
	}
	
	public abstract int getFireRate();
	
	@Override
	public Packet getMovePacket() {
		return new MoveCharacterPacket(getEID(), getMoveSpeed(), getX(), getY());
	}
	
	@Override
	public void update() {
		super.update();
		checkSurroundings();
		if (shouldUpdateStatus()) {
			getInstance().sendPacketToChunks(getIX(), getIY(), new CharacterStatusPacket(this));
			updateStatus = false;
		}
	}
	
	protected void checkSurroundings() {
		ServerChunk chunk;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				Integer x = getIX() + i;
				Integer y = getIY() + j;
				chunk = getInstance().getChunk(x, y);
				synchronized (chunk) {
				Set<Integer> keys = chunk.entitiesKeySet(x, y);
				ServerProjectile p;
				for (Integer key : keys) {
					if (!(chunk.getEntity(x, y, key) instanceof ServerProjectile))
						continue;
					p = (ServerProjectile) chunk.getEntity(x, y, key);
					if (p.getAffiliation() == getAffiliation() || !p.intersects(this)) {
						continue;
					}
					applyDamage(p.getDamage());
					p.flagRemoval();
					//System.out.println(p.getEID()+" is flagged ");
				}
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return super.toString()+"\nCharacter: Name: "+getName()+", hp: "+getHP();
	}
	
	public abstract float getRadius();
	
	@Override
	protected void handleMovement() {
		normalizeVelocity();
		float moveSpeed = getTicksForMoveTick() * getMoveSpeed();
		float distX = (getVelocityX() * (moveSpeed));
		float distY = (getVelocityY() * (moveSpeed));
		//System.out.println("dist x: "+distX+", dist y: "+distY);
		float newX = getX() + distX;
		float newY = getY() + distY;
		int newIX = MathUtil.ard(newX);
		int newIY = MathUtil.ard(newY);
		//if (MathUtil.ard(newX) != MathUtil.ard(getX()) || MathUtil.ard(newY) != MathUtil.ard(getY())) {
		float resistance = this.getInstance().getChunk(newIX, newIY).getResistance(newIX, newIY);
		if (resistance >= 1.0f) {
			if (this.getInstance().getChunk(newIX, getIY()).getResistance(newIX, getIY()) >= 1.0f) {
				if (newX > getX()) {
					newX = MathUtil.ard(newX) - (getRadius() / Tile.WIDTH);
				} else {
					newX = MathUtil.ard(getX()) + (getRadius() / Tile.WIDTH);
				}
			}
			if (this.getInstance().getChunk(getIX(), newIY).getResistance(getIX(), newIY) >= 1.0f) {
				if (newY > getY()) {
					newY = MathUtil.ard(newY) - (getRadius() / Tile.HEIGHT);
				} else {
					newY = MathUtil.ard(getY()) + (getRadius() / Tile.HEIGHT);
				}
			}
		} else if (resistance > 0) {
			if (this.getInstance().getChunk(newIX, getIY()).getResistance(newIX, getIY()) > 0)
				newX -= distX * resistance;
			if (this.getInstance().getChunk(getIX(), newIY).getResistance(getIX(), newIY) > 0)
				newY -= distY * resistance;
		}
		setX(newX);
		setY(newY);
		updateLocation();
	}
	
	@Override
	public boolean hasMovePacket() {
		return true;
	}
	
	@Override
	public Packet toPacket() {
		//System.out.println("Sending char: "+this.getName()+", "+this.getEID());
		return new CharacterPacket(this);
	}
	
	@Override
	public Packet toRemovePacket() {
		return new RemoveCharacterPacket(this.getEID());
	}

}
