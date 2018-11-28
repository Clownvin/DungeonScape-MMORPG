package com.git.clownvin.dsserver.user;

import com.git.clownvin.simpleuserframework.User;

public class Profile extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5947593307931424147L;
	
	public float x = 0.0f;
	public float y = 0.0f;
	public float z = 0.0f;
	public float lastX = 0.0f;
	public float lastY = 0.0f;
	public float lastZ = 0.0f;
	public float hp = 100.0f;
	
	public int instanceNumber;

	public Profile(String username, byte[] salt, byte[] hash) {
		super(username, salt, hash);
	}
}
