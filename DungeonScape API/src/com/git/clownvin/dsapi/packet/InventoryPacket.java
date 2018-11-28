package com.git.clownvin.dsapi.packet;

import com.git.clownvin.dsapi.item.Item;
import com.git.clownvin.simplepacketframework.packet.Packet;

public class InventoryPacket extends Packet {
	
	private static byte[] toBytes(Item[] items) {
		int nameLengths = 0;
		for (Item i : items) {
			nameLengths += i.getName().length() + 1;
		}
		byte[] bytes = new byte[4 + (items.length * 12) + nameLengths];
		int i = 0;
		bytes[i++] = (byte) ((items.length >> 24) & 0xFF);
		bytes[i++] = (byte) ((items.length >> 16) & 0xFF);
		bytes[i++] = (byte) ((items.length >> 8) & 0xFF);
		bytes[i++] = (byte) (items.length & 0xFF);
		for (Item item : items) {
			//iid
			bytes[i++] = (byte) ((item.getItemID() >> 24) & 0xFF);
			bytes[i++] = (byte) ((item.getItemID() >> 16) & 0xFF);
			bytes[i++] = (byte) ((item.getItemID() >> 8) & 0xFF);
			bytes[i++] = (byte) (item.getItemID() & 0xFF);
			bytes[i++] = (byte) ((item.getItemAmount() >> 56) & 0xFF);
			bytes[i++] = (byte) ((item.getItemAmount() >> 48) & 0xFF);
			bytes[i++] = (byte) ((item.getItemAmount() >> 40) & 0xFF);
			bytes[i++] = (byte) ((item.getItemAmount() >> 32) & 0xFF);
			bytes[i++] = (byte) ((item.getItemAmount() >> 24) & 0xFF);
			bytes[i++] = (byte) ((item.getItemAmount() >> 16) & 0xFF);
			bytes[i++] = (byte) ((item.getItemAmount() >> 8) & 0xFF);
			bytes[i++] = (byte) (item.getItemAmount() & 0xFF);
			for (byte b : item.getName().getBytes())
				bytes[i++] = b;
			bytes[i++] = (byte) '\n';
		}
		return bytes;
	}
	
	private int[] ids;
	private long[] amounts;
	private String[] names;
	
	public InventoryPacket(Item[] items) {
		this(false, toBytes(items), -1);
		ids = new int[items.length];
		amounts = new long[items.length];
		names = new String[items.length];
		int i = 0;
		for (Item item : items) {
			ids[i] = item.getItemID();
			amounts[i] = item.getItemAmount();
			names[i] = item.getName();
			i++;
		}
	}
	
	public int[] getIDs() {
		return ids;
	}
	
	public long[] getAmounts() {
		return amounts;
	}
	
	public String[] getNames() {
		return names;
	}
	
	public InventoryPacket(boolean construct, byte[] bytes, int length) {
		super(construct, bytes, length);
	}

	@Override
	protected void construct(byte[] bytes, int length) {
		int i = 0;
		char c = '\0';
		int size = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
		ids = new int[size];
		amounts = new long[size];
		names = new String[size];
		StringBuilder builder = new StringBuilder(10);
		for (int j = 0; j < size; j++) {
			ids[j] = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
			amounts[j] = ((bytes[i++] & 0xFFL) << 56) | ((bytes[i++] & 0xFFL) << 48) | ((bytes[i++] & 0xFFL) << 40) | ((bytes[i++] & 0xFFL) << 32) | ((bytes[i++] & 0xFFL) << 24) | ((bytes[i++] & 0xFFL) << 16) | ((bytes[i++] & 0xFFL) << 8) | (bytes[i++] & 0xFFL);
			while ((c = (char) bytes[i++]) != '\n')
				builder.append(c);
			names[j] = builder.toString();
			builder.replace(0, builder.length(), "");
		}
	}

	@Override
	public boolean shouldEncrypt() {
		// TODO Auto-generated method stub
		return false;
	}

}
