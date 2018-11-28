package com.git.clownvin.dsapi.packet;

import com.git.clownvin.simplepacketframework.packet.Packet;

public class MessagePacket extends Packet {
	
	public static final byte WHITE = 0;
	public static final byte BLACK = 1;
	public static final byte GREEN = 2;
	public static final byte BLUE = 3;
	public static final byte CYAN = 4;
	public static final byte PURPLE = 5;
	public static final byte ORANGE = 6;
	public static final byte YELLOW = 7;
	public static final byte MAGENTA = 8;
	public static final byte RED = 9;
	
	public static final int SYSTEM = -1;
	
	private String message;
	private int senderID;
	private byte color;
	
	public MessagePacket(String message, int senderID, byte color) {
		super(false, toBytes(message, senderID, color), -1);
		this.message = message;
		this.senderID = senderID;
		this.color = color;
	}
	
	public MessagePacket(boolean construct, byte[] bytes, int length) {
		super(construct, bytes, length);
	}
	
	public String getMessage() {
		return message;
	}
	
	public byte getColor() {
		return color;
	}
	
	public int getSenderID() {
		return senderID;
	}
	
	private static byte[] toBytes(String message, int senderID, byte color) {
		byte[] messageBytes = message.getBytes();
		byte[] bytes = new byte[5 + messageBytes.length];
		int i = 0;
		bytes[i++] = (byte) ((senderID >> 24) & 0xFF);
		bytes[i++] = (byte) ((senderID >> 16) & 0xFF);
		bytes[i++] = (byte) ((senderID >> 8) & 0xFF);
		bytes[i++] = (byte) (senderID & 0xFF);
		bytes[i++] = color;
		for (byte b : messageBytes) {
			bytes[i++] = b;
		}
		return bytes;
	}

	@Override
	protected void construct(byte[] bytes, int length) {
		int i = 0;
		senderID = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
		color = bytes[i++];
		StringBuilder builder = new StringBuilder(length - 5);
		for (; i < length; i++)
			builder.append((char) bytes[i]);
		message = builder.toString();
	}

	@Override
	public boolean shouldEncrypt() {
		return false;
	}

}
