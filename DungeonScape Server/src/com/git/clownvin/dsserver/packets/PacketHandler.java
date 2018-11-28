package com.git.clownvin.dsserver.packets;

import java.util.Set;

import com.git.clownvin.dsapi.entity.Entity;
import com.git.clownvin.dsapi.packet.ActionPacket;
import com.git.clownvin.dsapi.packet.ChunkPacket;
import com.git.clownvin.dsapi.packet.ChunkRequest;
import com.git.clownvin.dsapi.packet.ClientStatusPacket;
import com.git.clownvin.dsapi.packet.LoginRequest;
import com.git.clownvin.dsapi.packet.LoginResponse;
import com.git.clownvin.dsapi.packet.MMOPacketHandler;
import com.git.clownvin.dsapi.packet.MessagePacket;
import com.git.clownvin.dsapi.packet.VelocityPacket;
import com.git.clownvin.dsapi.world.Chunk;
import com.git.clownvin.dsapi.world.Tile;
import com.git.clownvin.dsserver.Server;
import com.git.clownvin.dsserver.commands.Commands;
import com.git.clownvin.dsserver.connection.UserConnection;
import com.git.clownvin.dsserver.entity.ServerEntity;
import com.git.clownvin.dsserver.world.Instances;
import com.git.clownvin.dsserver.world.ServerChunk;
import com.git.clownvin.math.MathUtil;
import com.git.clownvin.simplepacketframework.packet.Packet;
import com.git.clownvin.simplepacketframework.packet.Request;
import com.git.clownvin.simpleuserframework.UserDatabase;

public class PacketHandler extends MMOPacketHandler<UserConnection> {

