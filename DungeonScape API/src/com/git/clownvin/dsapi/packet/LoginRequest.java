package com.git.clownvin.dsapi.packet;

import com.git.clownvin.simplepacketframework.packet.Request;

public class LoginRequest extends Request {
	
	private String username, password;
	
	public LoginRequest(String username, String password) {
		super(false, combine((username+'\0').getBytes(), password.getBytes()), -1);
		this.username = username;
		this.password = password;
	}
	
	public LoginRequest(boolean construct, byte[] bytes, int length) {
		super(construct, bytes, length);
	}

	@Override
	protected void construct(byte[] bytes, int length) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (bytes[i] == (byte) '\0') {
				username = builder.toString();
				builder = new StringBuilder();
				continue;
			}
			builder.append((char) bytes[i]);
		}
		password = builder.toString();
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

	@Override
	public boolean shouldEncrypt() {
		return true;
	}

}
