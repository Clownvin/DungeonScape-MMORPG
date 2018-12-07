package com.git.clownvin.dsserver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.JFrame;

import com.git.clownvin.dsapi.config.Config;
import com.git.clownvin.dsserver.connection.UserConnection;
import com.git.clownvin.dsserver.entity.character.Characters;
import com.git.clownvin.dsserver.entity.object.Objects;
import com.git.clownvin.dsserver.entity.projectile.Projectiles;
import com.git.clownvin.dsserver.entity.projectile.patterns.FiringPattern;
import com.git.clownvin.dsserver.entity.projectile.patterns.FiringPatterns;
import com.git.clownvin.dsserver.item.Items;
import com.git.clownvin.dsserver.packets.PacketHandler;
import com.git.clownvin.dsserver.user.Profile;
import com.git.clownvin.dsserver.util.IDSystem;
import com.git.clownvin.dsserver.util.IDSystem.ID;
import com.git.clownvin.dsserver.util.SpriteTable;
import com.git.clownvin.dsserver.world.Instances;
import com.git.clownvin.dsserver.world.Maps;
import com.git.clownvin.simplepacketframework.packet.Packets;
import com.git.clownvin.simplescframework.AbstractServer;
import com.git.clownvin.simplescframework.connection.ConnectionAcceptor;
import com.git.clownvin.simpleuserframework.UserDatabase;

public class Server extends AbstractServer {

	private static Server server;
	private static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private static UserDatabase<Profile, UserConnection> users = new UserDatabase<>(Profile.class);
	private static LinkedList<UserConnection> connectionList = new LinkedList<>();
	private static PacketHandler packetHandler;
	private static Characters characters;
	private static Objects objects;
	private static Projectiles projectiles;
	private static SpriteTable spriteTable;
	private static FiringPatterns firingPatterns;
	private static IDSystem idSystem;
	private static volatile long serverTime = 0L;
//	private static volatile int nextID = 0;

//	public static int getNextID() {
//		if (nextID == Integer.MAX_VALUE)
//			nextID = 0;
//		return nextID++;
//	}
//	
	public static final int USER_VERSION = 3;

	private static long startTime;

	public static Characters getCharacters() {
		return characters;
	}

	public static FiringPattern getFiringPattern(String pattern) {
		return firingPatterns.getPattern(pattern);
	}

	public static LinkedList<UserConnection> getConnectionList() {
		return connectionList;
	}

	public static ID getID() {
		return idSystem.getNextID();
	}

	public static Server getInstance() {
		return server;
	}

	public static Objects getObjects() {
		return objects;
	}

	public static Integer getSprite(String sprite) {
		return spriteTable.getSprite(sprite);
	}

	public static Projectiles getProjectiles() {
		return projectiles;
	}

	public static UserDatabase<Profile, UserConnection> getUsers() {
		return users;
	}

	public static Date getServerTimeAsDate() {
		return new Date(getServerTime());
	}

	public static void main(String[] args) {
		// for (int i = 0; i < 100; i++) {
		// generateMap(720, 720, "sakt");
		// }
		startTime = System.currentTimeMillis();
		packetHandler = new PacketHandler();
		Packets.setPacketHandler(packetHandler);
		ConnectionAcceptor.setConnectionClass(UserConnection.class);
		ConnectionAcceptor.setOnConnectConsumer((c, p) -> {
			synchronized (connectionList) {
				UserConnection connection = (UserConnection) c;
				connectionList.add(connection);
				System.out.println(connection + ": Added connection");
			}
		});
		// Packets.setDebug(true);
		server = new Server();
		server.start();
	}

	public static long getServerTime() {
		return serverTime;
	}

	private volatile long tickConsumption = 0L;

	private static volatile long tickCount = 0L;

	public static long getTickCount() {
		return tickCount;
	}

	private Server() {
		super("MMOServer", 6942);
	}

	@Override
	public void atStart() {
		JFrame frame = new JFrame("Close me to kill server!");
		 frame.setVisible(true);
		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 frame.setSize(100, 100);
		idSystem = new IDSystem(200_000);
		spriteTable = new SpriteTable();
		firingPatterns = new FiringPatterns();
		characters = new Characters();
		objects = new Objects();
		projectiles = new Projectiles();
		Maps.loadMaps();
		Items.loadItemDefinitions();
		Maps.loadMap("sakt/sakt");
		Instances.put(Maps.getMap("sakt/sakt").createInstance(Instances.OVERWORLD));
		System.out.println("Server finished start up, time: "
				+ String.format("%.3f", ((System.currentTimeMillis() - startTime) / 1000f)) + "s");
	}

	@Override
	public void atStop() {
		System.out.println("Server stopping..");
	}

	private long lastInfo = 0L;
	private long instanceTime = 0L, characterTime = 0L, projectileTime = 0L;

	@Override
	public void duringLoop() throws InterruptedException {
		long start = System.currentTimeMillis();
		// ServerTimePacket packet = new ServerTimePacket(serverTime);
		// synchronized (connectionList) {
		// for (UserConnection connection : connectionList) {
		// connection.send(packet);
		// }
		// }
		// try {
		// packetHandler.update();
		// } catch (Exception e) {

		// }
		long start2 = System.currentTimeMillis();
		try {
			Instances.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
		instanceTime += System.currentTimeMillis() - start2;
		start2 = System.currentTimeMillis();
		try {
			characters.update();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		characterTime += System.currentTimeMillis() - start2;
		start2 = System.currentTimeMillis();
		try {
			projectiles.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
		projectileTime += System.currentTimeMillis() - start2;
		tickConsumption += System.currentTimeMillis() - start;
		tickCount++;
		if (System.currentTimeMillis() - lastInfo >= 60000) {
			System.out.println("------------Info------------");
			System.out.println("Current server time: " + dateFormat.format(getServerTimeAsDate()));
			System.out.println("Tick count: " + tickCount);
			System.out
					.println("Tick Consumption: "
							+ String.format("%08.4f",
									((tickConsumption / (float) (System.currentTimeMillis() - lastInfo)) * 100.0f))
							+ "%");
			System.out.println("Char Consumption: "
					+ String.format("%08.4f", ((characterTime / (float) tickConsumption) * 100.0f)) + "%");
			System.out.println("Proj Consumption: "
					+ String.format("%08.4f", ((projectileTime / (float) tickConsumption) * 100.0f)) + "%");
			System.out.println("Inst Consumption: "
					+ String.format("%08.4f", ((instanceTime / (float) tickConsumption) * 100.0f)) + "%");
			System.out.println("Projectiles: " + projectiles.getCount());
			System.out.println("Characters: " + characters.getCount());
			System.out.println("----------------------------");
			tickConsumption = 0L;
			characterTime = 0L;
			projectileTime = 0L;
			instanceTime = 0L;
			lastInfo = System.currentTimeMillis();
		}
		serverTime += Config.TICK_RATE;
		long toSleep = Config.TICK_RATE - (System.currentTimeMillis() - start);
		if (toSleep > 0)
			Thread.sleep(toSleep);
		// lse
		// System.out.println(-toSleep+" ms behind.");
	}

}
