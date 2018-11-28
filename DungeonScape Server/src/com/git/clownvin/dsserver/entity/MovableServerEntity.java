package com.git.clownvin.dsserver.entity;

import com.git.clownvin.dsapi.entity.MovableEntity;
import com.git.clownvin.dsserver.Server;
import com.git.clownvin.dsserver.world.Instance;
import com.git.clownvin.dsserver.world.Instances;
import com.git.clownvin.simplepacketframework.packet.Packet;

public abstract class MovableServerEntity extends ServerEntity implements MovableEntity {

	private float velocityX = 0.0f, velocityY = 0.0f;
	
	private float moveSpeed; // Amount to move per second
	
	private long movementTick = 0L; //
	
	private long ticksForMoveTick = 0L;
	
	private boolean moving = false;
	
	public MovableServerEntity(float startMoveSpeed) {
		setMoveSpeed(startMoveSpeed);
	}
	
	public abstract Packet getMovePacket();
	
	@Override
	public float getMoveSpeed() {
		return moveSpeed;
	}
	@Override
	public float getVelocityX() {
		return velocityX;
	}
	
	@Override
	public float getVelocityY() {
		return velocityY;
	}
	
	protected abstract void handleMovement();
	
	public abstract boolean hasMovePacket();
	
	@Override
	public boolean isMoving() {
		return moving;
	}
	
	@Override
	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
		this.ticksForMoveTick = calcTicksForMoveTick();
	}
	
	@Override
	public void setVelocityX(float velocity) {
		//System.out.println("Setting X velocity: "+velocity);
		velocityX = velocity;
	}
	
	@Override
	public void setVelocityY(float velocity) {
		velocityY = velocity;
		//System.out.println("Setting Y velocity: "+velocity);
	}
	
	@Override
	public void startMoving() {
		moving = true;
		setMoveTimer();
	}
	
	@Override
	public void stopMoving() {
		moving = false;
	}
	
	@Override
	public void update() {
		if (needsRemoval())
			return;
		if (moving && shouldMoveTick()) {
			handleMovement();
			setMoveTimer();
		}
	}
	
	@Override
	public String toString() {
		return super.toString()+"\nMovableEntity: TFM: "+this.getTicksForMoveTick()+", lastX: "+getLastX()+", lastY: "+getLastY()+", lastIX: "+getLastIX()+", lastIY: "+getLastIY()+", vX: "+getVelocityX()+", vY: "+getVelocityY();
	}
	
	public final void setMoveTimer() {
		movementTick = Server.getTickCount();
	}
	
	public float getEstX() {
		float dist = (getTicksForMoveTick() * getMoveSpeed()) / (Server.getTickCount() - getLastMoveTick());
		return getX() + (dist * getVelocityX());
	}
	
	public float getEstY() {
		float dist = (getTicksForMoveTick() * getMoveSpeed()) / (Server.getTickCount() - getLastMoveTick());
		return getY() + (dist * getVelocityY());
	}
	
	public long getLastMoveTick() {
		return movementTick;
	}
	
	public boolean shouldMoveTick() {
		return Server.getTickCount() - movementTick >= getTicksForMoveTick();
	}
	
	public final long getTicksForMoveTick() {
		return ticksForMoveTick;
	}
	
	public void updateLocation() {
		//System.out.println(this+" updating location.");
		Instance instance = Instances.get(getInstanceNumber());//.getChunk(getX(), getY(), getZ());
		if (instance != getInstance()) {
			if (getInstance() != null)
				getInstance().removeEntity(this, true);
			instance.addEntity(this);
			setInstance(instance);
		}
		else
			instance.moveEntity(this);
	}

}
