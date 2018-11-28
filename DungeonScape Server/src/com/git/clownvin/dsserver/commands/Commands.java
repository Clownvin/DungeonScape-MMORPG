package com.git.clownvin.dsserver.commands;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.git.clownvin.dsapi.packet.ChunkPacket;
import com.git.clownvin.dsapi.packet.MessagePacket;
import com.git.clownvin.dsapi.world.Tile;
import com.git.clownvin.dsserver.Server;
import com.git.clownvin.dsserver.connection.UserConnection;
import com.git.clownvin.dsserver.entity.object.ObjectDefinition;
import com.git.clownvin.dsserver.entity.object.ServerGameObject;
import com.git.clownvin.dsserver.item.Inventory;
import com.git.clownvin.dsserver.item.ServerItem;
import com.git.clownvin.dsserver.util.FileEditor;
import com.git.clownvin.dsserver.util.MapEditor;
import com.git.clownvin.dsserver.world.Instances;
import com.git.clownvin.dsserver.world.Maps;
import com.git.clownvin.dsserver.world.ServerChunk;

public class Commands {
	
	private static MapEditor editor;
	private static String tilename = "grass";
	private static float resistance = 0;
	private static int oid = -1;
	private static int nid = -1, respawnTime = -1;
	
	public static void handleCommand(String command, UserConnection source) {
		if (command.length() <= 0)
			return;
		try {
		String[] tokens = command.split(" ");
		switch (tokens[0].toLowerCase()) {
		case "omap":
			if (editor != null)
				editor.saveMap();
			editor = new MapEditor(Maps.getMap(tokens[1]), source);
			source.send(new MessagePacket("Map \""+tokens[1]+"\" opened for editing.", MessagePacket.SYSTEM, MessagePacket.GREEN));
			break;
		case "smap":
			if (editor == null) {
				source.send(new MessagePacket("MapEditor not bound to map!", MessagePacket.SYSTEM, MessagePacket.RED));
				break;
			}
			editor.saveMap();
			source.send(new MessagePacket("Map saved.", MessagePacket.SYSTEM, MessagePacket.GREEN));
			break;
		case "stile":
			if (editor == null) {
				source.send(new MessagePacket("MapEditor not bound to map!", MessagePacket.SYSTEM, MessagePacket.RED));
				break;
			}
			if (tokens.length < 3) {
				source.send(new MessagePacket("Syntax: /stile <tilename> <res>", MessagePacket.SYSTEM, MessagePacket.RED));
				break;
			}
			tilename = tokens[1];
			resistance = Float.parseFloat(tokens[2]);
			source.send(new MessagePacket("Tile set to "+tilename+", "+Server.getSprite(tilename)+" with resistance "+resistance, MessagePacket.SYSTEM, MessagePacket.GREEN));
			break;
		case "rtile":
			if (editor == null) {
				source.send(new MessagePacket("MapEditor not bound to map!", MessagePacket.SYSTEM, MessagePacket.RED));
				break;
			}
			if (tokens.length < 3) {
				source.send(new MessagePacket("Syntax: /rtile <x> <y>", MessagePacket.SYSTEM, MessagePacket.RED));
				break;
			}
			int x = Integer.parseInt(tokens[1]);
			int y = Integer.parseInt(tokens[2]);
			editor.replaceTile(x, y, new Tile(x, y, resistance, Server.getSprite(tilename)));
			source.send(new MessagePacket("Added tile at "+x+", "+y+".", MessagePacket.SYSTEM, MessagePacket.GREEN));
			break;
		case "aobj":
			if (editor == null) {
				source.send(new MessagePacket("MapEditor not bound to map!", MessagePacket.SYSTEM, MessagePacket.RED));
				break;
			}
			if (tokens.length < 3) {
				source.send(new MessagePacket("Syntax: /aobj <x> <y>", MessagePacket.SYSTEM, MessagePacket.RED));
				break;
			}
			if (oid == -1) {
				source.send(new MessagePacket("Set OID with /soid <oid>", MessagePacket.SYSTEM, MessagePacket.RED));
				break;
			}
			int x2 = Integer.parseInt(tokens[1]);
			int y2 = Integer.parseInt(tokens[2]);
			ServerGameObject obj = new ServerGameObject(Server.getObjects().getDefinition(oid), x2, y2, source.getCharacter().getInstance());
			source.getCharacter().getInstance().addEntity(obj);
			Server.getObjects().add(obj);
			editor.addObject(oid, x2, y2);
			source.send(new MessagePacket("Added object at "+x2+", "+y2+".", MessagePacket.SYSTEM, MessagePacket.GREEN));
			break;
		case "anpc":
			if (editor == null) {
				source.send(new MessagePacket("MapEditor not bound to map!", MessagePacket.SYSTEM, MessagePacket.RED));
				break;
			}
			if (tokens.length < 5) {
				source.send(new MessagePacket("Syntax: /anpc <x> <y> <x2> <y2>", MessagePacket.SYSTEM, MessagePacket.RED));
				break;
			}
			if (nid == -1) {
				source.send(new MessagePacket("Set NID with /snid <nid>", MessagePacket.SYSTEM, MessagePacket.RED));
				break;
			}
			int x3 = Integer.parseInt(tokens[1]);
			int y3 = Integer.parseInt(tokens[2]);
			int x4 = Integer.parseInt(tokens[3]);
			int y4 = Integer.parseInt(tokens[4]);
			editor.addNPC(nid, x3, y3, x4, y4, respawnTime);
			source.send(new MessagePacket("Added npc at "+x3+", "+y3+" to "+x4+", "+y4+" with respawn timer of "+respawnTime+".", MessagePacket.SYSTEM, MessagePacket.GREEN));
			break;
		case "soid":
			if (editor == null) {
				source.send(new MessagePacket("MapEditor not bound to map!", MessagePacket.SYSTEM, MessagePacket.RED));
				break;
			}
			if (tokens.length < 2) {
				source.send(new MessagePacket("Syntax: /soid <oid>", MessagePacket.SYSTEM, MessagePacket.RED));
				break;
			}
			oid = Integer.parseInt(tokens[1]);
			source.send(new MessagePacket("Set OID to "+oid, MessagePacket.SYSTEM, MessagePacket.GREEN));
			break;
		case "snid":
			if (editor == null) {
				source.send(new MessagePacket("MapEditor not bound to map!", MessagePacket.SYSTEM, MessagePacket.RED));
				break;
			}
			if (tokens.length < 3) {
				source.send(new MessagePacket("Syntax: /snid <nid> <respawn time>", MessagePacket.SYSTEM, MessagePacket.RED));
				break;
			}
			nid = Integer.parseInt(tokens[1]);
			respawnTime = Integer.parseInt(tokens[2]);
			source.send(new MessagePacket("Set NID to "+nid+" and respawn time to "+respawnTime, MessagePacket.SYSTEM, MessagePacket.GREEN));
			break;
		case "loc":
			source.send(new MessagePacket("Location: ("+source.getCharacter().getIX()+", "+source.getCharacter().getIY()+"), ("+source.getCharacter().getX()+", "+source.getCharacter().getY()+"), ("+source.getCharacter().getActualX()+", "+source.getCharacter().getActualY()+")", MessagePacket.SYSTEM, MessagePacket.PURPLE));
			break;
		case "tele":
			if (tokens.length == 2) {
				switch (tokens[1]) {
				case "home":
					source.getCharacter().setX(0);
					source.getCharacter().setY(0);
					source.getCharacter().setInstanceNumber(Instances.OVERWORLD);
					source.getCharacter().updateLocation();
					break;
				default:
					source.send(new MessagePacket("Unknown location. Try \"home\"", MessagePacket.SYSTEM, MessagePacket.RED));
					break;
				}
			}
			else {
				source.getCharacter().setX(Integer.parseInt(tokens[1]));
				source.getCharacter().setY(Integer.parseInt(tokens[2]));
				source.getCharacter().setInstanceNumber(Instances.OVERWORLD);
				source.getCharacter().updateLocation();
			}
			break;
		case "move":
			if (tokens.length < 3) {
				source.send(new MessagePacket("Syntax: /move +-x +-y", MessagePacket.SYSTEM, MessagePacket.RED));
				break;
			}
			source.getCharacter().setX(source.getCharacter().getX() + Integer.parseInt(tokens[1]));
			source.getCharacter().setY(source.getCharacter().getY() + Integer.parseInt(tokens[2]));
			source.getCharacter().setInstanceNumber(Instances.OVERWORLD);
			source.getCharacter().updateLocation();
			break;
		case "aitem":
			if (tokens.length != 3) {
				source.send(new MessagePacket("syntax: /aitem <id> <amount>", MessagePacket.SYSTEM, MessagePacket.RED));
				return;
			}
			long added = source.getProfile().<Inventory>get("inventory").addItem(new ServerItem(Integer.parseInt(tokens[1]), Long.parseLong(tokens[2])));
			source.send(new MessagePacket("Added all but "+added+" of the items you requested.", MessagePacket.SYSTEM, MessagePacket.GREEN));
			break;
		case "ritem":
			if (tokens.length != 3) {
				source.send(new MessagePacket("syntax: /ritem <id> <amount>", MessagePacket.SYSTEM, MessagePacket.RED));
				return;
			}
			long removed = source.getProfile().<Inventory>get("inventory").removeItem(new ServerItem(Integer.parseInt(tokens[1]), Long.parseLong(tokens[2])));
			source.send(new MessagePacket("Removed "+removed+" of the items from your inventory.", MessagePacket.SYSTEM, MessagePacket.GREEN));
			break;
		case "citem":
			if (tokens.length != 2) {
				source.send(new MessagePacket("syntax: /citem <id>", MessagePacket.SYSTEM, MessagePacket.RED));
				return;
			}
			long amt = source.getProfile().<Inventory>get("inventory").getItemAmount(Integer.parseInt(tokens[1]));
			source.send(new MessagePacket("You have "+amt+" of that item", MessagePacket.SYSTEM, MessagePacket.GREEN));
			break;
		case "invincible":
		case "godmode":
		case "god":
			source.getCharacter().setDamageReduction(1.0f);
			break;
		default:
			System.out.println("Unknown command: "+tokens[0]);
			source.send(new MessagePacket("---Commands---", MessagePacket.SYSTEM, MessagePacket.ORANGE));
			source.send(new MessagePacket("omap <map/map>", MessagePacket.SYSTEM, MessagePacket.ORANGE));
			source.send(new MessagePacket("smap", MessagePacket.SYSTEM, MessagePacket.ORANGE));
			source.send(new MessagePacket("stile <name> <resistance>", MessagePacket.SYSTEM, MessagePacket.ORANGE));
			source.send(new MessagePacket("rtile <x> <y>", MessagePacket.SYSTEM, MessagePacket.ORANGE));
			source.send(new MessagePacket("soid <oid>", MessagePacket.SYSTEM, MessagePacket.ORANGE));
			source.send(new MessagePacket("aobj <x> <y>", MessagePacket.SYSTEM, MessagePacket.ORANGE));
			source.send(new MessagePacket("snid <nid> <respawn time>", MessagePacket.SYSTEM, MessagePacket.ORANGE));
			source.send(new MessagePacket("anpc <x> <y> <x2> <y2>", MessagePacket.SYSTEM, MessagePacket.ORANGE));
			source.send(new MessagePacket("aitem <item> <amount>", MessagePacket.SYSTEM, MessagePacket.ORANGE));
			source.send(new MessagePacket("ritem <item> <amount>", MessagePacket.SYSTEM, MessagePacket.ORANGE));
			source.send(new MessagePacket("citem <item> <amount>", MessagePacket.SYSTEM, MessagePacket.ORANGE));
			source.send(new MessagePacket("god/invincible", MessagePacket.SYSTEM, MessagePacket.ORANGE));
			source.send(new MessagePacket("tele <home : x y>", MessagePacket.SYSTEM, MessagePacket.ORANGE));
			source.send(new MessagePacket("loc", MessagePacket.SYSTEM, MessagePacket.ORANGE));
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
