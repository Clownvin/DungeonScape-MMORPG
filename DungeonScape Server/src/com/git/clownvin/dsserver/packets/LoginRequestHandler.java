package com.git.clownvin.dsserver.packets;

import com.git.clownvin.dsapi.packet.LoginRequest;
import com.git.clownvin.dsapi.packet.LoginResponse;
import com.git.clownvin.dsserver.Server;
import com.git.clownvin.dsserver.connection.UserConnection;
import com.git.clownvin.simplepacketframework.packet.AbstractPacketHandler;
import com.git.clownvin.simpleuserframework.UserDatabase;

public class LoginRequestHandler extends AbstractPacketHandler<UserConnection, LoginRequest> {

	@Override
	public boolean handlePacket(UserConnection source, LoginRequest packet) {
		if (packet.getUsername().length() < 3) {
			source.send(new LoginResponse(packet.getReqID(), LoginResponse.USERNAME_TOO_SHORT));
			return true;
		}
		if (packet.getPassword().length() < 5) {
			source.send(new LoginResponse(packet.getReqID(), LoginResponse.PASSWORD_TOO_SHORT));
			return true;
		}
		byte returnValue = Server.getUsers().verifyCredentials(source, packet.getUsername(), packet.getPassword());
		switch (returnValue) {
		case UserDatabase.SUCCESS:
			Server.getUsers().logIn(source);
			source.send(new LoginResponse(packet.getReqID(), LoginResponse.SUCCESS));
			break;
		case UserDatabase.INVALID_CREDENTIALS:
			source.send(new LoginResponse(packet.getReqID(), LoginResponse.INCORRECT));
			break;
		case UserDatabase.ALREADY_LOGGED_IN:
			source.send(new LoginResponse(packet.getReqID(), LoginResponse.ALREADY_LOGGED_IN));
			break;
		case UserDatabase.EXCEPTION_OCCURED:
			source.send(new LoginResponse(packet.getReqID(), LoginResponse.EXCEPTION_OCCURED));
			break;
		default:
			throw new RuntimeException("Unexpected return value: "+returnValue);
		}
		return true;
	}

}
