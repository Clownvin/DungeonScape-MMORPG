package com.git.clownvin.dsserver.entity.character;

import com.git.clownvin.dsapi.config.Config;
import com.git.clownvin.dsapi.packet.MessagePacket;
import com.git.clownvin.dsserver.Server;
import com.git.clownvin.dsserver.connection.UserConnection;
import com.git.clownvin.dsserver.entity.projectile.patterns.FiringPattern;
import com.git.clownvin.dsserver.item.Inventory;
import com.git.clownvin.dsserver.user.Profile;
import com.git.clownvin.dsserver.world.Instance;
import com.git.clownvin.dsserver.world.Instances;
import com.git.clownvin.math.MathUtil;

public class PlayerCharacter extends ServerCharacter {
	
	private final UserConnection connection;
	
	private Instance instance = null;
	
	private Integer x, y, lastX, lastY;
	
	private byte gear = 0;
	
	private float damageReduction = 0.0f;
	
	public byte getGear() {
		return gear;
	}
	
	public void setGear(byte gear) {
		this.gear = gear;
		setMoveSpeed(gear * Config.DEFAULT_MOVE_SPEED);
	}
	
	public static final float RADIUS = 4f;
	
	public float originX, originY;
	
	public PlayerCharacter(UserConnection connection) {
		this.connection = connection;
		System.out.println("Connection profile: "+connection.getProfile());
		Profile p = connection.getProfile();
		setX(p.x);
		setY(p.y);
		lastX = MathUtil.ard(p.lastX);
		lastY = MathUtil.ard(p.lastY);
		updateLocation();
		Server.getCharacters().add(this);
	}
	
	public byte getAffiliation() {
		return GOOD;
	}
	
	@Override
	public void dispose() {
		Instance i = Instances.get(getInstanceNumber());
		i.removeEntity(this, true);
		if (getInstanceNumber() != Instances.OVERWORLD) { //They're in custom instance, need to back them out to entrance in overworld.
			setX(i.map.entX);
			setY(i.map.entY);
			setInstanceNumber(Instances.OVERWORLD);
		}
		super.dispose();
		//Server.getCharacters().remove(this);
	}

	public UserConnection getConnection() {
		return connection;
	}

	@Override
	public int getFireRate() {
		return (int) (Config.TICK_RATE);
	}
	
	@Override
	public float getHeight() {
		return 32;
	}
	
	@Override
	public float getHP() {
		return connection.getProfile().hp;
	}
	
	@Override
	public Instance getInstance() {
		return instance;
	}

	@Override
	public int getInstanceNumber() {
		return connection
				.getProfile()
				.instanceNumber;
	}

	@Override
	public Integer getIX() {
		return x;
	}

	@Override
	public Integer getIY() {
		return y;
	}

	@Override
	public Integer getLastIX() {
		return lastX;
	}

	@Override
	public Integer getLastIY() {
		return lastY;
	}

	@Override
	public float getLastX() {
		return connection.getProfile().lastX;
	}

	@Override
	public float getLastY() {
		return connection.getProfile().lastY;
	}
	
	@Override
	public String getName() {
		return connection
				.getProfile()
				.getUsername();
	}

	@Override
	public float getResistance() {
		return 0.0f;
	}

	@Override
	public int getSprite() {
		return Server.getSprite("basic_player_sprite"); // Stick.png
	}

	@Override
	public float getWidth() {
		return 32;
	}
	
	@Override
	public float getX() {
		return connection.getProfile().x;
	}
	
	@Override
	public float getY() {
		return connection.getProfile().y;
	}

	@Override
	public void setHP(float hp) {
		connection.getProfile().hp = hp;
		updateStatus();
		//System.out.println("hp = "+connection.getProfile().hp);
		//getInstance().sendPacketToChunks(getIX(), getIY(), getIZ(), new CharacterStatusPacket(this));
	}

	@Override
	public void setInstance(Instance instance) {
		this.instance = instance;
	}

	@Override
	public void setInstanceNumber(int instanceNumber) {
		int oldNum = connection.getProfile().instanceNumber;
		connection.getProfile().instanceNumber = instanceNumber;
		if (oldNum != instanceNumber)
			updateLocation();
	}

	@Override
	public void setX(float x) {
		Profile profile = connection.getProfile();
		profile.lastX = profile.x;
		profile.x = x;
		this.lastX = this.x;
		this.x = MathUtil.ard(x);
	}

	@Override
	public void setY(float y) {
		Profile profile = connection.getProfile();
		profile.lastY = profile.y;
		profile.y = y;
		this.lastY = this.y;
		this.y = MathUtil.ard(y);
	}

	@Override
	public String toString() {
		return connection.getProfile().getUsername()+"'s Character";
	}

	@Override
	public void update() {
		super.update();
		//System.out.println(this.getTicksForMoveTick());
		if (getHP() <= 0.0f) {
			getConnection().send(new MessagePacket("Oh no! You have died!", MessagePacket.SYSTEM, MessagePacket.RED));
			setX(getInstance().map.entX);
			setY(getInstance().map.entY);
			setInstanceNumber(Instances.OVERWORLD);
			updateLocation();
			setHP(100.0f);
		}
		if (connection.getProfile().<Inventory>get("inventory").hasChanged()) {
			connection.send(connection.getProfile().<Inventory>get("inventory").toPacket());
		}
	}
	
	private long fireDelay = 0L;
	private int fireCount = 0;
	
	public void firePrimary(float directionX, float directionY) {
		if (System.currentTimeMillis() - fireDelay < getFireRate())
			return;
		connection.getProfile().<FiringPattern>get("firepattern1").update(this);
		fireDelay = System.currentTimeMillis() + getFireRate();
		fireCount++;
	}
	
	public void fireSecondary(float directionX, float directionY) {
		if (System.currentTimeMillis() - fireDelay < getFireRate())
			return;
		connection.getProfile().<FiringPattern>get("firepattern2").update(this);
		fireDelay = System.currentTimeMillis() + getFireRate();
		fireCount++;
	}
	
	public int getFireCount() {
		return fireCount;
	}

	@Override
	public float getRadius() {
		return RADIUS;
	}
	
	public void setDamageReduction(float damageReduction) {
		this.damageReduction = damageReduction;
	}

	@Override
	public float getDamageReduction() {
		return damageReduction;
	}
}
