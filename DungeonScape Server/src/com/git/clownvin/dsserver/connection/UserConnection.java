package com.git.clownvin.dsserver.connection;

import java.io.IOException;
import java.net.Socket;

import com.git.clownvin.dsapi.config.Config;
import com.git.clownvin.dsapi.packet.CharacterPacket;
import com.git.clownvin.dsapi.packet.SelfPacket;
import com.git.clownvin.dsserver.Server;
import com.git.clownvin.dsserver.entity.character.PlayerCharacter;
import com.git.clownvin.dsserver.entity.projectile.patterns.ConePattern;
import com.git.clownvin.dsserver.item.Inventory;
import com.git.clownvin.dsserver.user.Profile;
import com.git.clownvin.simplepacketframework.connection.Connection;
import com.git.clownvin.simplepacketframework.packet.PacketSystem;
import com.git.clownvin.simpleuserframework.UserContainer;

public class UserConnection extends Connection implements UserContainer<Profile> {
	
	private volatile Profile profile = null;
	private volatile PlayerCharacter character = null;

	public UserConnection(Socket socket, PacketSystem packetSystem) throws IOException {
		super(socket, packetSystem);
	}
	
	public PlayerCharacter getCharacter() {
		return character;
	}
	
	@Override
	public Profile getProfile() {
		return profile;
	}
	
	public boolean loggedIn() {
		return profile != null;
	}
	
	@Override
	public void onKill() {
		//System.out.println("Doing kill?");
		if (!loggedIn()) {
			return;
		}
		character.flagRemoval();
		Server.getUsers().logOut(this);
		//profile = null;
	}

	@Override
	public void setProfile(Profile user) {
		if (loggedIn())
			Server.getUsers().logOut(this.profile); 
		this.profile = user;
		if (user.get("version") == null) {
			user.put("version", 0);
		}
		if (user.<Integer>get("version") < Server.USER_VERSION) {
			updateProfile();
		}
		System.out.println("UserConnection profile: "+profile);
		character = new PlayerCharacter(this);
		this.send(new CharacterPacket(character));
		this.send(new SelfPacket(character.getEID()));
		this.send(this.profile.<Inventory>get("inventory").toPacket());
		//Instances.get(getProfile().instanceNumber).addEntity(character);
		//this.send(new ChunkPacket(Instances.get(user.instanceNumber).getChunk(user.x, user.y, user.z)));
	}
	
	@Override
	public String toString() {
		return super.toString()+"("+profile+")";
	}

	private void updateProfile() {
		System.out.println("Updating user to current version");
		int version = profile.get("version");
		switch (version) {
		case 0:
		case 1:
			profile.put("firepattern1", new ConePattern(3, 30, 8, Config.DEFAULT_MOVE_SPEED * 12, 666, 5, 1.0f));
			profile.put("firepattern2", new ConePattern(3, 15, 8, Config.DEFAULT_MOVE_SPEED * 12, 666, 5, 1.0f));
		case 2:
			profile.put("inventory", new Inventory(Config.INVENTORY_SIZE));
		default:
			profile.put("version", Server.USER_VERSION);
		}
		Server.getUsers().saveUser(profile);
	}

}
