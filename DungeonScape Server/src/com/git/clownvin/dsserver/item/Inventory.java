package com.git.clownvin.dsserver.item;

import java.io.Serializable;

import com.git.clownvin.dsapi.item.Item;
import com.git.clownvin.dsapi.packet.InventoryPacket;

public class Inventory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1784798195674034494L;
	private ServerItem[] items;
	private boolean hasChanged = false;
	
	public Inventory() {
		
	}
	
	public boolean hasChanged() {
		if (hasChanged) {
			hasChanged = false;
			return true;
		}
		return false;
	}
	
	public Inventory(int size) {
		items = new ServerItem[size];
		for (int i = 0; i < items.length; i++) {
			items[i] = Items.NULL_ITEM;
		}
	}
	
	/**
	 * 
	 * @return first empty slot index, or -1 if full
	 */
	public int getFirstEmptySlot() {
		for (int i = 0; i < items.length; i++) {
			if (items[i].getItemID() == Item.NULL_IID)
				return i;
		}
		return -1;
	}
	
	/**
	 * 
	 * @param iid itemID of item to find in slot
	 * @return slot index of item, or -1 if none
	 */
	public int getItemSlot(int iid) {
		for (int i = 0; i < items.length; i++) {
			if (items[i].getItemID() == iid)
				return i;
		}
		return -1;
	}
	
	/**
	 * 
	 * @param iid itemId of item to find in slot
	 * @return first index with same itemID and enough room left for amount, or -1 if there is none
	 */
	public int getItemSlotWithRoom(int iid) {
		for (int i = 0; i < items.length; i++) {
			if (items[i].getItemID() != iid)
				continue;
			if (items[i].maxStack() == items[i].getItemAmount())
				continue;
			return i;
		}
		return -1;
	}
	
	/**
	 * 
	 * @param iid item id
	 * @return amount of items in inventory, or 0 if none
	 */
	public long getItemAmount(int iid) {
		long amount = 0L;
		for (int i = 0; i < items.length; i++) {
			if (items[i].getItemID() != iid)
				continue;
			amount += items[i].getItemAmount();
		}
		return amount;
	}
	
	public InventoryPacket toPacket() {
		return new InventoryPacket(items);
	}
	
	/**
	 * 
	 * @param item item(s) to be added
	 * @return 0 if all of item was added, else the amount remaining
	 */
	public long addItem(ServerItem item) {
		//System.out.println("");
		if (item.stackable()) {
			//System.out.println("item stackable");
			long amount = item.getItemAmount();
			//System.out.println("has "+amount+" to add");
			int index = -1;
			while (amount > 0 && (index = getItemSlotWithRoom(item.getItemID())) != -1) {
				amount = items[index].addItems(amount);
				//System.out.println("has "+amount+" left to add");
			}
			if (amount == 0) {
				//System.out.println("added all in one stack");
				cleanAndOrganize();
				return 0;
			}
			while (amount > 0 && (index = getFirstEmptySlot()) != -1) {
				items[index] = new ServerItem(item.getItemID(), 0);
				amount = items[index].addItems(amount);
			}
			//System.out.println("adding to fresh stack");
			if (index == -1) {
				cleanAndOrganize();
				return amount; 
			}
			//System.out.println("added all");
			cleanAndOrganize();
			return 0;
		}
		//System.out.println("not stackbable");
		int index = getFirstEmptySlot();
		if (index == -1) {
			//System.out.println("no inv space");
			cleanAndOrganize();
			return item.itemAmount;
		}
		//System.out.println("added");
		items[index] = new ServerItem(item.getItemID(), item.itemAmount);
		cleanAndOrganize();
		return 0;
	}
	
	/**
	 * 
	 * @param item item(s) to be removed
	 * @return amount of items removed, or 0 if none
	 */
	public long removeItem(ServerItem item) {
		long amount = item.getItemAmount();
		int index = -1;
		while (amount > 0 && (index = getItemSlot(item.getItemID())) != -1) {
			System.out.println("Here, amount: "+amount+", index: "+index);
			amount = items[index].removeItems(amount);
			if (amount > 0) {
				System.out.println(items[index].getItemAmount()+" left in that stack");
				items[index] = Items.NULL_ITEM;
			}
		}
		cleanAndOrganize();
		return item.getItemAmount() - amount;
	}
	
	public void cleanAndOrganize() {
		int moveAmount = 0;
		for (int i = 0; i < items.length; i++) {
			if (items[i].getItemAmount() <= 0)
				items[i] = Items.NULL_ITEM;
			if (items[i].getItemID() == Item.NULL_IID)
				moveAmount++;
			else if (moveAmount > 0) {
				items[i - moveAmount] = items[i];
				items[i] = Items.NULL_ITEM;
			}
		}
		hasChanged = true;
	}
}
