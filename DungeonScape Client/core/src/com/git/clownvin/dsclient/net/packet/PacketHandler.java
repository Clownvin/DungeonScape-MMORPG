package com.git.clownvin.dsclient.net.packet;

import com.badlogic.gdx.graphics.Color;
import com.git.clownvin.dsapi.packet.CharacterPacket;
import com.git.clownvin.dsapi.packet.CharacterStatusPacket;
import com.git.clownvin.dsapi.packet.ChunkPacket;
import com.git.clownvin.dsapi.packet.InventoryPacket;
import com.git.clownvin.dsapi.packet.MMOPacketHandler;
import com.git.clownvin.dsapi.packet.MessagePacket;
import com.git.clownvin.dsapi.packet.MoveCharacterPacket;
import com.git.clownvin.dsapi.packet.ObjectPacket;
import com.git.clownvin.dsapi.packet.ProjectilePacket;
import com.git.clownvin.dsapi.packet.RemoveCharacterPacket;
import com.git.clownvin.dsapi.packet.RemoveObjectPacket;
import com.git.clownvin.dsapi.packet.RemoveProjectilePacket;
import com.git.clownvin.dsapi.packet.SelfPacket;
import com.git.clownvin.dsapi.packet.ServerTimePacket;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.dsclient.entity.character.ClientCharacter;
import com.git.clownvin.dsclient.entity.object.ClientGameObject;
import com.git.clownvin.dsclient.entity.projectile.ClientProjectile;
import com.git.clownvin.dsclient.world.ClientChunk;
import com.git.clownvin.simplepacketframework.connection.Connection;
import com.git.clownvin.simplepacketframework.packet.Packet;
import com.git.clownvin.simplepacketframework.packet.Request;

public class PacketHandler extends MMOPacketHandler<Connection> {
	
	private final DSGame game;
	
	public PacketHandler(DSGame game) {
		super();
		this.game = game;
	}

