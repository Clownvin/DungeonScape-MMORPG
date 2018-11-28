package com.git.clownvin.dsserver.item;

import java.io.Serializable;

public class ItemDefinition implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 390545370679441673L;
	public final String name;
	public final String plural;
	public final int id;
	public final boolean stackable;
	public final long maxStack;
	public final boolean equipable;
	public final int equipSlot;
	
	public ItemDefinition(String name, String plural, int id, boolean stackable, long maxStack, boolean equipable, int equipSlot) {
		this.name = name;
		this.plural = plural;
		this.id = id;
		this.stackable = stackable;
		this.maxStack = maxStack;
		this.equipable = equipable;
		this.equipSlot = equipSlot;
	}
}