	@Override
	public boolean handlePacket(UserConnection source, Packet packet) {
		ServerChunk chunk;
		switch (packet.getType()) {
		case 3: //MESSAGE
			MessagePacket mp = (MessagePacket) packet;
			String messageString = mp.getMessage();
			if (messageString.startsWith("/")) {
				Commands.handleCommand(messageString.substring(1), source);
			} else {
				MessagePacket message = new MessagePacket(messageString, source.getCharacter().getEID(), MessagePacket.WHITE);
				chunk = Instances.get(source.getProfile().instanceNumber).getChunk(source.getCharacter().getIX(), source.getCharacter().getIX());
				Instances.get(source.getProfile().instanceNumber).sendPacketToChunks(chunk.x, chunk.y, message);
			}
			break;
		case 4: //CLIENT STATUS
			ClientStatusPacket status = (ClientStatusPacket) packet;
			switch (status.getStatus()) {
			case ClientStatusPacket.READY:
				source.send(new MessagePacket("Welcome to DungeonScape!", MessagePacket.SYSTEM, MessagePacket.WHITE));
				break;
			default:
				System.out.println("Unknown client status: "+status.getStatus());
			}
			break;
		case 6: //CHUNK REQUEST
			ChunkRequest req = (ChunkRequest) packet;
			chunk = Instances.get(source.getProfile().instanceNumber).getChunk(MathUtil.ard(req.getX()), MathUtil.ard(req.getY()));
			//System.out.println("Sending chunk "+chunk);
			source.send(new ChunkPacket(chunk));
			int x2 = chunk.x + Chunk.WIDTH;
			int y2 = chunk.y + Chunk.HEIGHT;
			Entity e;
			Set<Integer> set = null;
			for (int x = chunk.x; x < x2; x++) {
				for (int y = chunk.y; y < y2; y++) {
					//try {

					synchronized (chunk) {
					set = chunk.entitiesKeySet(x, y);
					//} catch (Exception e1) {
						//System.out.println(chunk+", "+x+", "+y+", "+set);
					//	System.exit(0);
						//return true;
				//	}
						for (Integer i : set) {
							e = chunk.getEntity(x, y, i);
							if (e instanceof Tile)
								continue;
							source.send(((ServerEntity)e).toPacket());
						}
					set = chunk.charactersKeySet(x, y);
					//if (set.size() > 0)
					//System.out.println("Sending characters, count: "+set.size());
						for (Integer i : set) {
							//System.out.println("Sending character: "+((ServerEntity)chunk.getCharacter(x, y, i)));
							source.send(((ServerEntity)chunk.getCharacter(x, y, i)).toPacket());
						}
					}
				}
			}
//			LinkedList<ServerEntity> entities = chunk.getEntities();
//			//System.out.println("recieved chunk req");
//			synchronized (entities) {
//				for (ServerEntity e : entities) {
//					source.send(e.toPacket());
//				}
//			}
			break;
		case 9:
			VelocityPacket p = (VelocityPacket) packet;
			source.getCharacter().setVelocityX(p.getVelocityX());
			source.getCharacter().setVelocityY(p.getVelocityY());
			source.getCharacter().setGear(p.getSpeed());
			if (!source.getCharacter().isMoving() && (p.getVelocityX() != 0.0f || p.getVelocityY() != 0.0f))
				source.getCharacter().startMoving();
			else if (source.getCharacter().isMoving() && (p.getVelocityX() == 0.0f && p.getVelocityY() == 0.0f))
				source.getCharacter().stopMoving();
			break;
		case 15: //ACTION
			ActionPacket ap = (ActionPacket) packet;
			switch (ap.getAction()) {
			case ActionPacket.FIRE_PRIMARY:
				//System.out.println("Recieved packet w/ velocity: "+ap.getDirectionX() +", "+ ap.getDirectionY());
				source.getCharacter().firePrimary(ap.getDirectionX(), ap.getDirectionY());
				source.getCharacter().setLookX(ap.getDirectionX());
				source.getCharacter().setLookY(ap.getDirectionY());
				break;
			case ActionPacket.FIRE_SECONDARY:
				source.getCharacter().fireSecondary(ap.getDirectionX(), ap.getDirectionY());
				source.getCharacter().setLookX(ap.getDirectionX());
				source.getCharacter().setLookY(ap.getDirectionY());
				break;
			case ActionPacket.LOOK_AT:
				source.getCharacter().setLookX(ap.getDirectionX());
				source.getCharacter().setLookY(ap.getDirectionY());
				break;
			default:
				System.out.println("Unknown action: "+ap.getAction());
			}
			//System.out.println("Recieved packet to shoot bullet");
			break;
		default:
			System.out.println("Recieved packet from "+source+": "+packet);
		}
		return true;
	}

	@Override
	public boolean handleRequest(UserConnection source, Request packet) {
		switch (packet.getType()) {
		case 1: //LOGIN REQ
			LoginRequest req = (LoginRequest) packet;
			if (req.getUsername().length() < 3) {
				source.send(new LoginResponse(req.getReqID(), LoginResponse.USERNAME_TOO_SHORT));
				break;
			}
			if (req.getPassword().length() < 5) {
				source.send(new LoginResponse(req.getReqID(), LoginResponse.PASSWORD_TOO_SHORT));
				break;
			}
			byte returnValue = Server.getUsers().verifyCredentials(source, req.getUsername(), req.getPassword());
			switch (returnValue) {
			case UserDatabase.SUCCESS:
				Server.getUsers().logIn(source);
				source.send(new LoginResponse(req.getReqID(), LoginResponse.SUCCESS));
				break;
			case UserDatabase.INVALID_CREDENTIALS:
				source.send(new LoginResponse(req.getReqID(), LoginResponse.INCORRECT));
				break;
			case UserDatabase.ALREADY_LOGGED_IN:
				source.send(new LoginResponse(req.getReqID(), LoginResponse.ALREADY_LOGGED_IN));
				break;
			case UserDatabase.EXCEPTION_OCCURED:
				source.send(new LoginResponse(req.getReqID(), LoginResponse.EXCEPTION_OCCURED));
				break;
			default:
				throw new RuntimeException("Unexpected return value: "+returnValue);
			}
			break;
		default:
			System.out.println("Recieved request ("+packet.getReqID()+") from "+source+": "+packet);
		}
		return true;
	}

}
