package com.git.clownvin.dsserver.entity.object;

import com.git.clownvin.dsapi.entity.object.GameObject;
import com.git.clownvin.dsapi.packet.ObjectPacket;
import com.git.clownvin.dsapi.packet.RemoveObjectPacket;
import com.git.clownvin.dsserver.entity.ServerEntity;
import com.git.clownvin.dsserver.world.Instance;
import com.git.clownvin.math.MathUtil;
import com.git.clownvin.simplepacketframework.packet.Packet;

public class ServerGameObject extends ServerEntity implements GameObject {
	
	protected final float x, y;
	protected final Integer iX, iY;
	protected final int instanceNumber;
	protected Instance instance = null;
	protected final ObjectDefinition definition;
	
	public ServerGameObject(ObjectDefinition definition, float x, float y, Instance instance) {
		this.x = x;
		iX = MathUtil.ard(x);
		this.y = y;
		iY = MathUtil.ard(y);
		this.instanceNumber = instance.instanceNumber;
		this.definition = definition;
		this.instance = instance;
	}
	
	public String getName() {
		return definition.name;
	}
	
	@Override
	public void dispose() {
		instance.removeEntity(this, true);
		super.dispose();
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
	public float getResistance() {
		return definition.resistance;
	}

	@Override
	public int getSprite() {
		return definition.sprite;
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
	public void setInstance(Instance instance) {
		throw new UnsupportedOperationException("Cannot change instance of an object");
	}

	@Override
	public void setInstanceNumber(int instanceNumber) {
		throw new UnsupportedOperationException("Cannot change instance of an object");
	}

	@Override
	public Packet toPacket() {
		return new ObjectPacket(this);
	}

	@Override
	public Packet toRemovePacket() {
		return new RemoveObjectPacket(this.getEID());
	}

	@Override
	public void update() {
		//Probably do nothing?
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
	public float getSpriteWidth() {
		return definition.spriteWidth;
	}

	@Override
	public float getSpriteHeight() {
		return definition.spriteHeight;
	}

}