	@Override
	public boolean handlePacket(Connection source, Packet packet) {
		//if (!(packet instanceof ServerTimePacket))
	//	System.out.println("Recieved: "+packet);
		ClientCharacter character;
		switch (packet.getType()) {
			case 3:
				MessagePacket message = (MessagePacket) packet;
				Color color = Color.YELLOW;
				switch (message.getColor()) {
				case MessagePacket.BLACK:
					color = Color.BLACK;
					break;
				case MessagePacket.WHITE:
					color = Color.WHITE;
					break;
				case MessagePacket.YELLOW:
					color = Color.YELLOW;
					break;
				case MessagePacket.BLUE:
					color = Color.BLUE;
					break;
				case MessagePacket.CYAN:
					color = Color.CYAN;
					break;
				case MessagePacket.GREEN:
					color = Color.GREEN;
					break;
				case MessagePacket.MAGENTA:
					color = Color.MAGENTA;
					break;
				case MessagePacket.PURPLE:
					color = Color.PURPLE;
					break;
				case MessagePacket.ORANGE:
					color = Color.ORANGE;
					break;
				case MessagePacket.RED:
					color = Color.RED;
					break;
				default: 
					System.out.println("No case for color code: "+message.getColor());
				}
				if (message.getSenderID() == MessagePacket.SYSTEM) {
					game.getGameScreen().addChatMessage(message.getMessage(), color);
				} else {
					game.getGameScreen().addChatMessage(game.getCharacters().getCharacter(message.getSenderID()).getName()+": "+message.getMessage(), color);
					game.getCharacters().getCharacter(message.getSenderID()).setMessage(message.getMessage(), color);
				}
				break;
			case 5: //Chunk packet
				ChunkPacket cp = (ChunkPacket) packet;
				//System.out.println("Recieved chunk: "+cp.getX()+", "+cp.getY());
				game.getChunks().addChunk(new ClientChunk(game, cp.getX(), cp.getY(), cp.getTiles()));
				break;
			case 7: //CharacterPacket
				CharacterPacket cp2 = (CharacterPacket) packet;
				//System.out.println(game.getCharacters().size() +"chars");
				if (game.getCharacters().getCharacter(cp2.getID()) != null)
					break;
				//System.out.println("recieved char: "+cp2.getName()+", "+cp2.getID());
				character = new ClientCharacter(game, cp2.getX(), cp2.getY(), cp2.getWidth(), cp2.getHeight(), cp2.getLastX(), cp2.getLastY(), cp2.getID(), cp2.getHP(), cp2.getSprite(), cp2.getName(), cp2.getAffiliation());
				character.setLookX(cp2.getLookX());
				character.setLookY(cp2.getLookY());
				game.getCharacters().addCharacter(character);
				break;
			case 8: //SelfPacket
				game.setSelfID(((SelfPacket) packet).getSelfID());
				break;
			case 10: //Move character packet
				MoveCharacterPacket cmp = (MoveCharacterPacket) packet;
				//System.out.println("Moving character: "+cmp.getID());
				character = game.getCharacters().getCharacter(cmp.getID());
				//System.out.println("Character: "+character);
				character.setX(cmp.getX());
				character.setY(cmp.getY());
				character.setMoveSpeed(cmp.getMoveSpeed());
				break;
			case 11: //Remove character packet
				RemoveCharacterPacket rcp = (RemoveCharacterPacket) packet;
				character = game.getCharacters().getCharacter(rcp.getID());
				if (character.getEID() == game.getSelfID())
					return true; // We're not removing ourself lol
				game.getCharacters().removeCharacter(character);
				break;
			case 12: // ServerTimePacket
				game.setServerTime(((ServerTimePacket)packet).getServerTime());
				break;
			case 13: //Projectile
				//System.out.println("Recieved projectile");
				ProjectilePacket pp = (ProjectilePacket) packet;
				game.getProjectiles().addProjectile(new ClientProjectile(game, pp.getX(), pp.getY(), pp.getWidth(), pp.getHeight(), pp.getSprite(), pp.getMoveSpeed(), pp.getVelocityX(), pp.getVelocityY(), pp.getCreationTime(), pp.getSourceID(), pp.getID(), pp.getAffiliation()));
				break;
			case 14: //Remove projectile
				RemoveProjectilePacket rpp = (RemoveProjectilePacket) packet;
				game.getProjectiles().removeProjectile(rpp.getID());
				//System.out.println("Removing projectile????");
				break;
			case 16: //Character status
				CharacterStatusPacket csp = (CharacterStatusPacket) packet;
				character = game.getCharacters().getCharacter(csp.getID());
			//	System.out.println("Recieved status packet: " + csp.getHP());
				//System.out.println("Character for id "+csp.getID()+", "+character);
				character.setHP(csp.getHP());
				character.setLookX(csp.getLookX());
				character.setLookY(csp.getLookY());
				break;
			case 17: //Object packet
				ObjectPacket op = (ObjectPacket) packet;
				//System.out.println("Recieved object");
				game.getObjects().addObject(new ClientGameObject(game, op.getID(), op.getX(), op.getY(), op.getWidth(), op.getHeight(), op.getSprite(), op.getSpriteWidth(), op.getSpriteHeight()));
				break;
			case 18:
				RemoveObjectPacket rop = (RemoveObjectPacket) packet;
				game.getObjects().removeObject(rop.getID());
				break;
			case 19: //Inventory packet
				InventoryPacket ip = (InventoryPacket) packet;
				System.out.println("Recieved inventory packet!");
				game.getGameScreen().setInventory(ip.getIDs(), ip.getNames(), ip.getAmounts());
				break;
			default:
				System.err.println("No case for packet with type: "+packet.getType());
		}
		return true;
	}

	@Override
	public boolean handleRequest(Connection source, Request packet) {
		switch (packet.getType()) {
			default:
				System.err.println("No case for request with type: "+packet.getType());
		}
		return true;
	}

}
