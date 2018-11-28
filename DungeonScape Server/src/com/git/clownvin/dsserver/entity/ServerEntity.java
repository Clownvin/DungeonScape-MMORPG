package com.git.clownvin.dsserver.entity;

import com.git.clownvin.dsapi.entity.Entity;
import com.git.clownvin.dsserver.Server;
import com.git.clownvin.dsserver.util.IDSystem.ID;
import com.git.clownvin.dsserver.world.Instance;
import com.git.clownvin.simplepacketframework.packet.Packet;

public abstract class ServerEntity implements Entity {
	
	private final ID eid;
	
	private boolean needsRemoval = false;
	
	public ServerEntity() {
		eid = Server.getID();
	}
	
	public void dispose() {
		eid.dispose();
	}
	
	public void flagRemoval() {
		needsRemoval = true;
	}
	
	public final int getEID() {
		return eid.getID();
	}
	
	public abstract Instance getInstance();
	
	public abstract int getInstanceNumber();
	
	@Override
	public final boolean needsRemoval() {
		return needsRemoval;
	}
	
	public abstract void setInstance(Instance instance);
	
	public abstract void setInstanceNumber(int instanceNumber);
	
	public abstract Packet toPacket();
	
	public abstract Packet toRemovePacket();
	
	@Override
	public String toString() {
		return "Entity: Instance: "+getInstanceNumber()+", EID: "+eid+", x: "+getX()+", y: "+getY()+", ix: "+getIX()+", iy: "+getIY();
	}
}
