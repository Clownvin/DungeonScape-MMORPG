package com.git.clownvin.dsapi.entity;

public interface MovableEntity extends Entity {
	
	public default long calcTicksForMoveTick() {
		return (long) (.2f / getMoveSpeed());
	}
	
	public void setX(float x);
	
	public void setY(float y);
	
	public float getLastX();
	
	public float getLastY();
	
	public Integer getLastIX();
	
	public Integer getLastIY();
	
	public float getVelocityX();
	
	public float getVelocityY();
	
	public float getMoveSpeed();
	
	public void setMoveSpeed(float moveSpeed);
	
	public void setVelocityX(float velocity);
	
	public void setVelocityY(float velocity);
	
	public default void normalizeVelocity() {
		float hyp = (float) Math.sqrt((getVelocityX() * getVelocityX()) + (getVelocityY() * getVelocityY()));
		setVelocityX(getVelocityX() / hyp);
		setVelocityY(getVelocityY() / hyp);
	}
	
	public void startMoving();
	
	public void stopMoving();
	
	public boolean isMoving();
}
