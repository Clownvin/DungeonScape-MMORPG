package com.git.clownvin.dsserver.packets;

import java.util.Set;

import com.git.clownvin.dsapi.entity.Entity;
import com.git.clownvin.dsapi.packet.ChunkPacket;
import com.git.clownvin.dsapi.packet.ChunkRequest;
import com.git.clownvin.dsapi.world.Chunk;
import com.git.clownvin.dsapi.world.Tile;
import com.git.clownvin.dsserver.connection.UserConnection;
import com.git.clownvin.dsserver.entity.ServerEntity;
import com.git.clownvin.dsserver.world.Instances;
import com.git.clownvin.math.MathUtil;
import com.git.clownvin.simplepacketframework.packet.AbstractPacketHandler;

public class ChunkRequestHandler extends AbstractPacketHandler<UserConnection, ChunkRequest> {

	@Override
	public boolean handlePacket(UserConnection source, ChunkRequest packet) {
		Chunk chunk = Instances.get(source.getProfile().instanceNumber).getChunk(MathUtil.ard(packet.getX()), MathUtil.ard(packet.getY()));
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
//		LinkedList<ServerEntity> entities = chunk.getEntities();
//		//System.out.println("recieved chunk req");
//		synchronized (entities) {
//			for (ServerEntity e : entities) {
//				source.send(e.toPacket());
//			}
//		}
		return true;
	}

}
