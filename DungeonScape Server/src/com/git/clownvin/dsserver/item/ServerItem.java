package com.git.clownvin.dsserver.item;

import java.io.Serializable;

import com.git.clownvin.dsapi.item.Item;

public class ServerItem implements Item, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2923306617729705964L;
	protected ItemDefinition definition;
	protected int iid;
	protected long itemAmount = 0;
	
	public ServerItem() {
		
	}
	
	public ServerItem(int iid, long itemAmount) {
		this.iid = iid;
		this.definition = Items.getItemDefinition(iid);
		this.itemAmount = itemAmount;
		if (!stackable() && itemAmount > 1)
			throw new IllegalArgumentException("Non-stackable item cannot have amount greater than 1.");
	}

	@Override
	public int getItemID() {
		return definition.id;
	}

	@Override
	public boolean stackable() {
		return definition.stackable;
	}

	@Override
	public boolean equipable() {
		return definition.equipable;
	}

	@Override
	public int equipSlot() {
		return definition.equipSlot;
	}

	@Override
	public long getItemAmount() {
		return itemAmount;
	}
	
	/**
	 * 
	 * @param amount amount to try and add
	 * @return amount left to add, or 0 if none
	 */
	public long addItems(long amount) {
		if (maxStack() < itemAmount + amount) {
			System.out.println(itemAmount+" + "+amount+" > "+maxStack());
			long left = amount - (maxStack() - itemAmount);
			itemAmount = maxStack();
			return left;
		}
		System.out.println(itemAmount+" + "+amount+" < "+maxStack());
		this.itemAmount += amount;
		return 0;
	}
	
	/**
	 * 
	 * @param amount amount to try and remove
	 * @return amount left to remove, or 0 if none
	 */
	public long removeItems(long amount) {
		if (this.itemAmount < amount) {
			long left = amount - this.itemAmount;
			this.itemAmount = 0;
			return left;
		}
		this.itemAmount -= amount;
		return 0;
	}

	@Override
	public long maxStack() {
		return definition.maxStack;
	}

	@Override
	public String getName() {
		return definition.name;
	}
	
	@Override
	public String toString() {
		return "Item: name: "+getName()+", id: "+getItemID()+", stackable: "+stackable()+", ammount: "+getItemAmount()+", maxstack: "+maxStack()+", equipable: "+equipable()+", equipslot: "+equipSlot();
	}

}
