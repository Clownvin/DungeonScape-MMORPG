package com.git.clownvin.dsclient.entity;

import com.git.clownvin.dsapi.config.Config;
import com.git.clownvin.dsapi.entity.MovableEntity;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.math.MathUtil;

public abstract class MovableClientEntity extends ClientEntity implements MovableEntity {

	protected float lastX, lastY, moveSpeed;
	
	public MovableClientEntity(DSGame game, int eid, float x, float y, float width, float height, int sprite, float lastX, float lastY, float moveSpeed) {
		super(game, eid, x, y, width, height, sprite);
		this.lastX = lastX;
		this.lastY = lastY;
		this.moveSpeed = moveSpeed;
	}

	protected long moveTimer = 0L;

	@Override
	public float getX() {
		if (isMoving()) {
			return lastX + (((System.currentTimeMillis() - moveTimer) / (this.calcTicksForMoveTick() * (float) Config.TICK_RATE)) * (x - lastX));
		}
		return x;
	}

	@Override
	public float getY() {
		if (isMoving()) {
			return lastY + (((System.currentTimeMillis() - moveTimer) / (this.calcTicksForMoveTick() * (float) Config.TICK_RATE))  * (y - lastY));
		}
		return y;
	}
	
	@Override
	public Integer getLastIX() {
		return MathUtil.ard(getLastX());
	}

	@Override
	public Integer getLastIY() {
		return MathUtil.ard(getLastY());
	}

	@Override
	public Integer getIX() {
		return MathUtil.ard(getX());
	}

	@Override
	public Integer getIY() {
		return MathUtil.ard(getY());
	}

	@Override
	public void setX(float x) {
		lastX = this.x;
		this.x = x;
		oX = getActualX() + (getWidth() / 2);
	}

	@Override
	public void setY(float y) {
		lastY = this.y;
		this.y = y;
		oY = getActualY() + (getHeight() / 2);
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
	public float getVelocityX() {
		throw new UnsupportedOperationException("Is this supposed to happen?");
	}

	@Override
	public float getVelocityY() {
		throw new UnsupportedOperationException("Is this supposed to happen?");
	}

	@Override
	public float getMoveSpeed() {
		return moveSpeed;
	}

	@Override
	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
		moveTimer = System.currentTimeMillis();
	}

	@Override
	public void setVelocityX(float velocity) {
		throw new UnsupportedOperationException("Is this supposed to happen?");
	}

	@Override
	public void setVelocityY(float velocity) {
		throw new UnsupportedOperationException("Is this supposed to happen?");
	}

	@Override
	public void startMoving() {
		throw new UnsupportedOperationException("Is this supposed to happen?");
	}

	@Override
	public void stopMoving() {
		throw new UnsupportedOperationException("Is this supposed to happen?");
	}

	@Override
	public boolean isMoving() {
		return System.currentTimeMillis() - moveTimer < this.calcTicksForMoveTick() * Config.TICK_RATE;
	}

}
