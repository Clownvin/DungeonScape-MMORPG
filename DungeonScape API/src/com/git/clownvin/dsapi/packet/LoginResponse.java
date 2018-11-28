package com.git.clownvin.dsapi.packet;

import java.nio.ByteBuffer;

import com.git.clownvin.simplepacketframework.packet.Response;

public class LoginResponse extends Response {
	
	public static final byte SUCCESS = 1;
	public static final byte INCORRECT = 2;
	public static final byte ALREADY_LOGGED_IN = 3;
	public static final byte EXCEPTION_OCCURED = 4;
	public static final byte USERNAME_TOO_SHORT = 5;
	public static final byte PASSWORD_TOO_SHORT = 6;
	
	private byte result;
	
	public LoginResponse(long reqID, byte result) {
		super(false, combine(ByteBuffer.allocate(8).putLong(reqID).array(), new byte[] {result}), -1);
		this.result = result;
		this.reqID = reqID;
	}

	public LoginResponse(boolean construct, byte[] bytes, int length) {
		super(construct, bytes, length);
	}

	@Override
	protected void construct(byte[] bytes, int length) {
		result = bytes[0];
	}
	
	public byte getResult() {
		return result;
	}

	@Override
	public boolean shouldEncrypt() {
		// TODO Auto-generated method stub
		return false;
	}

}
