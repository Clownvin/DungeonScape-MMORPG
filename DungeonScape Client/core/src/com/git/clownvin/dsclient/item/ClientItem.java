package com.git.clownvin.dsclient.item;

import com.git.clownvin.dsapi.item.Item;

public class ClientItem implements Item {
	
	private final String name;
	private final int iid;
	private final long amount;
	
	public ClientItem(String name, int iid, long amount) {
		this.name = name;
		this.iid = iid;
		this.amount = amount;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getItemID() {
		return iid;
	}

	@Override
	public long getItemAmount() {
		return amount;
	}

	@Override
	public boolean stackable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long maxStack() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean equipable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int equipSlot() {
		// TODO Auto-generated method stub
		return 0;
	}

}
