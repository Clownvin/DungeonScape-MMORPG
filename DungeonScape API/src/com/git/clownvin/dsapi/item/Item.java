package com.git.clownvin.dsapi.item;

public interface Item {
	public static final int NULL_IID = -1;
	public static final int NO_SLOT = -1;
	public static final int HEAD = 1, CHEST = 2, LEG = 3, WEAPON = 4, TRINKET1 = 5, TRINKET2 = 6;
	
	public String getName();
	
	public int getItemID();
	
	public long getItemAmount();
	
	public boolean stackable();
	
	public long maxStack();
	
	public boolean equipable();
	
	public int equipSlot();
}
